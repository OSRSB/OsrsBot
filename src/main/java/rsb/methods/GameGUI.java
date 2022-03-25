package rsb.methods;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
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
	 * @return The compasses <code>RSInterface</code>;otherwise null.
	 * 	TODO: fix!
	 */
	public synchronized Widget getCompass() {
		return (isFixed())
				? methods.client.getWidget(WidgetInfo.FIXED_VIEWPORT.getId(), 10)
				: null;
	}

	/**
	 * @return The minimaps <code>RSInterface</code>; otherwise null.
	 * 	TODO: fix!
	 */
	public synchronized Widget getMinimapInterface() {
		return (isFixed())
				? methods.client.getWidget(WidgetInfo.FIXED_VIEWPORT_MINIMAP)
				: methods.client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_WIDGET);
	}

	/**
	 * @param interfaceTab The enumerated tab containing WidgetInfo of the tab.
	 * @return The specified tab <code>RSInterface</code>; otherwise null.
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
	 * Determines whether or no the client is currently in the fixed display
	 * mode.
	 *
	 * @return <code>true</code> if in fixed mode; otherwise <code>false</code>.
	 * 	TODO: fix!
	 */
	@Deprecated
	public boolean isFixed() {
		return true;
	}

	public static ViewportLayout getViewportLayout() {
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


