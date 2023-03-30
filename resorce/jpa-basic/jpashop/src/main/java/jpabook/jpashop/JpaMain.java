package jpabook.jpashop;

import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        //EntityManagerFactory는 EntityManager를 생성하기 위해 팩토리 인터페이스로 persistence단위별로 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //EntityManager객체 생성
        //EntityManager는 persistence Context와 Entity 관리
        EntityManager em = emf.createEntityManager();

        //EntityManager에서 트랜잭션을 가져와 위한객체 생성
        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랙잭션 시작

        try {
            Order order = new Order();
            order.setStatus(OrderStatus.ORER);


            em.persist(order);


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            //code -DB저장
            //엔티티 매니져 종료
            em.close();
        }
        //WAS가 내려갈때 닫아줌 -> connection pool resource release
        //팩토리  종료. 커넥션 풀 자원 반환
        emf.close();



    }
}
