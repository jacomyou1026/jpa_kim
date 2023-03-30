package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberServiceTest {
    MemberService memberService;
    MemberMemberRepository memberMemberRepository ;
    @BeforeEach
    public void beforeEach(){
        memberMemberRepository = new MemberMemberRepository();
        memberService = new MemberService(memberMemberRepository);
    }

//    shift+f10 : 이전 실행한거 실행
    @AfterEach
    public void  aftherEach(){
        memberMemberRepository.clearStore();
    }



    @Test
    void 회원가입() {
//        given
        Member member = new Member();
        member.setName("hello");

//        when
        Long saveID =  memberService.join(member);

//        then
        Member result = memberService.findOne(saveID).get();
        assertThat(member.getName()).isEqualTo(result.getName());
    }

    @Test
    public void 중복_회원_예외(){
//        given
        Member member1 = new Member() ;
        member1.setName("spring");

        Member member2 = new Member() ;
        member2.setName("spring");

//        when
        memberService.join(member1);
//        단축키 Ctrl+shift+v
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");


//        try {
//            memberService.join(member2);
//            fail("예외발생");
//
//        } catch (Exception e) {
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원");
//            throw new RuntimeException(e);
//        }


//        than


    }


    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}