package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof UserException) {
                log.info("UserException reslover to 400");
                String acceptHeader = request.getHeader("accept");
                //accept 가 json 인경우는 json 형식으로 보내주고 그렇지 않으면
                //HTML 화면으로 이동 시킨다


                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //400


                //acceptHeader가 json일 경우
                if ("application/json".equals(acceptHeader)) {
                    Map<String,Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    //객체 -> 문자
                    String result = objectMapper.writeValueAsString(errorResult);
                    log.info("objectMapper = {}", result);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result); //response에 Json

                    return new ModelAndView();


                }else{
                    // acceptHeader가 Text/HTML,
                    return new ModelAndView("error/500");
                }


            }

        } catch (IOException e) {
            log.error("resolver ex",e);
        }
        return null; // 다음 ExceptionResolver 찾기 OR 기존 발생 예외를 서블릿 밖으로 던점(500)

    }
}
