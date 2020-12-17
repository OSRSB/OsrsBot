package rsb.internal.wrappers;

import net.runelite.api.Node;

public interface NodeSub extends Node {

	NodeSub getNextSub();

	NodeSub getPrevSub();

}
