
package it.greenvulcano.gvesb.util.extras.placeholders;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import it.greenvulcano.gvesb.util.extras.placeholders.handlers.SqlJsonHandler;
import it.greenvulcano.util.metadata.PropertiesHandler;
import it.greenvulcano.util.metadata.PropertyHandler;

public class Activator implements BundleActivator {	
	private List<PropertyHandler> handlers = null;
	
	public Activator() {
		handlers = new ArrayList<PropertyHandler>();
		handlers.add(new SqlJsonHandler());
	}

	@Override
	public void start(BundleContext context) throws Exception {
		for (PropertyHandler ph : handlers) {
			PropertiesHandler.registerHandler(ph);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		for (PropertyHandler ph : handlers) {
			PropertiesHandler.unregisterHandler(ph);
		}
	}
}
