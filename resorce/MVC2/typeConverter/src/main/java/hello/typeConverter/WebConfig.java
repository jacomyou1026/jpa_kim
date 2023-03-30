package hello.typeConverter;

import hello.typeConverter.converter.IntegerToStringConverter;
import hello.typeConverter.converter.IpPortToStringConverter;
import hello.typeConverter.converter.StringToIntegerConverter;
import hello.typeConverter.converter.StringToIpPortConverter;
import hello.typeConverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    //컨버터 등록
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //주석처리 우선순위
//        registry.addConverter(new IntegerToStringConverter());
//        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IpPortToStringConverter());
        registry.addConverter(new StringToIpPortConverter());

        //추가(우선순위  : 컨버터 > 포맷터)
        registry.addFormatter(new MyNumberFormatter());
    }
}
