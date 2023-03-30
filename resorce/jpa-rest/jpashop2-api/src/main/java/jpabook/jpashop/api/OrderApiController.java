package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.Query.OrderQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    //Entity직접노출 - x
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch()); //다 가져옴

        //강제 초기화
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o->o.getItem().getName()); // 강제초기화
//            for (OrderItem orderItem : orderItems) {
//                orderItem.getItem().getName();
//            }

        }

        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }


    private final OrderQueryService orderQueryService;

    //패치조인
    @GetMapping("/api/v3/orders")
    public List<jpabook.jpashop.service.Query.OrderDto> ordersV3() {
//        List<Order> orders = orderRepository.findAllWithItem();
//        List<OrderDto> result = orders.stream()
//                .map(o -> new OrderDto(o))
//                .collect(toList());

        return orderQueryService.ordersV3();
    }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환 페이징 고려
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화 - id로 in을 넣어
     * in의 개수 사이즈가 얼마를 넣고 싶은 지를 결정
     * -
     * 선호
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit); //페이징 (fetch join)
        for (Order order : orders) {
            System.out.println("order = " + order);
            System.out.println("order.getMember().getName() = " + order.getMember().getName());
            System.out.println("ord/er.getMember().getAddress() = " + order.getMember().getAddress());

        }
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return result;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();

    }


    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();

    }



//페이징x
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }




        //속에있는것 까지 다 Entity외부 노출x
    @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        //OrderItem 조차 DTO로 변환
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //1
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus(); //2
            address = order.getMember().getAddress(); //3

            //Entity여서 -> DTO변환
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());

        }
    }

    @Getter
    static class OrderItemDto {
        private String itemName; //상품명
        private int orderPrice; //주문 가격
        private int count; //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName(); //2
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }

    }




}
