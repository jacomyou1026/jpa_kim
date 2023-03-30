package hello.servelet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
1. 파라미터 전송기능
http://localhost:8080/requst-param?user-name=helllo&age=20
 */
@WebServlet(name="requestParmServlet", urlPatterns = "/request-param")
public class RequestParmServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회] - start");
        request.getParameterNames().asIterator().
                forEachRemaining(paramName-> System.out.println(paramName+"="+request.getParameter(paramName)));
        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");

        System.out.println("username = " + username);
        System.out.println("age = " + age);

//        http://localhost:9092/requst-parm?username=helllo&age=20&username=hello2
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        String[] usernames = request.getParameterValues("username");
        for(String name:usernames){
            System.out.println("usernames = " + name);
        }

        response.getWriter().write("Okays");

    }
}
