package hello.typeConverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    //문자 -> 숫자
    public Integer convert(String source) {
        log.info("convert soucrce = {}" ,source);
        Integer integer = Integer.valueOf(source);
        return integer;
    }

}
