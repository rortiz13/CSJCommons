package co.com.tecnocom.csj.core.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import co.com.tecnocom.csj.core.pool.DatabaseConnection;
//import co.com.tecnocom.csj.core.util.dto.AppointmentType;
import co.com.tecnocom.csj.core.util.dto.Department;
import co.com.tecnocom.csj.core.util.dto.DocumentType;
import co.com.tecnocom.csj.core.util.dto.Appointment;
//import co.com.tecnocom.csj.core.util.dto.Location;
import co.com.tecnocom.csj.core.util.properties.DatasourceProperties;

public enum CommonDataUtil {
    INSTANCE;
    
    private static final String dbName = DatasourceProperties.INSTANCE.getRamaJudicialDatabase();
//    private static final String dbLocationName = "Despacho";

    private static final String sqlDepartmentsAndCities = "select d.code as department_code, d.name as department_name, c.code as city_code, c.name as city_name, d.id as department_id, c.id as city_id from " + dbName +/*ramajudicial*/"department as d, " + dbName + /*ramajudicial*/"city as c where c.fk_department = d.id order by department_name, city_name";
//    private final String sqlDepartmentsAndCities = "select d.code as department_code, d.name as department_name, c.code as city_code, c.name as city_name, d.id as department_id, c.id as city_id from lportal_ramajudicial.dbo.department as d, lportal_ramajudicial.dbo.city as c where c.fk_department = d.id order by department_name, city_name";
    private static final String sqlDocumentTypes = "select * from " + dbName /*ramajudicial*/ +"document_type";
//    private final String sqlDocumentTypes = "select * from lportal_ramajudicial.dbo.document_type";
//    private final String sqlAppointmentTypes = "select * from ramajudicial.dbo.appointment_type";
//    private final String sqlAppointmentTypes = "select * from lportal_ramajudicial.dbo.appointment_type";
    
    private final static String sqlAppointments = "select d.code as d_code, d.name as d_name, c.code as c_code, c.name as c_name, a.fk_appointment_type as appointment_type_id, a.start_date as start_date, a.id as id, a.full_name as full_name, a.telephone as telephone, a.email as email, a.comments as comments, a.hours as hours, a.scope_group_id as scope_group_id " + 				// campos
    										"from " + dbName +/*ramajudicial*/"appointment as a, " + /*ramajudicial.dbo.appointment_type as t,*/ dbName +/*ramajudicial*/"city as c, " + dbName + /*ramajudicial*/"department as d " + 	// tablas
//    										"from lportal_ramajudicial.dbo.appointment as a, lportal_ramajudicial.dbo.appointment_type as t, lportal_ramajudicial.dbo.city as c, lportal_ramajudicial.dbo.department as d " + 	// tablas
    										"where a.fk_city = c.id " + /* and a.fk_appointment_type = t.id*/ " and c.fk_department = d.id and a.scope_group_id = %d " +																// joins
    										"order by a.start_date";																															// sort
    
    private final static String sqlAppointment = "select d.code as d_code, d.name as d_name, c.code as c_code, c.name as c_name, a.fk_appointment_type as appointment_type_id, a.start_date as start_date, a.id as id, a.full_name as full_name, a.telephone as telephone, a.email as email, a.comments as comments, a.hours as hours, a.scope_group_id as scope_group_id " + 					// campos
											"from " + dbName +/*ramajudicial*/"appointment as a, " + /*ramajudicial.dbo.appointment_type as t, */ dbName + /*ramajudicial*/"city as c, " + dbName +/*ramajudicial*/"department as d " + 	// tablas
//											"from lportal_ramajudicial.dbo.appointment as a, lportal_ramajudicial.dbo.appointment_type as t, lportal_ramajudicial.dbo.city as c, lportal_ramajudicial.dbo.department as d " + 	// tablas
											"where a.fk_city = c.id and " + /*a.fk_appointment_type = t.id and */"c.fk_department = d.id and a.id = ";// concatenar con el id a consultar				// joins y query
    
    private final static String sqlDeleteAppointment = "delete from " + dbName +/*ramajudicial*/"appointment where id = ";// concatenar con el id a borrar
//    private final static String sqlDeleteAppointment = "delete from lportal_ramajudicial.dbo.appointment where id = ";// concatenar con el id a borrar
    
//    private final static String sqlLocation = "select l.MUN_CODIGO as id, l.MUN_NOMBRE as name, l.MUN_LATITUD as latitude, l.MUN_LONGITUD as longitude, d.DEP_NOMBRE as department_name, d.DEP_CODIGO as department_code " +//   l.address, c.id, c.name, c.code " +
//    										"from " + dbLocationName +/*ramajudicial*/".dbo.GLO_MUNICIPIOS as l, " + dbLocationName + /*ramajudicial*/".dbo.GLO_DEPARTAMENTOS as d " +
////    										"from lportal_ramajudicial.dbo.location as l, lportal_ramajudicial.dbo.city as c " +
//    										"where l.MUN_DEPARTAMENTO = d.DEP_CODIGO and l.MUN_CODIGO = ";// concatenar con el id a consultar
//    
//    private final static String sqlSearchLocations = "select l.MUN_CODIGO as id, l.MUN_NOMBRE as name, l.MUN_LATITUD as latitude, l.MUN_LONGITUD as longitude, d.DEP_NOMBRE as department_name, d.DEP_CODIGO as department_code " +	// campos
//								    		"from " + dbLocationName + ".dbo.GLO_MUNICIPIOS as l, " + dbLocationName + ".dbo.GLO_DEPARTAMENTOS as d " +						// tablas
////								    		"from lportal_ramajudicial.dbo.location as l, lportal_ramajudicial.dbo.city as c " +						// tablas
//								    		"where (l.MUN_DEPARTAMENTO = d.DEP_CODIGO) and (l.MUN_NOMBRE like '%%%s%%' or d.DEP_NOMBRE like '%%%s%%') " +			// condiciones
//								    		"order by l.MUN_NOMBRE, d.DEP_NOMBRE";																	// sort
    
    private static final SimpleDateFormat sqlServerDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Map<Integer, Department> departmentsAndCities;
    private List<DocumentType> documentTypes;
//    private List<AppointmentType> appointmentTypes;

    public Map<Integer, Department> getDepartmentsAndCities(String datasourceJNDIName) {
	if (departmentsAndCities == null) {
	    Connection connection = null;
	    Statement departmentsAndCitiesStatement = null;
	    ResultSet rs = null;
	    try {

		connection = DatabaseConnection.INSTANCE.getConnection(datasourceJNDIName);
		departmentsAndCitiesStatement = DatabaseConnection.INSTANCE.getStatement(connection);
		rs = DatabaseConnection.INSTANCE.executeQuery(departmentsAndCitiesStatement, sqlDepartmentsAndCities);

		departmentsAndCities = Converter.INSTANCE.convertDepartmentsAndCities(rs);

	    } finally {
		DatabaseConnection.INSTANCE.close(connection, departmentsAndCitiesStatement, rs);
	    }
	}

	return departmentsAndCities;
    }

    public List<DocumentType> getDocumentTypes(String datasourceJNDIName) {
	if (documentTypes == null) {
	    Connection connection = null;
	    Statement documentTypesStatement = null;
	    ResultSet rs = null;
	    try {

		connection = DatabaseConnection.INSTANCE.getConnection(datasourceJNDIName);
		documentTypesStatement = DatabaseConnection.INSTANCE.getStatement(connection);
		rs = DatabaseConnection.INSTANCE.executeQuery(documentTypesStatement, sqlDocumentTypes);

		documentTypes = Converter.INSTANCE.convertDocumentTypes(rs);

	    } finally {
		DatabaseConnection.INSTANCE.close(connection, documentTypesStatement, rs);
	    }
	}

	return documentTypes;
    }
    
//    public List<AppointmentType> getAllAppointmentTypes(String datasourceJNDIName) throws SQLException{
//    	if(appointmentTypes == null){
//    		Connection connection = null;
//    		Statement appointmentTypeStatement = null;
//    		ResultSet rs = null;
//    		try{
//	    		connection = DatabaseConnection.INSTANCE.getConnection(datasourceJNDIName);
//	    		appointmentTypeStatement = DatabaseConnection.INSTANCE.getStatement(connection);
//	    		rs = DatabaseConnection.INSTANCE.executeQuery(appointmentTypeStatement, sqlAppointmentTypes);
//	    		appointmentTypes = Converter.INSTANCE.convertToAppointmentTypes(rs);
//    		} catch(SQLException ex){
//    			throw ex;
//    		} finally{
//    			DatabaseConnection.INSTANCE.close(connection, appointmentTypeStatement, rs);
//    		}
//    	}
//    	return appointmentTypes;
//    }
    
    // las citas no se cachean (solo son visibles por el administrador del portal)
    public List<Appointment> getAllAppointments(long scopeGroupId, String datasourceJNDIName) /*throws SQLException*/{
		Connection connection = null;
		Statement appointmentStatement = null;
		ResultSet rs = null;
		try{
    		connection = DatabaseConnection.INSTANCE.getConnection(datasourceJNDIName);
    		appointmentStatement = DatabaseConnection.INSTANCE.getStatement(connection);
    		System.out.println("Appointments query: " + String.format(sqlAppointments, scopeGroupId));
    		rs = DatabaseConnection.INSTANCE.executeQuery(appointmentStatement, String.format(sqlAppointments, scopeGroupId));
    		System.out.println("RS CITAS: " + rs);
        	return Converter.INSTANCE.convertToAppointments(rs);
		}/* catch(SQLException ex){
			throw ex;
		}*/ finally{
			DatabaseConnection.INSTANCE.close(connection, appointmentStatement, rs);
		}
    }
    
	/**
	 * crea directamente un registro en la tabla de appointments recibiendo unicamente 
	 * los valores de los campos que van en la tabla
	 * 
     * @param fullName
     * @param phone
     * @param email
     * @param comments
     * @param appointmentHours
     * @param cityId
     * @param appointmentTypeId
     * @param startDate
     * @throws SQLException
     */
    public void createAppointmentDirectly(String fullName, String phone, String email, String comments, int appointmentHours, int cityId, long appointmentTypeId, Date startDate, long scopeGroupId, String datasourceJNDIName) throws SQLException{
    	String statement = String.format("insert into "+dbName+/*ramajudicial*/"appointment (full_name, telephone, email, comments, hours, fk_city, fk_appointment_type, start_date, scope_group_id) values ('%s', '%s', '%s', '%s', %d, %d, %d, '%s', %d)"
    			, fullName, phone, email, comments, appointmentHours, cityId, appointmentTypeId, sqlServerDateFormat.format(startDate), scopeGroupId);
    	Connection connection = null;
    	Statement insertAppointmentStatement = null;
    	try{
	    	connection = DatabaseConnection.INSTANCE.getConnection(datasourceJNDIName);
	    	insertAppointmentStatement = DatabaseConnection.INSTANCE.getStatement(connection);
	    	insertAppointmentStatement.executeUpdate(statement);
    	}
    	catch (SQLException ex){
    		throw ex;
    	}
    	finally{
    		DatabaseConnection.INSTANCE.close(insertAppointmentStatement);
    		DatabaseConnection.INSTANCE.close(connection);
    	}
    }
    
    public Appointment getAppointmentById(int id, String datasourceJNDIName) throws SQLException{
		Connection connection = null;
		PreparedStatement appointmentStatement = null;
		ResultSet rs = null;
		try{
    		connection = DatabaseConnection.INSTANCE.getConnection(datasourceJNDIName);
    		appointmentStatement = DatabaseConnection.INSTANCE.getPreparedStatement(connection, sqlAppointment + id, false);
    		// solo deberia retornar un resultado
    		appointmentStatement.setMaxRows(1);
    		rs = DatabaseConnection.INSTANCE.executeQuery(appointmentStatement);
    		return Converter.INSTANCE.convertToSingleAppointment(rs);
		} catch(SQLException ex){
			throw ex;
		} finally{
			DatabaseConnection.INSTANCE.close(connection, appointmentStatement, rs);
		}
    }
    
    public void deleteAppointmentById(int id, String datasourceJNDIName) throws SQLException{
    	Connection connection = null;
    	Statement deleteAppointmentStatement = null;
    	try{
	    	connection = DatabaseConnection.INSTANCE.getConnection(datasourceJNDIName);
	    	deleteAppointmentStatement = DatabaseConnection.INSTANCE.getStatement(connection);
	    	deleteAppointmentStatement.executeUpdate(sqlDeleteAppointment + id);
    	}
    	catch (SQLException ex){
    		throw ex;
    	}
    	finally{
    		DatabaseConnection.INSTANCE.close(deleteAppointmentStatement);
    		DatabaseConnection.INSTANCE.close(connection);
    	}
    }
    
//    /**
//     * Ejecuta una busqueda entre las ubicaciones buscando por el nombre de la ubicacion y nombre de la ciudad en la que est�,
//     * retornando todos los resultados ordenados por el nombre de la ubicaci�n y luego el nombre de la ciudad
//     * 
//     * @param searchKeywords
//     * @return
//     * @throws SQLException
//     */
//    public List<Location> searchLocations(String searchKeywords, String datasourceJNDIName) throws SQLException{
//    	Connection connection = null;
//    	Statement searchStatement = null;
//    	ResultSet results = null;
//    	try{
//    		connection = DatabaseConnection.INSTANCE.getConnection(datasourceJNDIName);
//    		searchStatement = DatabaseConnection.INSTANCE.getStatement(connection);
////    		System.out.println("searching locations with query:");
////    		System.out.println(String.format(sqlSearchLocations, searchKeywords, searchKeywords));
//    		results = searchStatement.executeQuery(String.format(sqlSearchLocations, searchKeywords, searchKeywords));
//    		return Converter.INSTANCE.convertToLocations(results);
//    	} catch (SQLException e){
//    		throw e;
//    	} finally{
//    		DatabaseConnection.INSTANCE.close(connection, searchStatement, results);
//    	}
//    }
    
//    public Location findLocationById(int locationId, int departmentCode, String datasourceJNDIName) throws SQLException{
//    	Connection connection = null;
//    	PreparedStatement locationStatement = null;
//    	ResultSet result = null;
//    	try{
//    		connection = DatabaseConnection.INSTANCE.getConnection(datasourceJNDIName);
//    		System.out.println("get single location with query:");
//    		System.out.println(sqlLocation + locationId);
//    		locationStatement = DatabaseConnection.INSTANCE.getPreparedStatement(connection, sqlLocation + locationId + " and d.DEP_CODIGO = " + departmentCode, false);
//    		// solo deberia retornar un resultado
//    		locationStatement.setMaxRows(1);
//    		result = DatabaseConnection.INSTANCE.executeQuery(locationStatement);
//    		return Converter.INSTANCE.convertToSingleLocation(result);
//    	} catch(SQLException e){
//    		throw e;
//    	} finally{
//    		DatabaseConnection.INSTANCE.close(connection, locationStatement, result);
//    	}
//    }
}
