package rsb.plugin.base;

import javax.swing.*;

public class DebugPanel extends JPanel {

    JCheckBox drawBoundaries = new JCheckBox("Draw Boundaries");
    JCheckBox drawMouse = new JCheckBox("Draw Mouse");
    JCheckBox drawGround = new JCheckBox("Draw Ground");
    JCheckBox drawSettings = new JCheckBox("Draw Settings");
    JCheckBox drawWeb = new JCheckBox("Draw Web");

    public DebugPanel() {
        super();
        init();
    }

    private void init() {
        this.add(drawBoundaries);
        this.add(drawMouse);
        this.add(drawGround);
        this.add(drawSettings);
        this.add(drawWeb);
    }
}
