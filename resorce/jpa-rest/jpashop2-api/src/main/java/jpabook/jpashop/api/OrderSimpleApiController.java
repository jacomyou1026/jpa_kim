package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/*
xToOne(ManyToOne, OneToOne)
Order
Order ->Member
Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            System.out.println("order = " + order);
        }
        for (Order order : all) {
            order.getMember().getName(); //Lazy강제 초기화
            order.getMember().getAddress(); //Lazy강제 초기화

        }
            return all;
    }
    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X) -1
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        //Order 2개
        // N+1 -> 1+회원 N(2)+배송 N
        return orderRepository.findAllByString(new OrderSearch()).
                stream().map(SimpleOrderDto::new).
                collect(Collectors.toList());

    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O) -2
     * - fetch join으로 쿼리 1번 호출
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     * 재사용성0- entity로 이용
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        //쿼리 1
        List<Order> orders = orderRepository.findAllWithMemberDelivery(); 
        //Entitiy -> DTO변환
        return orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }



    /**
     * V4. JPA에서 DTO로 바로 조회
     * -쿼리1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회
     * -v3보다 성능 최적화
     * -재사용성이 불가(OrderSimpleQueryDto)dto가 정해져있어서
     * 코함드상 더 지져분
     */

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;

        //2(N)+1
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //1
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); ///2
        }
    }


}
