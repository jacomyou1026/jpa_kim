package hello.typeConverter.controller;

import hello.typeConverter.type.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello-v1")
    public String hellloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); //문자 타입 조회
        Integer integerVaue = Integer.valueOf(data);//숫자 타입으로 변경
        System.out.println("integerVaue = " + integerVaue);
        return "ok";

    }

    //추가한 컨버터 > 기본 컨버터
    @GetMapping("/hello-V2") //"1,00"
    public String helloV2(@RequestParam Integer data) {
        System.out.println("data = " + data);
        return "ok";
    }


    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        System.out.println("ipPort.getIp() = " + ipPort.getIp());
        System.out.println("ipPort.getPort() = " + ipPort.getPort());
        return "ok";
    }
}
