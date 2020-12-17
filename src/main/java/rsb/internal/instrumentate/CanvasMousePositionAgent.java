package rsb.internal.instrumentate;

import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;

@Slf4j
public class CanvasMousePositionAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        log.info("[PREMAIN] Changes to component and container calls commencing...");
        inst.addTransformer(new ComponentMousePositionTransformer());
        inst.addTransformer(new ContainerMousePositionTransformer());
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        log.info("[AGENTMAIN] Changes to component and container calls commencing...");
    }
}