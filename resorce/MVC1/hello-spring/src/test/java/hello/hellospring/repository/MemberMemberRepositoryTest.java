package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


public class MemberMemberRepositoryTest {
    MemberMemberRepository repository = new MemberMemberRepository();

    @AfterEach
//    하나의 테스크가 끝나면 여기로 온다. @AfterEach
    public  void afterEach(){
        repository.clearStore();

    }
    @Test
    public void save(){
        Member member = new Member();
        member.setName("Spring");
        repository.save(member);
        Member result =  repository.finaById(member.getId()).get();
//        검증
        org.assertj.core.api.Assertions.assertThat(member).isEqualTo(result);

//      검증
        Assertions.assertEquals(member,result);

    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("Spring1");
        repository.save(member1);
        
//        shift+F6: 이름 변경
         Member member2 = new Member();
        member2.setName("Spring1");
        repository.save(member2);

        Member result =  repository.findByName("Spring1").get();

        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(member1);

    }

    @Test
    public void finalAll(){
        Member member1 = new Member();
        member1.setName("Spring1");
        repository.save(member1);

//        shift+F6: 이름 변경
        Member member2 = new Member();
        member2.setName("Spring1");
        repository.save(member2);
        List<Member> result =  repository.finalAll();
        org.assertj.core.api.Assertions.assertThat(result.size()).isEqualTo(2);



    }





}
