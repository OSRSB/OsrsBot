package rsb.walker.dax_api.walker.handlers.special_cases;

import rsb.walker.dax_api.walker.handlers.special_cases.impl.SecurityStrongholdHandler;
import rsb.walker.dax_api.walker.handlers.special_cases.impl.ShantyPassHandler;
import rsb.walker.dax_api.walker.models.MoveTask;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialCaseHandlers {

    private static final List<SpecialCaseHandler> list = Collections.unmodifiableList(Arrays.asList(
            new ShantyPassHandler(),
            new SecurityStrongholdHandler()
                                                                                                   ));

    public static SpecialCaseHandler getSpecialCaseHandler(MoveTask moveTask) {
        List<SpecialCaseHandler> handlers = list.stream().filter(specialCaseHandler -> specialCaseHandler.shouldHandle(moveTask)).collect(
		        Collectors.toList());

        if (handlers.size() > 1) {
            throw new IllegalStateException(String.format(
                    "Duplicate special case handlers for a single move task: %s %s",
                    moveTask, handlers.stream().map(SpecialCaseHandler::getName).collect(Collectors.joining("|"))
                                                         ));
        }

        return handlers.size() > 0 ? handlers.get(0) : null;
    }

}
