package everymeal.server.global.util.swagger;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeExample {
    Class<? extends BaseExceptionList> value();
}
