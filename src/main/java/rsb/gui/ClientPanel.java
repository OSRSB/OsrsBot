
package rsb.gui;

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

		add(client, BorderLayout.CENTER);
	}
}