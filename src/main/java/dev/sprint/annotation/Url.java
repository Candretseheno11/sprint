package main.java.dev.sprint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import main.java.dev.sprint.constant.HttpMethod;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Url {

    String value();
    HttpMethod method() default HttpMethod.GET;
}
