package hello.typeConverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {



    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text={},locale={}", text, locale);
        //"1,000" -> 1000(객체 -> 숫자)
        NumberFormat format = NumberFormat.getInstance(locale);
        //ctrl+alt+N = 문자 합치기
        return format.parse(text);
    }

    //    @Override
//    public String print(Number object, Locale locale) {
//        log.info("object={}, locale={}", object, locale);
//        NumberFormat instance = NumberFormat.getInstance(locale);
//        String format = instance.format(object);
//        return format;
//    }

    @Override
    public String print(Number object, Locale locale) {
        log.info("object={}, locale={}", object, locale);
        return NumberFormat.getInstance(locale).format(object);
    }


}
