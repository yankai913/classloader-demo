package com.zoo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * 服务容器，用于装载，启动目录服务
 * 
 * @author kai.yank
 * 
 */
public class ServiceContainer {

    private static String DEFAULT_SERVICE_DIR = "META-INF/services/";

    private static SpringClassLoader springClassLoader;


    public static void start() {
        start(DEFAULT_SERVICE_DIR);
    }


    public static void start(String serviceDir) {
        ClassLoader parent = ServiceContainer.class.getClassLoader();
        ServiceClassLoader serviceClassLoader = new ServiceClassLoader(parseServiceDir(serviceDir), parent);
        springClassLoader = new SpringClassLoader();
        springClassLoader.setServiceClassLoader(serviceClassLoader);
        Thread.currentThread().setContextClassLoader(springClassLoader);
    }


    private static URL[] parseServiceDir(String serviceDir) {
        try {
            URL url = ServiceContainer.class.getClassLoader().getResource(serviceDir);
            if (url != null) {
                serviceDir = url.getPath();
            }
            List<java.net.URL> urlList = new ArrayList<java.net.URL>();
            File dir = new File(serviceDir);
            if (!dir.isDirectory()) {
                throw new IllegalStateException(dir.getPath() + " is not dir");
            }
            for (File file : dir.listFiles()) {
                if (file.getPath().endsWith(".jar")) {
                    urlList.add(new URL("file:" + file.getPath()));
                }
            }
            if (urlList.isEmpty()) {
                throw new IllegalStateException("urlList is empty");
            }
            return urlList.toArray(new java.net.URL[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Class<?> loadClass(String name) throws ClassNotFoundException {
        return springClassLoader.loadClass(name);
    }
}
