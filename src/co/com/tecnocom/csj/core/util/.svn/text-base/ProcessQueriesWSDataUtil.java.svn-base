package co.com.tecnocom.csj.core.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import co.com.tecnocom.csj.core.pool.DatabaseConnection;
import co.com.tecnocom.csj.core.util.concurrent.MultiDatasourceRequestBuilder;
import co.com.tecnocom.csj.core.util.concurrent.MultiDatasourceRequestInvoker;
import co.com.tecnocom.csj.core.util.properties.DatasourceProperties;

public enum ProcessQueriesWSDataUtil {
	INSTANCE;
	/**
	 * el esquema de base de datos en el que se consultan las actuaciones y
	 * datos relacionados
	 */
	// private static final String localSchemaName =
	// DatasourceProperties.INSTANCE.getRamaJudicialDS();

	// consultas
	private static Long idGrupoRegistroCounter = null;
	// private static final String queryProcessString =
	// "select a.A103LLAVPROC as num_proc, p.A112NOMBSUJE as actor, r.A110FECHDESA as fecha_actuacion, r.A110TIPOACTU as tipo_actuacion, r.A110DESCACTU as descripcion_actuacion, r.A110FECHINIC as fecha_inicio_terminos, r.A110FECHFINA as fecha_fin_terminos "
	// +
	// "from dbo.T103DAINFOPROC as a, dbo.T112DRSUJEPROC as p, dbo.T110DRACTUPROC as r "
	// + "where (a.A103FECHREGI between cast('%s"
	// + /* 2003-15-01 00:00:00:000" */"' as datetime) and cast('%s"
	// + /* 2003-14-12 23:59:59:999
	// */"' as datetime)) and p.A112LLAVPROC = a.A103LLAVPROC and a.A103LLAVPROC = r.A110LLAVPROC "
	// + "and a.A103LLAVPROC in "; // se concatena con los ids de los
	// // procesos para formar una cadena
	// // similar a esta (xxxxxxxxx, xxxxxxxxx
	// // ....)

//	Original
//	private static final String queryProcessString = "select a.A103LLAVPROC as num_proc, p.A112NOMBSUJE as actor, r.A110FECHDESA as fecha_actuacion, r.A110TIPOACTU as tipo_actuacion, r.A110DESCACTU as descripcion_actuacion, r.A110FECHINIC as fecha_inicio_terminos, r.A110FECHFINA as fecha_fin_terminos "
//			+ "from dbo.T103DAINFOPROC as a, dbo.T112DRSUJEPROC as p, dbo.T110DRACTUPROC as r "
//			+ "where (a.A103FECHREGI between '%s' and '%s') and p.A112LLAVPROC = a.A103LLAVPROC and a.A103LLAVPROC = r.A110LLAVPROC "
//			+ "and a.A103LLAVPROC in ";
	
//	Nueva
	private static final String queryProcessString = "select distinct processInfo.A103LLAVPROC as num_proc, sujeProc.A112NOMBSUJE as actor, " +
			" processInfo.A103DESCACTD as ActuacionDespacho, processInfo.A103FECHDESD as FechaActuacionDespacho, " +
			" processInfo.A103DESCACTS as ActuacionSecretaria, processInfo.A103FECHDESS as FechaActuacionSecretaria, " +
			" processInfo.A103FECHINIS as FechaInicioTerminos, processInfo.A103FECHFINS as FechaFinTerminos " +
			" " +
			" from dbo.T103DAINFOPROC as processInfo, dbo.T112DRSUJEPROC as sujeProc, dbo.T110DRACTUPROC as actuProc " +
			" " +
			" where (processInfo.A103FECHDESS between '%s' and '%s' or processInfo.A103FECHDESD between '%s' and '%s') " +
			" and sujeProc.A112LLAVPROC = processInfo.A103LLAVPROC " +
			" and processInfo.A103LLAVPROC = actuProc.A110LLAVPROC " +
			" and processInfo.A103LLAVPROC in ";

	private static final String registerProcessString = "insert into dbo.process_records (idGrupoRegistro, grouper, description, name, num_process, city_code, corporation_code, especiallity_code) values ";
	private static final String insertHelper = "(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s')";
	private static final String selectMaxIdGrupoRegistro = "select max(idGrupoRegistro) as maximo from dbo.process_records";

	// private static final String queryActionsByRecordIdsString =
	// "select a.A110LLAVPROC as num_proc, s.A112NOMBSUJE as actor, a.A110FECHDESA as fecha_actuacion, a.A110TIPOACTU as tipo_actuacion, a.A110DESCACTU as descripcion_actuacion, a.A110FECHINIC as fecha_inicio_terminos, a.A110FECHFINA as fecha_fin_terminos "
	// + // --, a.A110FECHREGI as fecha
	// "FROM dbo.T110DRACTUPROC as a, dbo.T112DRSUJEPROC as s " +
	// "where (a.A110FECHREGI between cast('%s"/* "2003-15-01 00:00:00:000" */
	// + "' as datetime) and cast('%s"/* "2003-14-12 23:59:59:999" */+
	// "' as datetime)) and a.A110LLAVPROC = s.A112LLAVPROC " +
	// "and a.A110LLAVPROC in "; // se
	// // concatena
	// // con
	// // los
	// // ids
	// // de
	// // los
	// // procesos
	// // para
	// // formar
	// // una
	// // cadena
	// // similar
	// // a
	// // esta
	// // (xxxxxxxxx,
	// // xxxxxxxxx
	// // ....)

//	private static final String queryActionsByRecordIdsString = "select a.A110LLAVPROC as num_proc, s.A112NOMBSUJE as actor, a.A110FECHDESA as fecha_actuacion, a.A110TIPOACTU as tipo_actuacion, a.A110DESCACTU as descripcion_actuacion, a.A110FECHINIC as fecha_inicio_terminos, a.A110FECHFINA as fecha_fin_terminos "
//			+ "FROM dbo.T110DRACTUPROC as a, dbo.T112DRSUJEPROC as s " + "where (a.A110FECHREGI between '%s' and '%s') and a.A110LLAVPROC = s.A112LLAVPROC " + "and a.A110LLAVPROC in ";

	private ProcessQueriesWSDataUtil() {
	}

	public List<String[]> queryProcess(Long recordGroupId, String grouper, Date startDate, Date endDate) throws SQLException {
//		HashMap<String, List<String>> groupedProcessIds = getProcessesByRegistroGroupId(recordGroupId);
		HashMap<String, List<String>> groupedProcessIds = getProcessesByRegistroGroupIdAndGrouper(recordGroupId, grouper);
		System.out.println("cantidad de datasources para la consulta: " + groupedProcessIds.size());
		MultiDatasourceRequestBuilder builder = new MultiDatasourceRequestBuilder();
		builder.setGroupedProcessIds(groupedProcessIds);
		builder.setQuery(queryProcessString);
		builder.setEndDate(endDate);
		builder.setStartDate(startDate);
		MultiDatasourceRequestInvoker invoker = builder.build();
		try {
			invoker.startQuery();
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}
		List<String[]> results = invoker.getResults();
		System.out.println(results.size() + " resultados obtenidos!");
		return results;
	}

	public Long registerProcesses(List<String[]> processesArchiveCSV, String description, String grouper) throws SQLException {
		Long idRegistro = getValidIdRegistro();
		StringBuilder builder = new StringBuilder(registerProcessString);
		for (String[] strings : processesArchiveCSV) {

			System.out.println(Arrays.toString(strings));
			String insertValues = String.format(insertHelper, idRegistro, grouper, description, strings[0], strings[1], strings[2], strings[3], strings[4]);
			builder.append(insertValues).append(", ");
		}

		String finalQuery = builder.toString().replaceFirst(",\\s$", "");
		System.out.println("<-------" + finalQuery + "-------->");
		try {
			executeInserts(finalQuery);
		} catch (SQLException e) {
			// si se produce una excepcion se revierte el valor de el id del
			// grupo de registro ya que no se habra usado
			if (idGrupoRegistroCounter > 0L) {
				idGrupoRegistroCounter--;
			} else {
				idGrupoRegistroCounter = null;
			}
			throw e;
		}
		return idRegistro;
	}

	public List<String[]> queryActionsByRecordIdsAndGrouper(Long recordGroupId, Date startDate, Date endDate, String grouper) throws SQLException {
		HashMap<String, List<String>> groupedProcessIds = getProcessesByRegistroGroupIdAndGrouper(recordGroupId, grouper);

		MultiDatasourceRequestBuilder builder = new MultiDatasourceRequestBuilder();
		builder.setGroupedProcessIds(groupedProcessIds);
//		builder.setQuery(queryActionsByRecordIdsString);
		builder.setQuery(queryProcessString);
		builder.setEndDate(endDate);
		builder.setStartDate(startDate);
		MultiDatasourceRequestInvoker invoker = builder.build();
		try {
			invoker.startQuery();
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}
		List<String[]> results = invoker.getResults();
		System.out.println(results.size() + " resultados obtenidos!");
		return results;
	}

	/**
	 * en dos pasos este metodo obtiene los registros necesarios en la tabla de
	 * bases de la base de datos "local" a traves de el recordGroupId y con
	 * estos datos hace una consulta a la base del consejo
	 * 
	 * @param recordGroupId
	 * @param startDate
	 * @param endDate
	 * @param dsKey
	 * @return cada linea del result set como String[]
	 * @throws SQLException
	 */
	public List<String[]> queryActionsByRecordIds(Long recordGroupId, Date startDate, Date endDate) throws SQLException {
		HashMap<String, List<String>> groupedProcessIds = getProcessesByRegistroGroupId(recordGroupId);

		MultiDatasourceRequestBuilder builder = new MultiDatasourceRequestBuilder();
		builder.setGroupedProcessIds(groupedProcessIds);
//		builder.setQuery(queryActionsByRecordIdsString);
		builder.setQuery(queryProcessString);
		builder.setEndDate(endDate);
		builder.setStartDate(startDate);
		MultiDatasourceRequestInvoker invoker = builder.build();
		try {
			invoker.startQuery();
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}
		List<String[]> results = invoker.getResults();
		System.out.println(results.size() + " resultados obtenidos!");

		return results;
	}

	/**
	 * obtiene de la base de datos el maximo "id de grupo de registro" y lo
	 * incrementa en uno con la finalidad para que cada
	 * "id de grupo de registro" sea unico para cada operaci�n.
	 * 
	 * @return el siguiente id de grupo de registro en la secuencia de la base
	 *         de datos
	 * @throws SQLException
	 */
	private Long getValidIdRegistro() throws SQLException {
		if (idGrupoRegistroCounter != null) {
			return ++idGrupoRegistroCounter;
		} else {
			String datasource = DatasourceProperties.INSTANCE.getRamaJudicialDS();
			System.out.println("datasource name = " + datasource);
			Connection connection = DatabaseConnection.INSTANCE.getConnection(datasource);
			System.out.println("connection.toString() = " + connection.toString());
			Statement statement = DatabaseConnection.INSTANCE.getStatement(connection);
			ResultSet rs = DatabaseConnection.INSTANCE.executeQuery(statement, selectMaxIdGrupoRegistro);

			try {
				if (rs.next()) {
					idGrupoRegistroCounter = rs.getLong(1);
					if (rs.wasNull()) {
						idGrupoRegistroCounter = 0L;
						return idGrupoRegistroCounter;
					}
					return ++idGrupoRegistroCounter;
				} else {
					idGrupoRegistroCounter = 0L;
					return idGrupoRegistroCounter;
				}

			} catch (SQLException e) {
				throw e;
			} finally {
				DatabaseConnection.INSTANCE.close(connection, statement, rs);
			}
		}
	}

	/**
	 * ingresa los datos contenidos en el csv de entrada traducido a sentencia
	 * sql incluyendo ademas los datos descripcion y agrupador enviados en el
	 * web service.
	 * 
	 * @param query
	 * @throws SQLException
	 */
	private void executeInserts(String query) throws SQLException {

		Connection connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getRamaJudicialDS());
		Statement insertStatement = DatabaseConnection.INSTANCE.getStatement(connection);
		try {
			insertStatement.executeUpdate(query);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (insertStatement != null) {
				DatabaseConnection.INSTANCE.close(insertStatement);
			}
			if (connection != null) {
				DatabaseConnection.INSTANCE.close(connection);
			}
		}
	}

	/**
	 * dado un "id de grupo de registro" retorna todos los ids de los procesos
	 * que fueron ingresados con el grupo de registro.
	 * 
	 * @param registroGrupoId
	 * @return lista con ids de procesos
	 * @throws SQLException
	 */
	private HashMap<String, List<String>> getProcessesByRegistroGroupId(Long registroGrupoId) throws SQLException {
		Connection connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getRamaJudicialDS());
		Statement statement = DatabaseConnection.INSTANCE.getStatement(connection);
		ResultSet rs = null;
		String sqlQuery = "select r.num_process, r.city_code, r.corporation_code, r.especiallity_code from dbo.process_records as r where idGrupoRegistro = " + registroGrupoId;
		try {
			rs = statement.executeQuery(sqlQuery);
			HashMap<String, List<String>> groupedIds = new HashMap<String, List<String>>();
			while (rs.next()) {
				String num_process = rs.getString(1);
				String city_code = rs.getString(2);
				String corporation_code = rs.getString(3);
				String especiallity_code = rs.getString(4);
				String groupKey = city_code + "_" + corporation_code + "_" + especiallity_code;
				if (groupedIds.containsKey(groupKey)) {
					groupedIds.get(groupKey).add(num_process);
				} else {
					List<String> newList = new LinkedList<String>();
					newList.add(num_process);
					groupedIds.put(groupKey, newList);
				}
			}
			return groupedIds;
		} catch (SQLException e) {
			System.out.println("sql query = " + sqlQuery);
			throw e;
		} finally {
			DatabaseConnection.INSTANCE.close(connection, statement, rs);
		}
	}

	private HashMap<String, List<String>> getProcessesByRegistroGroupIdAndGrouper(Long registroGrupoId, String grouper) throws SQLException {
		Connection connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getRamaJudicialDS());
		Statement statement = DatabaseConnection.INSTANCE.getStatement(connection);
		ResultSet rs = null;
		String sqlQuery = "select r.num_process, r.city_code, r.corporation_code, r.especiallity_code from dbo.process_records as r where r.idGrupoRegistro = " + registroGrupoId + " and r.grouper = '" + grouper + "'";
		try {
			rs = statement.executeQuery(sqlQuery);
			HashMap<String, List<String>> groupedIds = new HashMap<String, List<String>>();
			while (rs.next()) {
				String num_process = rs.getString(1);
				String city_code = rs.getString(2);
				String corporation_code = rs.getString(3);
				String especiallity_code = rs.getString(4);
				String groupKey = city_code + "_" + corporation_code + "_" + especiallity_code;
				if (groupedIds.containsKey(groupKey)) {
					groupedIds.get(groupKey).add(num_process);
				} else {
					List<String> newList = new LinkedList<String>();
					newList.add(num_process);
					groupedIds.put(groupKey, newList);
				}
			}
			return groupedIds;

		} catch (SQLException e) {
			throw e;
		} finally {
			DatabaseConnection.INSTANCE.close(connection, statement, rs);
		}
	}
}
