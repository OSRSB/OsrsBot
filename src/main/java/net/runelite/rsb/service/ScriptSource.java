package net.runelite.rsb.service;

import net.runelite.rsb.script.Script;

import java.util.List;

/**
 * @author GigiaJ
 */
public interface ScriptSource {

	List<ScriptDefinition> list();

	Script load(ScriptDefinition def) throws ServiceException;

}
