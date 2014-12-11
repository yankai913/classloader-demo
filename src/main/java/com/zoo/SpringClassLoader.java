package com.zoo;

/**
 * 设置为当前上下文默认类加载器
 * 
 * @author kai.yank
 * 
 */
public class SpringClassLoader extends ClassLoader {

    private ServiceClassLoader serviceClassLoader;


    public ServiceClassLoader getServiceClassLoader() {
        return serviceClassLoader;
    }


    public void setServiceClassLoader(ServiceClassLoader serviceClassLoader) {
        this.serviceClassLoader = serviceClassLoader;
    }


    private Class<?> loadClasspathClass(String name) {
        try {
            return super.loadClass(name);
        } catch (Exception e) {
        }
        return null;
    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // first load classPath, then load third Libs;
        try {
            Class<?> clazz = loadClasspathClass(name);
            if (clazz != null) {
                return clazz;
            }
            clazz = loadThirdLibsClass(name);
            if (clazz != null) {
                return clazz;
            }
        } catch (Exception e) {
        }
        throw new ClassNotFoundException(name);
    }


    private Class<?> loadThirdLibsClass(String name) {
        try {
            return serviceClassLoader.loadClass(name);
        } catch (Exception e) {
        }
        return null;
    }
}