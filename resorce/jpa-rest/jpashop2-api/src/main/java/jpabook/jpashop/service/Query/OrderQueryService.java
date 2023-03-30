package jpabook.jpashop.service.Query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.api.OrderSimpleApiController;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 패키지도 따로 두는 것이 좋음
 * 쿼리용 서비스 별도 분리해라
 * 
 * Open - Session -in-View를 꺼둬 잘 실행 - Service계층이여서
 * 
 * 김영한 선호
 */

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }
}
