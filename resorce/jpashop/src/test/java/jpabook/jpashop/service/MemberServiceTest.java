package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRespository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.engine.IElementDefinitionsAware;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //unit실행시 spring으로 실행할래
@SpringBootTest
@Transactional //test에 있을 시 롤백됨 - insert쿼리 안나감
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRespository memberRespository;

    @Autowired
    EntityManager em;


    @Test
    //@Rollback(value = false)
    public void 회원가입() throws Exception{
    //given
        Member member = new Member();
        member.setName("kim");

    //when
        Long saveId = memberService.join(member);

        //then
        em.flush();
        assertEquals(member,memberRespository.findOne(saveId));
        //pk값이 같은면 같은 영속성을 가짐

    }

    @Test(expected = IllegalStateException.class) //예외 잡아줌
    public void 중복회원예외() throws Exception{
    //given
        Member member1 = new Member();
        member1.setName("ken1");

        Member member2 = new Member();
        member2.setName("ken1");


        //when
        memberService.join(member1);
        memberService.join(member2); //예외


    //then
        fail("예외 발생해야 한다.");
    }



}