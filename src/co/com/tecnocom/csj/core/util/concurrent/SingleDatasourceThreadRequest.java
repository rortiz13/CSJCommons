package co.com.tecnocom.csj.core.util.concurrent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import co.com.tecnocom.csj.core.util.properties.Conexion;

import com.liferay.portal.kernel.util.StringPool;

public class SingleDatasourceThreadRequest extends Thread {

	private String datasourceKey;
	private List<String> ids;
	private String query;
	private Date startDate;
	private Date endDate;
//	private static final SimpleDateFormat sqlServerTimestampFormatStart = new SimpleDateFormat("yyyy-MM-dd 00:00:00:000");
//	private static final SimpleDateFormat sqlServerTimestampFormatEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59:999");
	private static final SimpleDateFormat sqlServerDateFormat = new SimpleDateFormat("yyyyMMdd");
	private MultiDatasourceRequestInvoker invoker;
	
	public SingleDatasourceThreadRequest(String datasourceKey, List<String> ids, String query, Date startDate, Date endDate) {
		this.datasourceKey = datasourceKey;
		this.ids = ids;
		this.query = query;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@Override
	public void run(){
		if(invoker == null){
			throw new NullPointerException("el observador es nulo: no puede iniciarse el hilo hasta que se halla seteado un observer apropiado");
		}
		List<String[]> resultToSubmit = null;
		System.out.println("inicio query al datasource: " + datasourceKey);
		String executableQuery = formatProcessQuery(ids, startDate,endDate, query);
		try {
			resultToSubmit = querySingleDatasource(executableQuery, datasourceKey);
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		} finally {
			invoker.notifyCompletion(resultToSubmit);
		}
	}
	
	public SingleDatasourceThreadRequest setObserver(MultiDatasourceRequestInvoker invoker){
		this.invoker = invoker;
		return this;
	}
	
	protected List<String[]> querySingleDatasource(String executableQuery, String datasourceKey) throws SQLException{
		List<String[]> resultLines;
		try {
			resultLines = queryActions(executableQuery, datasourceKey);
		} catch (SQLException e) {
			System.out.println("executed query = "  + executableQuery);
			throw e;
		}
		return resultLines;
	}
	
	private String formatProcessQuery(List<String> processIds, Date startDate, Date endDate, String query){
		String finalQuery = null;
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for(String processId: processIds){
			builder.append("'").append(processId).append("', ");
		}
		String inValues = builder.toString().replaceFirst(",\\s$", ") order by num_proc");
		finalQuery = String.format(query, sqlServerDateFormat.format(startDate), sqlServerDateFormat.format(endDate), sqlServerDateFormat.format(startDate), sqlServerDateFormat.format(endDate)) + inValues;
		return finalQuery;
	}
	
//	Original
//	private List<String[]> queryActions(String query, String dsKey) throws SQLException{
//		Connection connection = DatabaseConnection.INSTANCE.getConnection(dsKey);
//		Statement statement = DatabaseConnection.INSTANCE.getStatement(connection);
//		ResultSet rs = null;
//		System.out.println("query = " + query);
//		rs = DatabaseConnection.INSTANCE.executeQuery(statement, query);
//
//		LinkedList<String[]> lines = new LinkedList<String[]>();
//		try {
//			while(rs.next()){
//				String num_proc = rs.getString(1);
//				String actor = rs.getString(2);
//				Date fecha_actuacion = rs.getDate(3);
//				String tipo_actuacion = rs.getString(4);
//				String descripcion_actuacion = rs.getString(5);
//				Date fecha_inicio_terminos = rs.getDate(6);
//				Date fecha_fin_terminos = rs.getDate(7);
//				String[] line = new String[]{num_proc, actor, fecha_actuacion == null ? "NULL" : fecha_actuacion.toString(), tipo_actuacion, descripcion_actuacion, fecha_inicio_terminos == null ? "NULL" : fecha_inicio_terminos.toString(), fecha_fin_terminos == null ? "NULL" : fecha_fin_terminos.toString()};
////				System.out.println("line = " + Arrays.toString(line));
//				lines.add(line);
//			}
//			System.out.println(lines.size());
//			return lines;
//		} catch (SQLException e) {
//			throw e;
//		} finally{
//			DatabaseConnection.INSTANCE.close(connection, statement, rs);
//		}
//	}select IPBaseDatos,NombreBaseDatos,UsuariobaseDatos,ContraseñaBaseDatos from basesdatosCSJ WHERE idConexion='79'
	
	public String getIdConexion(String ciudad, String entidad, String especialidad) {
		try {

			System.out.println("llego "+ciudad+entidad+especialidad);
			String idConexion="";
			String querie="select idConexion from T081BRESPEENTI WHERE A081CODICIUD='"+ciudad+"' and A081CODIENTI='"+entidad+"' and A081CODIESPE='"+especialidad+"' ";
			Connection conection;
			conection = Conexion.open(Conexion.DATA_SOURCE_PRINCIPAL);
			
			Statement statement = conection.createStatement();
			ResultSet resultSet = statement.executeQuery(querie);
			if(resultSet != null){
				resultSet.next();					
				idConexion = resultSet.getString("idConexion");
			}
			System.out.println("mando "+idConexion);
			conection.close();
			return idConexion;
		} catch (ClassNotFoundException e) {
			System.out.println("error getIdConexion"+e);
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("error getIdConexion"+e);
			e.printStackTrace();
		}
		return null;
	}
	
	public Connection getConexion(String dsKey) throws SQLException, ClassNotFoundException {
			
		StringTokenizer st = new StringTokenizer(dsKey, "_");
		String idConexion=getIdConexion(st.nextToken(), st.nextToken(), st.nextToken());
		
		String ip="",database="",user="",pass="";
		String querie="select IPBaseDatos,NombreBaseDatos,UsuariobaseDatos,ContraseñaBaseDatos from basesdatosCSJ WHERE idConexion='"+idConexion+"'";
		Connection conection;
		conection = Conexion.open(Conexion.DATA_SOURCE_PRINCIPAL);
		System.out.println(querie);
		Statement statement = conection.createStatement();
		ResultSet resultSet = statement.executeQuery(querie);
		if(resultSet != null){
			resultSet.next();					
			ip = resultSet.getString("IPBaseDatos");
			database = resultSet.getString("NombreBaseDatos");
			user = resultSet.getString("UsuariobaseDatos");
			pass = resultSet.getString("ContraseñaBaseDatos");
		}
		conection.close();
		Connection conexion;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(ip.contains(",")){
			ip.replaceAll(",", ":");
			
		}
//		conexion = DriverManager.getConnection("jdbc:sqlserver://172.16.2.19:1433;databaseName=Consejo;user=webce;password=webce;");
		String url="jdbc:sqlserver://"+ip+";databaseName="+database+";user="+user+";password="+pass+";";
		System.out.println(url);
		conexion = DriverManager.getConnection(url);

		return conexion;
	}
	
	private List<String[]> queryActions(String query, String dsKey) throws SQLException{
		
		try{
			
		Connection connection = getConexion(dsKey);
		Statement statement = connection.createStatement();
		ResultSet rs = null;
		System.out.println( query);
	//	query="select distinct processInfo.A103LLAVPROC as num_proc, sujeProc.A112NOMBSUJE as actor,  processInfo.A103DESCACTD as ActuacionDespacho, processInfo.A103FECHDESD as FechaActuacionDespacho,  processInfo.A103DESCACTS as ActuacionSecretaria, processInfo.A103FECHDESS as FechaActuacionSecretaria,  processInfo.A103FECHINIS as FechaInicioTerminos, processInfo.A103FECHFINS as FechaFinTerminos   from dbo.T103DAINFOPROC as processInfo, dbo.T112DRSUJEPROC as sujeProc, dbo.T110DRACTUPROC as actuProc   where  sujeProc.A112LLAVPROC = processInfo.A103LLAVPROC  and processInfo.A103LLAVPROC = actuProc.A110LLAVPROC  and processInfo.A103LLAVPROC in ('25000232600020100091101', '19001233100020030078500', '05001233100020090114001', '50001231500020010026201', '73001233100020110007301', '76001233100020080093401', '17001233100020090037401') order by num_proc";
		rs = statement.executeQuery(query);
		

		LinkedList<String[]> lines = new LinkedList<String[]>();
		try {
			if(rs != null){
				
				while(rs.next()){
					System.out.println("entro en panama");
					String num_proc = rs.getString("num_proc");
					String actor = rs.getString("actor");
					String actuacionDespacho = rs.getString("ActuacionDespacho");
					Date fechaActuacionDespacho = rs.getDate("FechaActuacionDespacho");
					String actuacionSecretaria = rs.getString("ActuacionSecretaria");
					Date fechaActuacionSecretaria = rs.getDate("FechaActuacionSecretaria");
					Date fechaInicioTerminos = rs.getDate("FechaInicioTerminos");
					Date fechaFinTerminos = rs.getDate("FechaFinTerminos");
					
					String[] line = new String[]{num_proc, actor, actuacionDespacho, fechaActuacionDespacho == null ? StringPool.BLANK : fechaActuacionDespacho.toString(), actuacionSecretaria, fechaActuacionSecretaria == null ? StringPool.BLANK : fechaActuacionSecretaria.toString(), fechaInicioTerminos == null ? StringPool.BLANK : fechaInicioTerminos.toString(), fechaFinTerminos == null ? StringPool.BLANK : fechaFinTerminos.toString()};
	//				System.out.println("line = " + Arrays.toString(line));
					lines.add(line);
				}
			}else{
				System.out.println(" vacia");
			}
			System.out.println("panama respuesta "+lines.size());
			return lines;
		} catch (SQLException e) {
			throw e;
		} finally{
			connection.close();
		}
		}catch (Exception e){
			System.out.println("error JCRM   "+e);
			return null;
		}
	}
}
