package net.runelite.rsb.internal.instrumentate;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

@Slf4j
public class ComponentMousePositionTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className,
              Class classBeingRedefined, ProtectionDomain protectionDomain,
              byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        if (className.equals("java/awt/Component")) {
            log.debug("Instrumenting java/awt/Component");
            try {
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                classPool.getClassLoader();

                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if (method.getName().contains("getMousePosition")) {
                        method.setBody("{" +
                                "return new java.awt.Point(0, 0);" +
                                "}");
                    }
                }
                byteCode = ctClass.toBytecode();
                ctClass.detach();
                log.debug("Instrumentation of java/awt/Component complete");
            } catch (Throwable ex) {
                log.error("Failed to instrument java/awt/Component", ex);
            }
        }
        return byteCode;
    }
}
