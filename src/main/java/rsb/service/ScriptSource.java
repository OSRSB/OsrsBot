package rsb.service;

import rsb.script.Script;

import java.util.List;

/**
 * @author GigiaJ
 */
public interface ScriptSource {

	List<ScriptDefinition> list();

	Script load(ScriptDefinition def) throws ServiceException;

}
