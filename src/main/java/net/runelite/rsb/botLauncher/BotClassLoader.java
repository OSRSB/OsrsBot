package net.runelite.rsb.botLauncher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class BotClassLoader extends ClassLoader {

    private ChildClassLoader childClassLoader;

    /**
     * This class allows me to call findClass on a classloader
     */
    private static class FindClassClassLoader extends ClassLoader
    {
        public FindClassClassLoader(ClassLoader parent)
        {
            super(parent);
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException
        {
            return super.findClass(name);
        }



        public Class<?> findLoaded(String name)
        {
            return this.findLoadedClass(name);
        }
    }

    /**
     * This class delegates (child then parent) for the findClass method for a URLClassLoader.
     * We need this because findClass is protected in URLClassLoader
     */
    private static class ChildClassLoader extends ClassLoader
    {
        private final FindClassClassLoader realParent;

        public ChildClassLoader(String name, FindClassClassLoader realParent)
        {
            super(name, null);
            this.realParent = realParent;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException
        {
            Class<?> loaded = realParent.findLoaded(name);
            if( loaded != null )
                return realParent.findClass(name);
            if (name.startsWith("java.")) {
                return ClassLoader.getPlatformClassLoader().loadClass(name);
            }
            try {
                //Contains for subclasses and subpackages
                if (name.equals("net.runelite.rsb.botLauncher.BotLiteInterface") ||
                    name.startsWith("net.runelite.rsb.internal.globval.GlobalConfiguration") ||
                    name.startsWith("javax.") ||
                    name.startsWith("org.pushingpixels")
                ) {
                    return realParent.loadClass(name);
                }
                byte[] bt = loadClassData(name);
                return defineClass(name, bt, 0, bt.length);
            } catch (Exception e) {
                return realParent.findClass(name);
            }
        }

        private byte[] loadClassData(String className) {
            //read class
            InputStream is = getResourceAsStream(className.replace(".", "/")+".class");
            ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
            //write into byte
            int len =0;
            try {
                while((len=is.read())!=-1){
                    byteSt.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //convert into byte array
            return byteSt.toByteArray();
        }

        @Override
        public Enumeration<URL> getResources(String name) throws IOException {
            List<URL> allRes = new LinkedList<>();

            // load resource from this classloader
            Enumeration<URL> thisRes = findResources(name);
            if (thisRes != null) {
                while (thisRes.hasMoreElements()) {
                    allRes.add(thisRes.nextElement());
                }
            }

            // then try finding resources from parent classloaders
            Enumeration<URL> parentRes = super.findResources(name);
            if (parentRes != null) {
                while (parentRes.hasMoreElements()) {
                    allRes.add(parentRes.nextElement());
                }
            }

            return new Enumeration<URL>() {
                Iterator<URL> it = allRes.iterator();

                @Override
                public boolean hasMoreElements() {
                    return it.hasNext();
                }

                @Override
                public URL nextElement() {
                    return it.next();
                }
            };
        }

        @Override
        public URL getResource(String name) {
            URL res = findResource(name);
            if (res == null) {
                res = realParent.getResource(name);
            }
            return res;
        }

    }


    public BotClassLoader(String name)
    {
        super(Thread.currentThread().getContextClassLoader());

        childClassLoader = new ChildClassLoader(name, new FindClassClassLoader(this.getParent()));
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        try
        {
            // first we try to find a class inside the child classloader
            return childClassLoader.findClass(name);
        }
        catch( ClassNotFoundException e )
        {
            // didn't find it, try the parent
            return super.loadClass(name, resolve);
        }
    }
}
