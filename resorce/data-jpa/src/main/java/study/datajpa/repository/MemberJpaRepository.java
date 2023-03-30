package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        //JPQL
        return em.createQuery("select  m from Member m", Member.class)
                .getResultList();

    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult(); //결과가 정확히 하나일 경우

    }


    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUserAndAge(String username, int age) {
        return em.createQuery("select m from Member m where m.username =:username and m.age >:age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsernames(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age =:age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset) //어디서 부터 가지고 올거야
                .setMaxResults(limit)  //개수를 몇개 가지고 올거야
                .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age =:age", Long.class)
                .setParameter("age",age)
                .getSingleResult(); //한건만 있기에

    }

    //한꺼번에 수정
    public int bulkAgePlus(int age) {
        return em.createQuery(
                        "update Member m set m.age= m.age+1 " +
                                "where m.age >=:age")
                .setParameter("age",age)
                .executeUpdate();
    }






}
