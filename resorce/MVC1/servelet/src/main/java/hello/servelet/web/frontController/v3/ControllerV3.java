package hello.servelet.web.frontController.v3;

import hello.servelet.web.frontController.ModelView;

import java.util.Map;

public interface ControllerV3 {
    ModelView process(Map<String,String> paramMap);
}
