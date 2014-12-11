package com.zoo;

import java.lang.reflect.Method;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
    public static void main(String[] args) throws Exception {
        String startClass = "org.springframework.context.support.ClassPathXmlApplicationContext";
        // 任意绝对路径
//         ServiceContainer.start("C:/services");
        // 非classpath
//         ServiceContainer.start("services/");
        // classpath
        ServiceContainer.start();
        Class<?> ctxClass = ServiceContainer.loadClass(startClass);
        ClassPathXmlApplicationContext ctx =
                (ClassPathXmlApplicationContext) ctxClass.getConstructor(new Class<?>[] { String.class })
                    .newInstance(new Object[] { "beans.xml" });
        Object obj = ctx.getBean("helloService");
        System.out.println(obj);
        Method method = obj.getClass().getDeclaredMethod("sayHello", new Class<?>[] { String.class });
        method.invoke(obj, new Object[] { "hello" });
    }
}
