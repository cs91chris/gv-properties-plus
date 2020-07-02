
package it.greenvulcano.gvesb.util.extras.placeholders.handlers;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import it.greenvulcano.util.metadata.PropertiesHandlerException;
import it.greenvulcano.util.txt.TextUtils;

public class JsonPathHandler extends BasePropertiesHandler {
	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JsonPathHandler.class);
	
	private String json = null;
	private String expression = null;
	private static Configuration conf = null;
	
	static {
		managedTypes.add("jsonpath");
		Collections.unmodifiableList(managedTypes);
		
		Configuration.setDefaults(new Configuration.Defaults() {
			private final JsonProvider jsonProvider = new JacksonJsonProvider();
			private final MappingProvider mappingProvider = new JacksonMappingProvider();
			
			@Override
			public JsonProvider jsonProvider() {
				return jsonProvider;
			}
			
			@Override
			public MappingProvider mappingProvider() {
				return mappingProvider;
			}
			
			@Override
			public Set<Option> options() {
				return EnumSet.of(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.ALWAYS_RETURN_LIST,
						Option.SUPPRESS_EXCEPTIONS);
			}
		});
		
		conf = Configuration.defaultConfiguration();
	}
	
	@Override
	protected void resolve(String str) throws PropertiesHandlerException {
		try {
			List<String> tokens = TextUtils.splitByStringSeparator(str, separator);
			expression = tokens.get(0);
			json = tokens.get(1);
		}
		catch (IndexOutOfBoundsException e) {
			throw new PropertiesHandlerException(this.getClass().getName() + "invalid value: [" + str + "]");
		}
		logger.debug(this.getClass().getName() + "resolved: expression=" + expression + " json=" + json);
	}
	
	@Override
	protected String perform(String type) throws PropertiesHandlerException {
		logger.debug(this.getClass().getName() + "executing: type=" + type);
		
		List<?> resp = JsonPath.read(conf.jsonProvider().parse(json), expression);
		
		if (resp.size() == 1) {
			return resp.get(0).toString();
		}
		else {
			JSONArray respJson = new JSONArray(resp);
			return respJson.toString();
		}
	}
}
