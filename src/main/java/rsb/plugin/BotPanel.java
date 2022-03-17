/*
 * Copyright (c) 2018, SomeoneWithAnInternetConnection
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package rsb.plugin;

import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import rsb.internal.globval.GlobalConfiguration;
import rsb.plugin.base.Accordion;
import rsb.plugin.base.DebugPanel;
import rsb.plugin.base.GeneralPanel;
import rsb.plugin.base.BotViewPanel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ScheduledExecutorService;

class BotPanel extends PluginPanel
{
	private final JPanel display = new JPanel();
	private final MaterialTabGroup tabGroup = new MaterialTabGroup(display);


	@Inject
	private BotPanel(ClientThread clientThread, ItemManager itemManager, ScheduledExecutorService executor)
	{
		super(false);
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARK_GRAY_COLOR);
	}

	/**
	 * Associates the bot plugin panel with the script panel. Since the Bot Plugin has access to the injector the instance
	 * of RuneLite must be passed forward through the scriptPanel constructor for script selection to work.
	 *
	 * @param accountPanel	The account panel to associate with the bot plugin panel.
	 * @param scriptPanel	The script panel to associate with the bot plugin panel.
	 */
	protected void associateBot(AccountPanel accountPanel, ScriptPanel scriptPanel) {


		Accordion accordion = new Accordion();

		JPanel generalPanel = GeneralPanel.getInstance();
		JPanel debugPanel = new DebugPanel();
		JPanel botViewPanel = new BotViewPanel();

		accordion.addBar("General", generalPanel);
		accordion.addBar("Debug", debugPanel);
		accordion.addBar("Bot View", botViewPanel);

		MaterialTab baseTab = new MaterialTab("Settings", tabGroup, accordion);
		MaterialTab  accountTab = new MaterialTab("Accounts", tabGroup, accountPanel);
		JScrollPane botPanelScrollPane = new JScrollPane(scriptPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		MaterialTab scriptTab = new MaterialTab("Scripts", tabGroup, botPanelScrollPane);


		tabGroup.setBorder(new EmptyBorder(5, 0, 0, 0));
		tabGroup.addTab(baseTab);
		tabGroup.addTab(accountTab);
		tabGroup.addTab(scriptTab);
		tabGroup.select(baseTab);

		add(tabGroup, BorderLayout.NORTH);
		add(display, BorderLayout.CENTER);
	}

}