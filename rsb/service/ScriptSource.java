package net.runelite.client.rsb.service;

import net.runelite.client.rsb.script.Script;

import java.util.List;

/**
 * @author Jacmob
 */
public interface ScriptSource {

	List<ScriptDefinition> list();

	Script load(ScriptDefinition def) throws ServiceException;

}
