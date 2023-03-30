package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SesstionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final SesstionManager sesstionManager;


    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    //@PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO
        //쿠키에 시간정보를 주지않으면 세션쿠키(브로우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);
        return "redirect:/";

    }

   // @PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        //로그인 성공 처리 TODO

        //세션 관리자를 통해 세션 생성, 데이터 보관
        //쿠키에 시간정보를 주지않으면 세션쿠키(브로우저 종료시 모두 종료)
        sesstionManager.createSession(loginMember,response);

        return "redirect:/";

    }

   // @PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO
        //HttpSession 하면 쿠키 생성
        //세션이 있으면 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();


        //세션에 로그인 회원정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);




        return "redirect:/";

    }


    @PostMapping("/login")
    public String loginV4(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult,HttpServletRequest request
    ,@RequestParam(defaultValue = "/")String redirectURL) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO
        //HttpSession 하면 쿠키 생성
        //세션이 있으면 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession( );

        //세션에 로그인 회원정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);
        log.info("SessionConst.LOGIN_MEMBER : {} ",SessionConst.LOGIN_MEMBER);

        return "redirect:"+redirectURL;

    }

    //  @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }
//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        //쿠키값을 꺼내서 expire을 시키기 위해 request필요
        sesstionManager.expire(request);
        return "redirect:/";

    }

   @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        //쿠키값을 꺼내서 expire을 시키기 위해 request필요
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); //세션과 그안의 데이터 날라감
        }

        return "redirect:/";

    }

    //control+alt+m
    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
