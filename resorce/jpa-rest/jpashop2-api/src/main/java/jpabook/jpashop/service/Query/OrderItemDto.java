package jpabook.jpashop.service.Query;

import jpabook.jpashop.domain.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemDto {
    private String itemName; //상품명
    private int orderPrice; //주문 가격
    private int count; //주문 수량

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName(); //2
        orderPrice = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }

}