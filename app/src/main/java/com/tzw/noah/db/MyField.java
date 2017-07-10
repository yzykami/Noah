package com.tzw.noah.db;

/**
 * Created by yzy on 2017/7/10.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//表示注解的信息被保留在class文件(字节码文件)中当程序编译时，会被虚拟机保留在运行时
@Target(ElementType.FIELD)//说明该注解只能被声明在一个类的方法前
public @interface MyField {
    String name() default "";
}
