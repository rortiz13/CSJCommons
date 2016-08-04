package co.com.tecnocom.csj.core.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.com.tecnocom.csj.core.pool.DatabaseConnection;
import co.com.tecnocom.csj.core.util.dto.Department;
import co.com.tecnocom.csj.core.util.dto.PQR;
import co.com.tecnocom.csj.core.util.dto.PQRRecord;
import co.com.tecnocom.csj.core.util.dto.PQRStatus;
import co.com.tecnocom.csj.core.util.dto.PQRType;
import co.com.tecnocom.csj.core.util.properties.DatasourceProperties;

public enum PQRDataUtil {
    INSTANCE;

    private final String database = DatasourceProperties.INSTANCE.getRamaJudicialDatabase();

    private final String defaultStatus = "Recibido";
    private final String finalStatus = "Finalizado";

    private final String sqlPQRStatuses = "select id, name from " + database + "pqr_status order by id";

    private final String sqlLastStatus = "select top 1 fk_status, comment, updated_time from " + database
	    + "pqr_status_history where fk_pqr = ? order by updated_time desc";
    private final String sqlPQRDateById = "SELECT p.pqr_date FROM " + database + "pqr as p where p.id = ?";

    private final String sqlAllPQRs = "SELECT p.id,p.fk_document_type as dt,p.document_number as dn,p.name as n,p.phone as ph,p.email as em,p.address as ad,p.fk_city as ci,p.fk_pqr_type as pt,p.comment as com,p.filename as fn,p.pqr_date as pd,p.liferay_userid_responsible as lif FROM "
	    + database + "pqr as p" + /* modificación filtro por scopeGroupId*/" where p.scope_group_id = ";
    private final String sqlPQRsByResponsible = "SELECT p.id,p.fk_document_type as dt,p.document_number as dn,p.name as n,p.phone as ph,p.email as em,p.address as ad,p.fk_city as ci,p.fk_pqr_type as pt,p.comment as com,p.filename as fn,p.pqr_date as pd,p.liferay_userid_responsible as lif FROM "
	    + database + "pqr as p where liferay_userid_responsible = ?" + " and scope_group_id = ";

    private final String sqlCurrentStatus = "select id, fk_status, fk_pqr, comment, updated_time, liferay_user_id from " + database
	    + "pqr_status_history as sh order by updated_time asc";
    private final String sqlCurrentStatusByPqrs = "select id, fk_status, fk_pqr, comment, updated_time, liferay_user_id from " + database
	    + "pqr_status_history as sh where (%s) order by updated_time asc";

    private final String sqlPQRTypes = "select id, name from " + database + "pqr_type";
    private final String pqrInsert_withFilename = "insert into "
	    + database
	    + "pqr (fk_document_type, document_number, name, phone, email, address, fk_city, fk_pqr_type, comment, filename, pqr_date, scope_group_id) values (?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String pqrInsert = "insert into " + database
	    + "pqr (fk_document_type, document_number, name, phone, email, address, fk_city, fk_pqr_type, comment, pqr_date, scope_group_id) values (?,?,?,?,?,?,?,?,?,?,?)";

    private final String pqrHistoryInsert = "insert into " + database
	    + "pqr_status_history (fk_status, fk_pqr, comment, updated_time, liferay_user_id) values (?,?,?,?,?)";
    private final String pqrUpdateResponsible = "update " + database + "pqr set liferay_userid_responsible = ? where id = ?";

    private List<PQRType> pqrTypes;
    private List<PQRStatus> pqrStatuses;

    public boolean isFinalStatus(String status) {
	return finalStatus.equalsIgnoreCase(status);
    }

    public PQRStatus getPQRStatusByName(String name) {
	for (PQRStatus pqrStatus : getPQRStatuses()) {
	    if (pqrStatus.getName().equalsIgnoreCase(name)) {
		return pqrStatus;
	    }
	}

	return null;
    }

    public List<PQRStatus> getPQRStatuses() {
	if (pqrStatuses == null) {
	    Connection connection = null;
	    Statement ps = null;
	    ResultSet rs = null;
	    try {

		connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getPQRDS());
		ps = DatabaseConnection.INSTANCE.getStatement(connection);
		rs = DatabaseConnection.INSTANCE.executeQuery(ps, sqlPQRStatuses);

		pqrStatuses = Converter.INSTANCE.convertPqrStatuses(rs);

	    } finally {
		DatabaseConnection.INSTANCE.close(connection, ps, rs);
	    }
	}

	return pqrStatuses;
    }

    public List<PQRType> getPQRTypes() {
	if (pqrTypes == null) {
	    Connection connection = null;
	    Statement pqrTypesStatement = null;
	    ResultSet rs = null;
	    try {

		connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getPQRDS());
		pqrTypesStatement = DatabaseConnection.INSTANCE.getStatement(connection);
		rs = DatabaseConnection.INSTANCE.executeQuery(pqrTypesStatement, sqlPQRTypes);

		pqrTypes = Converter.INSTANCE.convertPQRTypes(rs);

	    } finally {
		DatabaseConnection.INSTANCE.close(connection, pqrTypesStatement, rs);
	    }
	}

	return pqrTypes;
    }

    public List<PQR> getPQRs(Long liferayUser, long scopeGroupId) {
	List<PQR> pqrs = new LinkedList<PQR>();

	Connection connection = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	    connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getPQRDS());
	    if (liferayUser == null) {
		ps = DatabaseConnection.INSTANCE.getPreparedStatement(connection, sqlAllPQRs + scopeGroupId, false);
	    } else {
		ps = DatabaseConnection.INSTANCE.getPreparedStatement(connection, sqlPQRsByResponsible + scopeGroupId, false);
		DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, liferayUser, 1);
	    }

	    rs = DatabaseConnection.INSTANCE.executeQuery(ps);

	    pqrs = Converter.INSTANCE.convertPQRs(rs, getPQRTypes(), CommonDataUtil.INSTANCE.getDocumentTypes(DatasourceProperties.INSTANCE.getPQRDS()),
		    new LinkedList<Department>(CommonDataUtil.INSTANCE.getDepartmentsAndCities(DatasourceProperties.INSTANCE.getPQRDS()).values()));

	    for (PQR pqr : pqrs) {
		Map<Long, List<PQRRecord>> pqrRecords = null;
		if (liferayUser == null) {
		    pqrRecords = getPQRRecords(null);
		} else {
		    pqrRecords = getPQRRecords(pqrs);
		}
		Converter.INSTANCE.setPQRRecords(pqr, pqrRecords, getPQRStatuses(), defaultStatus);
	    }

	} finally {
	    DatabaseConnection.INSTANCE.close(connection, ps, rs);
	}

	return pqrs;
    }

    public Map<Long, List<PQRRecord>> getPQRRecords(List<PQR> pqrs) {
	Map<Long, List<PQRRecord>> records = new LinkedHashMap<Long, List<PQRRecord>>();

	Connection connection = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	    connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getPQRDS());

	    if (pqrs == null) {
		ps = DatabaseConnection.INSTANCE.getPreparedStatement(connection, sqlCurrentStatus, false);
	    } else {
		StringBuilder or = new StringBuilder();
		for (PQR pqr : pqrs) {
		    or.append("fk_pqr=").append(pqr.getId());

		    if (!pqrs.get(pqrs.size() - 1).equals(pqr)) {
			or.append(" or ");
		    }
		}

		ps = DatabaseConnection.INSTANCE.getPreparedStatement(connection, String.format(sqlCurrentStatusByPqrs, or.toString()), false);
	    }

	    rs = DatabaseConnection.INSTANCE.executeQuery(ps);
	    records = Converter.INSTANCE.convertPQRRecords(rs);

	} finally {
	    DatabaseConnection.INSTANCE.close(connection, ps, rs);
	}

	return records;
    }

    public Long insertPQR(int documentTypeId, String documentNumber, String name, String phone, String email, String address, int cityId, int pqrTypeId,
	    String comment, String filename, long scopeGroupId) {
	Connection connection = null;
	PreparedStatement pqrInsertStatement = null;
	ResultSet rs = null;
	try {
	    connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getPQRDS());

	    if (filename == null || filename.trim().isEmpty()) {
		pqrInsertStatement = DatabaseConnection.INSTANCE.getPreparedStatement(connection, pqrInsert, true);
		DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, scopeGroupId, 11);
	    } else {
		pqrInsertStatement = DatabaseConnection.INSTANCE.getPreparedStatement(connection, pqrInsert_withFilename, true);
		DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, scopeGroupId, 12);
	    }

	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, documentTypeId, 1);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, documentNumber, 2);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, name, 3);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, phone, 4);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, email, 5);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, address, 6);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, cityId, 7);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, pqrTypeId, 8);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, comment, 9);
	    

	    if (filename == null || filename.trim().isEmpty()) {
		DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, new Timestamp(System.currentTimeMillis()), 10);
	    } else {
		DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, filename, 10);
		DatabaseConnection.INSTANCE.setPreparedStatementParameter(pqrInsertStatement, new Timestamp(System.currentTimeMillis()), 11);
	    }

	    DatabaseConnection.INSTANCE.executeUpdate(pqrInsertStatement);

	    return DatabaseConnection.INSTANCE.getGeneratedKeys(pqrInsertStatement, rs);
	} finally {
	    DatabaseConnection.INSTANCE.close(connection, pqrInsertStatement, rs);
	}
    }

    public void updatePQRStatus(long statusId, long pqrId, String comment, long liferayUserId, long currentLiferayUserId) {
	Connection connection = null;
	PreparedStatement ps = null;
	try {
	    connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getPQRDS());
	    ps = DatabaseConnection.INSTANCE.getPreparedStatement(connection, pqrHistoryInsert, false);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, statusId, 1);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, pqrId, 2);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, comment, 3);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, new Timestamp(System.currentTimeMillis()), 4);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, currentLiferayUserId, 5);

	    DatabaseConnection.INSTANCE.executeUpdate(ps);

	    updatePQRResponsible(connection, pqrId, liferayUserId);
	} finally {
	    DatabaseConnection.INSTANCE.close(connection, ps, null);
	}
    }

    private Date getPQRDate(long pqrId, Connection connection) {
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	    ps = DatabaseConnection.INSTANCE.getPreparedStatement(connection, sqlPQRDateById, false);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, pqrId, 1);

	    rs = DatabaseConnection.INSTANCE.executeQuery(ps);

	    return Converter.INSTANCE.getPQRDate(rs);
	} finally {
	    DatabaseConnection.INSTANCE.close(null, ps, rs);
	}
    }

    public PQRRecord getLastPQRStatus(long pqrId) {
	Connection connection = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	    connection = DatabaseConnection.INSTANCE.getConnection(DatasourceProperties.INSTANCE.getPQRDS());
	    ps = DatabaseConnection.INSTANCE.getPreparedStatement(connection, sqlLastStatus, false);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, pqrId, 1);

	    rs = DatabaseConnection.INSTANCE.executeQuery(ps);

	    PQRRecord record = Converter.INSTANCE.convertPQRRecord(rs, getPQRStatuses());

	    if (record == null) {
		Date date = getPQRDate(pqrId, connection);

		if (date == null) {
		    return null;
		}

		record = new PQRRecord();
		record.setStatusName(defaultStatus);
		record.setUpdated(date);

		return record;
	    } else {
		return record;
	    }
	} finally {
	    DatabaseConnection.INSTANCE.close(connection, ps, rs);
	}
    }

    private void updatePQRResponsible(Connection connection, long pqrId, long liferayUserId) {
	PreparedStatement ps = null;
	try {
	    ps = DatabaseConnection.INSTANCE.getPreparedStatement(connection, pqrUpdateResponsible, false);

	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, liferayUserId, 1);
	    DatabaseConnection.INSTANCE.setPreparedStatementParameter(ps, pqrId, 2);

	    DatabaseConnection.INSTANCE.executeUpdate(ps);
	} finally {
	    DatabaseConnection.INSTANCE.close(null, ps, null);
	}
    }
}
