package hello.typeConverter.formatter;

import hello.typeConverter.converter.IpPortToStringConverter;
import hello.typeConverter.converter.StringToIntegerConverter;
import hello.typeConverter.converter.StringToIpPortConverter;
import hello.typeConverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

import static org.assertj.core.api.Assertions.assertThat;

public class FormattingConversionServiceTest {

    @Test
    void formattingConversionServiceTest() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        //컨버터 등록
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        //포멧터 등록
        conversionService.addFormatter(new MyNumberFormatter());

        //컨버터 사용
        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));


        //포맷터 사용
        String convert = conversionService.convert(1000, String.class);
        assertThat(convert).isEqualTo("1,000");

        Long result = conversionService.convert("1,000", Long.class);
        assertThat(result).isEqualTo(1000L);
    }
}
