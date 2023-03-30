package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final MemberRepository memberRepository;

    /*
    @return null이면 로그인 실패
    * */
    public Member login(String loginID,String password){
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginID);
//        Member member = findMemberOptional.get();
////        if (member.getPassword(  ).equals(password)) {
////            return member;
////        }else {
////            return null;
////        }

        //ctrl+alt+n : 합치기
        // 자바8 OptionalStream공부!
        return memberRepository.findByLoginId(loginID)
                .filter(m -> m.getPassword().equals(password)).orElse(null);

    }

}
