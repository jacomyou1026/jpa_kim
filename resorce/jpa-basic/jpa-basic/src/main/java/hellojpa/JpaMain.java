package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

        //영속상태가 되면 PK값이 세팅되고 영속상태가 된다.
        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("homecity1", "street", "1000"));


            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("oldcity2", "street2", "2000"));
            member.getAddressHistory().add(new AddressEntity("oldcity3", "street2", "2000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("==============");
            Member findMember = em.find(Member.class, member.getId());

            //homecity -> new city
//            findMember.getHomeAddress().setCity("Newcity");


            Address homeAddress = findMember.getHomeAddress();
            //새로 넣어야함
            findMember.setHomeAddress(new Address("newCity", homeAddress.getStreet(), homeAddress.getZipcode()));


            //치킨 - > 한식
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");


            tx.commit();
        } catch (Exception e) {
            tx. rollback();
            e.printStackTrace();
        }finally {
            //code -DB저장
            //엔티티 매니져 종료
            em.close();
        }
        //WAS가 내려갈때 닫아줌 -> connection pool resource release
        //팩토리 종료. 커넥션 풀 자원 반환
        emf.close();

    }

}
