package hello.login.web.session;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }

        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name ->log.info("session name = {}",name,session.getAttribute(name)));
        log.info("session.getAttribute(SessionConst.LOGIN_MEMBER); :  {}", session.getAttribute(SessionConst.LOGIN_MEMBER));

        Object result = session.getAttribute(SessionConst.LOGIN_MEMBER);

        log.info("sessionId ={}",session.getId());
        log.info("getMaxInactiveInterval = {}",session.getMaxInactiveInterval()); //세션의 유효시간
        log.info("getCreationTime = {}",new Date(session.getCreationTime())); //세션 생성일자
        log.info("getLastAccessedTime = {}",new Date (session.getLastAccessedTime())); //마지막으로 접근 시간
        log.info("isNew = {}",session.isNew()); //새로생성된 세션이냐?

        return "세션 출력";
    }
}
