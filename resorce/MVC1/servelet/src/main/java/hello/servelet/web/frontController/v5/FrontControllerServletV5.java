package hello.servelet.web.frontController.v5;

import hello.servelet.web.frontController.ModelView;
import hello.servelet.web.frontController.MyView;
import hello.servelet.web.frontController.v3.ControllerV3;
import hello.servelet.web.frontController.v3.controller.MemberFormControllerV3;
import hello.servelet.web.frontController.v3.controller.MemberListControllerV3;
import hello.servelet.web.frontController.v3.controller.MemberSaveControllerV3;
import hello.servelet.web.frontController.v4.controller.MemberFormControllerV4;
import hello.servelet.web.frontController.v4.controller.MemberListControllerV4;
import hello.servelet.web.frontController.v4.controller.MemberSaveControllerV4;
import hello.servelet.web.frontController.v5.adapter.ControllerV3HandlerAdapter;
import hello.servelet.web.frontController.v5.adapter.ControllerV4HandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "frontControllerServletV5",urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //handler 찾기
        Object handler = getHandler(request); //new MemberFormControllerV3()

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //headler Adapter 찾기
        MyHandlerAdapter adapter = getHandlerAdapter(handler); //new MemberFormControllerV3()

        ModelView mv = adapter.handle(request, response, handler);

        String  viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(),request, response);

    }



    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI(); ///front-controller/v5/v3/members/new-form
        Object handler = handlerMappingMap.get(requestURI);
        return handler; // new MemberFormControllerV3())
    }
    private MyHandlerAdapter getHandlerAdapter(Object handler) { // new MemberFormControllerV3())
        //new MemberFormControllerV3()
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.support(handler)) {
                return adapter;
            }

        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler"+handler);
    }
    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/"+viewName +".jsp");
    }
}
