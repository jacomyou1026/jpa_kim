package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    private final EntityManager em;

    //command랑 qury분리/ 핵심 비즈니스. 그렇지 않은것 분리 - class 쪼개기
    List<Member> findAllMembers() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
