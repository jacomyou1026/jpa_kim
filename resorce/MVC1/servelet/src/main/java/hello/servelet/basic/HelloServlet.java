package hello.servelet.basic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//서블릿이란 자바를 사용하여 웹을 만들기 위해 필요한 기술입니다.
//클라이언트가 어떠한 요청을 하면 그에 대한 결과를 다시 전송해주어야 하는데, 이러한 역할을 하는 자바 프로그램입니다.

@WebServlet(name="helloServlet",urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("HelloServlet.service");
        System.out.println("request = " + request);
        System.out.println("response = " + response);

//        Ctrl+alt+v : Class명과 변수 이름
        String username = request.getParameter("username");
        System.out.println("username = " + username);

//        헤더 정보 들어감
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");

//  body에 들어감
        response.getWriter().write("hello"+username);

//        서블릿 이름과 url매핑은 겹치면 안됨

    }
}
