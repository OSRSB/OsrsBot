package rsb.botLauncher;
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
    }

    /**
     * This class delegates (child then parent) for the findClass method for a URLClassLoader.
     * We need this because findClass is protected in URLClassLoader
     */
    private static class ChildClassLoader extends ClassLoader
    {
        private FindClassClassLoader realParent;

        public ChildClassLoader(String name, FindClassClassLoader realParent)
        {
            super(name, null);

            this.realParent = realParent;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException
        {
            Class<?> loaded = super.findLoadedClass(name);
            if(loaded != null)
                return loaded;
            try
            {
                // first try to use the URLClassLoader findClass
                return super.findClass(name);
            }
            catch( ClassNotFoundException e )
            {
                // if that fails, we ask our real parent classloader to load the class (we give up)
                return realParent.loadClass(name);
            }
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
