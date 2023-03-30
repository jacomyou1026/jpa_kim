package hello.servelet.web.frontController.v5;

import hello.servelet.web.frontController.ModelView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MyHandlerAdapter {

    boolean support(Object handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response,Object handler) throws ServletException, IOException;


}
