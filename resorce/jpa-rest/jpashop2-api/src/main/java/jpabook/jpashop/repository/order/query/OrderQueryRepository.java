package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Entity가 아닌

@Repository @RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;


    //N+1문제 터짐
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders(); //query 1번 - > N개 

        result.forEach(o->{
             List<OrderItemQueryDto> orderItems= findOrderItems(o.getOrderId()); //query N번
            System.out.println("o.getOrderId() = " + o.getOrderId());
             o.setOrderItems(orderItems);
                });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count) " +
                        "from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id =:orderId", OrderItemQueryDto.class)
                .setParameter("orderId",orderId).getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.status,d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class

        ).getResultList();
    }

    //쿼리 2번만 나감
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        //orderId 리스트
        List<Long> orderIds = orderIds(result);
        System.out.println("orderIds = " + orderIds);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItems(orderIds);

        System.out.println("orderItemMap = " + orderItemMap);

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItems(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count) " +
                                "from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        //key : OrderItemQueryDto.getOrderId() , value - >List<OrderItemQueryDto>
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
        return orderItemMap;
    }

    private static List<Long> orderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream().map(
                        o -> o.getOrderId()
        ).collect(Collectors.toList());
        return orderIds;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id,m.name,o.orderDate,o.status,d.address,i.name,oi.orderPrice,oi.count) from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i ",OrderFlatDto.class
        ).getResultList();


    }
}
