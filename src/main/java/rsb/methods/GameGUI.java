package rsb.methods;

import net.runelite.api.widgets.Widget;
import rsb.internal.globval.GlobalWidgetInfo;
import rsb.internal.globval.enums.InterfaceTab;
import rsb.internal.globval.enums.ViewportLayout;

/**
 * For internal use to find GUI components.
 *
 * @author GigiaJ
 */
public class GameGUI extends MethodProvider {

	public GameGUI(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * @return The compasses <code>Widget</code>;otherwise null.
	 */
	public synchronized Widget getCompass() {
		ViewportLayout layout = getViewportLayout();
		if (layout != null) {
			switch (layout) {
				case FIXED_CLASSIC -> methods.client.getWidget(GlobalWidgetInfo.FIXED_CLASSIC_COMPASS.getPackedId());
				case RESIZABLE_MODERN -> methods.client.getWidget(GlobalWidgetInfo.RESIZABLE_MODERN_COMPASS.getPackedId());
				case RESIZABLE_CLASSIC -> methods.client.getWidget(GlobalWidgetInfo.RESIZABLE_CLASSIC_COMPASS.getPackedId());
				default -> throw new IllegalStateException("Unexpected value: " + layout);
			}
		}
		return null;
	}

	/**
	 * @return The minimaps <code>Widget</code>; otherwise null.
	 */
	public synchronized Widget getMinimapInterface() {
		ViewportLayout layout = getViewportLayout();
		if (layout != null) {
			switch (layout) {
				case FIXED_CLASSIC -> methods.client.getWidget(GlobalWidgetInfo.FIXED_CLASSIC_MINIMAP.getPackedId());
				case RESIZABLE_MODERN -> methods.client.getWidget(GlobalWidgetInfo.RESIZABLE_MODERN_MINIMAP.getPackedId());
				case RESIZABLE_CLASSIC -> methods.client.getWidget(GlobalWidgetInfo.RESIZABLE_CLASSIC_MINIMAP.getPackedId());
				default -> throw new IllegalStateException("Unexpected value: " + layout);
			}
		}
		return null;
	}

	/**
	 * @param interfaceTab The enumerated tab containing WidgetInfo of the tab.
	 * @return The specified tab <code>Widget</code>; otherwise null.
	 */
	public synchronized Widget getTab(final InterfaceTab interfaceTab) {
		ViewportLayout layout = getViewportLayout();
		if (layout != null) {
			switch (layout) {
				case FIXED_CLASSIC -> interfaceTab.getFixedClassicWidget();
				case RESIZABLE_MODERN -> interfaceTab.getResizableModernWidget();
				case RESIZABLE_CLASSIC -> interfaceTab.getResizableClassicWidget();
				default -> throw new IllegalStateException("Unexpected value: " + layout);
			}
		}
		return null;
	}

	/**
	 * Determines client viewport layout mode.
	 *
	 * @return <code>ViewportLayout</code>; otherwise <code>null</code>.
	 */
	public ViewportLayout getViewportLayout() {
		Widget minimapOnFixedClassic =
				methods.client.getWidget(GlobalWidgetInfo.RESIZABLE_CLASSIC_MINIMAP.getPackedId());
		Widget minimapOnResizableClassic =
				methods.client.getWidget(GlobalWidgetInfo.RESIZABLE_MODERN_MINIMAP.getPackedId());
		Widget minimapOnResizableModern =
				methods.client.getWidget(GlobalWidgetInfo.FIXED_CLASSIC_MINIMAP.getPackedId());
		if (minimapOnFixedClassic != null)
			return ViewportLayout.FIXED_CLASSIC;
		else if (minimapOnResizableClassic != null)
		    return ViewportLayout.RESIZABLE_CLASSIC;
		else if (minimapOnResizableModern != null)
			return ViewportLayout.RESIZABLE_MODERN;
		return null;
	}
}