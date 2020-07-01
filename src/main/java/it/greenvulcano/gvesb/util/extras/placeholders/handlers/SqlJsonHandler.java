
package it.greenvulcano.gvesb.util.extras.placeholders.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import it.greenvulcano.gvesb.j2ee.db.GVDBException;
import it.greenvulcano.gvesb.j2ee.db.connections.JDBCConnectionBuilder;
import it.greenvulcano.gvesb.util.extras.placeholders.BasePropertiesHandler;
import it.greenvulcano.util.metadata.PropertiesHandlerException;

public class SqlJsonHandler extends BasePropertiesHandler {
	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SqlJsonHandler.class);
	
	String connName = "";
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	String sqlStatement = null;
	
	static {
		managedTypes.add("sqljson");
		Collections.unmodifiableList(managedTypes);
	}
	
	@Override
	protected void resolve(String str) throws PropertiesHandlerException {
		int pIdx = str.indexOf(separator);
		if (pIdx != -1) {
			connName = str.substring(0, pIdx);
			sqlStatement = str.substring(pIdx + 2);
		}
		else {
			throw new PropertiesHandlerException("Connection not defined");
		}
		
		try {
			conn = JDBCConnectionBuilder.getConnection(connName);
		}
		catch (GVDBException e) {
			throw new PropertiesHandlerException(e);
		}
	}
	
	@Override
	protected String perform(String type) throws PropertiesHandlerException {
		logger.debug("Executing SQL statement {" + sqlStatement + "} on connection [" + connName + "]");
		
		try {
			ps = conn.prepareStatement(sqlStatement);
			rs = ps.executeQuery();
			JSONArray json = new JSONArray();
			ResultSetMetaData metadata = rs.getMetaData();
			int numColumns = metadata.getColumnCount();
			
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				for (int i = 1; i <= numColumns; ++i) {
					String column_name = metadata.getColumnName(i);
					obj.put(column_name, rs.getObject(column_name));
				}
				json.put(obj);
			}
			return json.length() == 1 ? json.get(0).toString() : json.toString();
		}
		catch (SQLException e) {
			throw new PropertiesHandlerException(e);
		}
	}
	
	@Override
	protected void cleanUp() {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				logger.warn(e.getMessage());
			}
		}
		
		if (ps != null) {
			try {
				ps.close();
			}
			catch (SQLException e) {
				logger.warn(e.getMessage());
			}
		}
		
		if (conn != null) {
			try {
				JDBCConnectionBuilder.releaseConnection(connName, conn);
			}
			catch (GVDBException e) {
				logger.warn(e.getMessage());
			}
		}
	}
}
