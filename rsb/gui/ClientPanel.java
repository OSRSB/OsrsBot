
package net.runelite.client.rsb.gui;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.annotation.Nullable;
import javax.swing.JPanel;
import net.runelite.api.Client;
import net.runelite.api.Constants;

final class ClientPanel extends JPanel
{
	public ClientPanel(@Nullable Applet client)
	{
		setSize(Constants.GAME_FIXED_SIZE);
		setMinimumSize(Constants.GAME_FIXED_SIZE);
		setPreferredSize(Constants.GAME_FIXED_SIZE);
		setLayout(new BorderLayout());
		setBackground(Color.black);

		if (client == null)
		{
			return;
		}

		client.setLayout(null);
		client.setSize(Constants.GAME_FIXED_SIZE);

		client.init();
		client.start();

		add(client, BorderLayout.CENTER);

		// This causes the whole game frame to be redrawn each frame instead
		// of only the viewport, so we can hook to MainBufferProvider#draw
		// and draw anywhere without it leaving artifacts
		if (client instanceof Client)
		{
			((Client)client).setGameDrawingMode(2);
		}
	}
}