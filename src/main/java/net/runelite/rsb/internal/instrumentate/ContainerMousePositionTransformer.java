package net.runelite.rsb.internal.instrumentate;

import javassist.*;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

@Slf4j
public class ContainerMousePositionTransformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className,
                            Class classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        if (className.equals("java/awt/Container")) {
            log.debug("Instrumenting java/awt/Container");
            try {
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod[] methods = ctClass.getDeclaredMethods();
                classPool.importPackage("net.runelite.rsb.botLauncher.Application");
                for (CtMethod method : methods) {
                    if (method.getName().contains("getMousePosition")) {
                        method.setBody("{" +
                                "return new java.awt.Point(0, 0);" +
                                "}");
                    }
                }
                byteCode = ctClass.toBytecode();
                ctClass.detach();
                log.debug("Instrumentation of java/awt/Container complete");
            } catch (Throwable ex) {
                log.error("Failed to instrument java/awt/Container", ex);
            }
        }
        return byteCode;
    }
}
