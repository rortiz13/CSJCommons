package co.com.tecnocom.csj.core.util.properties;

import java.io.IOException;
import java.util.Properties;

public enum DatasourceProperties {
	INSTANCE;
	
	private Properties properties;
	
	private DatasourceProperties() {
		try {
			properties = new Properties();
			properties.load(DatasourceProperties.class
					.getResourceAsStream("/co/com/tecnocom/csj/core/util/properties/datasources.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getLiferayDS() {
		return properties.getProperty("liferay");
	}
	
	public String getPQRDS(){
		return properties.getProperty("pqr");
	}
	
	public String getConsejoDS(){
		return properties.getProperty("consejo");
	}
	
	public String getRamaJudicialDS(){
		return properties.getProperty("lportal_ramajudicial_datasource");
	}
	
	public String getRamaJudicialDatabase() {
		return properties.getProperty("lportal_ramajudicial_database");
	}
	
	public String getMaxIntervalDaysForWSQuery(){
		return properties.getProperty("max_interval_days_for_ws_query");
	}
	
	public String getLocationDS(){
		return properties.getProperty("location_datasource");
	}
	
	public String getLocationDatabase(){
		return properties.getProperty("location_database");
	}

	public String getProcessQueriesDS() {
		return properties.getProperty("processQueries_datasource");
	}
	
	public String getProcessQueriesDatabase() {
		return properties.getProperty("processQueries_database");
	}
	
	public String getProcessQueriesDS(String code) {
		return properties.getProperty("processQueries_datasource_" + code);
	}
}
