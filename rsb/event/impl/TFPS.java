package net.runelite.client.rsb.event.impl;

import net.runelite.client.rsb.event.listener.TextPaintListener;
import net.runelite.client.rsb.util.StringUtil;

import java.awt.*;

public class TFPS implements TextPaintListener {

	private static final int LEN = 2;

	private final int[] frameCount = new int[TFPS.LEN];

	private int lastIdx = 0;

	public int drawLine(final Graphics render, int idx) {
		final int secTime = (int) (System.currentTimeMillis() / 1000);

		final int prevIdx = (secTime - 1) % TFPS.LEN;
		StringUtil.drawLine(render, idx++, String.format("%2d fps", frameCount[prevIdx]));

		final int curIdx = secTime % TFPS.LEN;
		if (lastIdx != curIdx) {
			lastIdx = curIdx;
			frameCount[curIdx] = 1;
		} else {
			frameCount[curIdx]++;
		}
		return idx;
	}
}
