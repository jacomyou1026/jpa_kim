package hello.itemservice.message;

import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class messageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage(){
        String result = ms.getMessage("hello", null, null);
//        introduce local variable(자동 변수생성)  : command + alt(option) + V
//        static import : alt+enter
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode(){
//메시지를 찾을 수 없음
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);

    }

    @Test
    void notFoundMessageCodeDefaultMessage(){
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    void argumentMessage(){
        String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(message).isEqualTo("안녕 Spring");

    }
    
//    국제화
//    -메시지 소스는 국제화까지 지원
    @Test
    void defaultLang(){
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");

    }

    @Test
    void enLang(){
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }


}


