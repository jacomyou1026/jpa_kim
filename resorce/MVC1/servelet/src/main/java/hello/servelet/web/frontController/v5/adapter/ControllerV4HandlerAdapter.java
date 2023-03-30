package hello.servelet.web.frontController.v5.adapter;

import hello.servelet.web.frontController.ModelView;
import hello.servelet.web.frontController.v4.ControllerV4;
import hello.servelet.web.frontController.v5.MyHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tags.shaded.org.apache.xpath.axes.HasPositionalPredChecker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean support(Object handler) {
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV4 controller = (ControllerV4) handler;

        Map<String, String> paramMap = createParamMap(request); //model
        HashMap<String, Object> model = new HashMap<>();

        String viewName = controller.process(paramMap, model); //view
        ModelView mv = new ModelView(viewName); //viewName
        mv.setModel(model); //model

        return mv;
    }
    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator().
                forEachRemaining(paramName->paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
