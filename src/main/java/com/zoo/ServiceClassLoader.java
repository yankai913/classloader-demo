package com.zoo;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * 加载指定目录的第三方包
 * 
 * @author kai.yank
 * 
 */
public class ServiceClassLoader extends URLClassLoader {

    private static ConcurrentHashMap<String, Class<?>> classCacheMap =
            new ConcurrentHashMap<String, Class<?>>();

    private URL[] urls;


    public ServiceClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.urls = urls;
        init();
    }


    private void init() {
        for (URL url : urls) {
            initClassName(url);
        }
    }


    private void initClassName(java.net.URL url) {
        String path = url.getPath();
        try {
            JarFile jarFile = new JarFile(path);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String classFileName = jarEntry.getName();
                if (classFileName.endsWith(".class")) {
                    classFileName = classFileName.replace("/", ".");
                    String className = classFileName.substring(0, classFileName.lastIndexOf("."));
                    loadClass(className);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz = classCacheMap.get(name);
        if (clazz == null) {
            clazz = super.loadClass(name);
            classCacheMap.put(name, clazz);
        }
        return clazz;
    }
}