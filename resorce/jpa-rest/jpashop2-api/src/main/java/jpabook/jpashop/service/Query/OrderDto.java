package jpabook.jpashop.service.Query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//속에있는것 까지 다 Entity외부 노출x
    @Getter
public class OrderDto {
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

