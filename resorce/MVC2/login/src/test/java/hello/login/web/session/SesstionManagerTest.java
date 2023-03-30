package hello.login.web.session;

import hello.login.domain.member.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SesstionManagerTest {

    SesstionManager sesstionManager = new SesstionManager();

    @Test
    void sessionTest(){

//       세션 생성
        //가짜로 response를 test할 수 있도록 MockHttpServletResponse() 제공
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sesstionManager.createSession(member,response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회
        Object result = sesstionManager.getSeesion(request);
        assertThat(result).isEqualTo(member);

        //세션 만료
        sesstionManager.expire(request);
        Object expired = sesstionManager.getSeesion(request);
        assertThat(expired).isNull();

    }



}