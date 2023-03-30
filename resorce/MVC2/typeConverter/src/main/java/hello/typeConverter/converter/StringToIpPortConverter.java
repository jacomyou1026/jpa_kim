package hello.typeConverter.converter;

import hello.typeConverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.awt.color.ICC_Profile;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {
    @Override
    public IpPort convert(String source) {
        log.info("convert source ={}",source);
        //"127.0.0.0:8080" ->ipPort객체
        String[] split = source.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        return new IpPort(ip, port);
    }
}
