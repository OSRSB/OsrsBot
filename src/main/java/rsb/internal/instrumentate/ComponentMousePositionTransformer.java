package rsb.internal.instrumentate;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class ComponentMousePositionTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className,
              Class classBeingRedefined, ProtectionDomain protectionDomain,
              byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        // since this transformer will be called when all the classes are
        // loaded by the classloader, we are restricting the instrumentation
        // using if block only for the Component & Container classes
        if (className.equals("java/awt/Component")) {
            System.out.println("Instrumenting......");
            try {
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(
                        classfileBuffer));
                classPool.getClassLoader();

                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if (method.getName().contains("getMousePosition")) {
                        method.setBody("{" +
                                //"try {" +
                                "System.out.println(\"DING\");" +
                                //"}catch(NoClassDefFoundError e) { System.out.println(\"Cheesesticks\"); }" +
                                "return new java.awt.Point (0,0);" +


                                "}");
                    }
                }
                byteCode = ctClass.toBytecode();
                ctClass.detach();
                System.out.println("Instrumentation complete.");
            } catch (Throwable ex) {
                System.out.println("Exception: " + ex);
                ex.printStackTrace();
            }
        }
        return byteCode;
    }
}
