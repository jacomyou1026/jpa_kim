package hello.login.web.argumentResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //파라미터에만 붙힐 수 있는 애노테이션이다
@Retention(RetentionPolicy.RUNTIME) //리플렉션 등을 활용할 수 있도록 런타임까지 애노테이션 정보가 남아있도록 해준다
public @interface Login {

}
