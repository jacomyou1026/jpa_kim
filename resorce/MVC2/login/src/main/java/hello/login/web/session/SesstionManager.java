package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//세션 관리
@Component
public class SesstionManager {
    public static final String Session_Cookie_Name = "mySessionId";

    //동시에 여러 쓰레드가 접근할 시ConcurrentHashMap 사용
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /*
    세션 생성
    * sessionId생성 (임의의 추정 불가능한 랜덤 값)
    * 세션 저장소에 sessionId와 보관할 값 저장
    *sessionId로 응답 쿠키를 생성해서 클라이언트 전달
    * */

    public void createSession(Object value, HttpServletResponse response){
        //세션 id를 생성, 값을 세션에 저장
        //임의의 값을 얻을 수 있다.
        String sessionId =  UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //상수로 만들기 - ctrl+alt+c
        Cookie mySessionCookie = new Cookie(Session_Cookie_Name, sessionId);
        response.addCookie(mySessionCookie);
    }

    /*
    세션 조회
     */
    public Object getSeesion(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, Session_Cookie_Name);

        if (sessionCookie == null) {
            return null;
        }
        return sessionStore.get(sessionCookie.getValue()); //value

    }

    /*
    세션 만료
    * */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, Session_Cookie_Name);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }


    public Cookie findCookie(HttpServletRequest request,String cookiName){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        //.findAny() 순서 상관x 먼저 나온 애
        //findFirst() 순서0 - 첫번째로 나온 애
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookiName))
                .findFirst()
                .orElse(null);

    }



}
