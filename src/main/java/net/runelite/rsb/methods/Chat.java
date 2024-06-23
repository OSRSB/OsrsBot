package net.runelite.rsb.methods;

import net.runelite.rsb.wrappers.RSWidget;
import java.util.ArrayList;
import java.util.Objects;

import static net.runelite.rsb.internal.globval.GlobalWidgetInfo.CHATBOX_MESSAGE_LINES;

/**
 * Chat box related operations.
 *
 * @author zebdo
 */
public class Chat extends MethodProvider {

    Chat(MethodContext ctx) {
        super(ctx);
    }

    /**
     * Returns the chat log as an array of strings
     *
     * @return An array of <code>Strings</code>.
     */
    public String[] getChatLog() {
        ArrayList<String> clAL = new ArrayList<>();
        RSWidget[] msgWidgets = methods.interfaces.getComponent(CHATBOX_MESSAGE_LINES).getComponents();
        for (RSWidget i : msgWidgets) {
            if (!Objects.equals(i.getText(), "")) {
                clAL.add(i.getText());
            }
        }
        return clAL.toArray(new String[clAL.size()]);
    }
}
