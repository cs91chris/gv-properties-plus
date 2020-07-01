
package it.greenvulcano.gvesb.util.extras.placeholders;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.greenvulcano.util.metadata.PropertiesHandler;
import it.greenvulcano.util.metadata.PropertiesHandlerException;
import it.greenvulcano.util.metadata.PropertyHandler;

public abstract class BasePropertiesHandler implements PropertyHandler {
	protected static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BasePropertiesHandler.class);
	protected final static List<String> managedTypes = new LinkedList<>();
	protected final static String separator = "::";
	
	@Override
	public List<String> getManagedTypes() {
		return managedTypes;
	}
	
	@Override
	public String expand(String type, String str, Map<String, Object> inProperties, Object object, Object extra)
			throws PropertiesHandlerException {
		try {
			if (!PropertiesHandler.isExpanded(str)) {
				str = PropertiesHandler.expand(str, inProperties, object, extra);
			}
			
			resolve(str);
			String resp = perform(type);
			return (resp != null) ? resp : str;
		}
		catch (Exception exc) {
			if (PropertiesHandler.isExceptionOnErrors()) {
				if (exc instanceof PropertiesHandlerException) {
					throw (PropertiesHandlerException) exc;
				}
				String mess = "Error handling '" + type + "' placeholder with value '" + str + "'";
				throw new PropertiesHandlerException(mess, exc);
			}
			return type + PROP_START + str + PROP_END;
		}
		finally {
			cleanUp();
		}
	}

	protected abstract void resolve(String str) throws PropertiesHandlerException;
	
	protected abstract String perform(String type) throws PropertiesHandlerException;
	
	protected void cleanUp() throws PropertiesHandlerException {
		
	}
}
