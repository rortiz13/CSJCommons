package co.com.tecnocom.csj.core.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.com.tecnocom.csj.core.pool.DatabaseConnection;
import co.com.tecnocom.csj.core.util.dto.locationFinder.Department;
import co.com.tecnocom.csj.core.util.dto.locationFinder.DespachoJudicial;
import co.com.tecnocom.csj.core.util.dto.locationFinder.Entity;
import co.com.tecnocom.csj.core.util.dto.locationFinder.Municipality;
import co.com.tecnocom.csj.core.util.dto.locationFinder.Specialty;
import co.com.tecnocom.csj.core.util.properties.DatasourceProperties;

public enum LocationFinderUtil {
	INSTANCE;

	private static List<Department> departments;
	private static List<Municipality> municipalities;
	private static Map<Department, List<Municipality>> departmentMunicipalities;
	private static List<Entity> entities;
	private static List<Specialty> specialties;

	private static final String locationDatabase = DatasourceProperties.INSTANCE.getLocationDatabase();
	private static final String locationDatasource = DatasourceProperties.INSTANCE.getLocationDS();

	/* Queries */
	private static final String sqlDepartments = "select mun.mun_departamento as department_code, dep.dep_nombre as department_name, mun.mun_codigo as municipality_code, mun.mun_nombre as municipality_name" + " FROM " + locationDatabase
			+ ".dbo.glo_municipios as mun, " + locationDatabase + ".dbo.glo_departamentos as dep" + " WHERE mun.mun_departamento = dep.dep_codigo ORDER BY dep.dep_nombre, mun.mun_nombre";
	private static final String sqlEntities = "select ent_codigo, ent_nombre from " + locationDatabase + ".dbo.glo_entidades";
	private static final String sqlSpecialties = "select esp_codigo, esp_nombre from " + locationDatabase + ".dbo.glo_especialidad";

	private Map<Department, List<Municipality>> getDepartmentsMap() {
		if (departmentMunicipalities == null) {
			departments = new LinkedList<Department>();
			municipalities = new LinkedList<Municipality>();
			
			Connection connection = null;
			Statement departmentsStatement = null;
			ResultSet rs = null;
			try {

				connection = DatabaseConnection.INSTANCE.getConnection(locationDatasource);
				departmentsStatement = DatabaseConnection.INSTANCE.getStatement(connection);
				rs = DatabaseConnection.INSTANCE.executeQuery(departmentsStatement, sqlDepartments);

				departmentMunicipalities = Converter.INSTANCE.convertDepartments(rs);
				
				for (Department department : departmentMunicipalities.keySet()) {
					departments.add(department);
					municipalities.addAll(departmentMunicipalities.get(department));
				}
			} finally {
				DatabaseConnection.INSTANCE.close(connection, departmentsStatement, rs);
			}
		}

		return departmentMunicipalities;
	}
	
	public List<Department> getAllDepartments() {
		if(departments == null) {
			getDepartmentsMap();
		}
		
		return departments;
	}
	
	public List<Municipality> getAllMunicipalities() {
		if(municipalities == null) {
			getDepartmentsMap();
		}
		
		Collections.sort(municipalities, new Comparator<Municipality>() {
			@Override
			public int compare(Municipality o1, Municipality o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		
		return municipalities;
	}
	
	public List<Municipality> getMunicipalitiesByDepartment(Long departmentId) {
		Department depto = new Department(departmentId);
		List<Municipality> municipalitiesByDepartment = departmentMunicipalities.get(depto);
		Collections.sort(municipalitiesByDepartment, new Comparator<Municipality>() {
			@Override
			public int compare(Municipality o1, Municipality o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
				
		return municipalitiesByDepartment;
	}
	
	public List<Entity> getAllEntities() {
		if(entities == null) {
			Connection connection = null;
			Statement entitiesStatement = null;
			ResultSet rs = null;
			try {

				connection = DatabaseConnection.INSTANCE.getConnection(locationDatasource);
				entitiesStatement = DatabaseConnection.INSTANCE.getStatement(connection);
				rs = DatabaseConnection.INSTANCE.executeQuery(entitiesStatement, sqlEntities);

				entities = Converter.INSTANCE.convertEntities(rs);
			} finally {
				DatabaseConnection.INSTANCE.close(connection, entitiesStatement, rs);
			}
		}
		
		return entities;
	}
	
	public List<Specialty> getAllSpecialties() {
		if(specialties == null) {
			Connection connection = null;
			Statement specialtiesStatement = null;
			ResultSet rs = null;
			try {

				connection = DatabaseConnection.INSTANCE.getConnection(locationDatasource);
				specialtiesStatement = DatabaseConnection.INSTANCE.getStatement(connection);
				rs = DatabaseConnection.INSTANCE.executeQuery(specialtiesStatement, sqlSpecialties);

				specialties = Converter.INSTANCE.convertSpecialties(rs);
			} finally {
				DatabaseConnection.INSTANCE.close(connection, specialtiesStatement, rs);
			}
		}
		
		return specialties;
	}
	
	public List<DespachoJudicial> buscarDespachos(Long departmentKey, Long municipalityKey, Long entityKey) {
		
		List<DespachoJudicial> despachos = new LinkedList<DespachoJudicial>();
		
		Connection connection = null;
		Statement despachosStatement = null;
		ResultSet rs = null;
		try {
			StringBuilder sqlDespachos = new StringBuilder("");
			sqlDespachos.append("SELECT * FROM ").append(locationDatabase).append(".dbo.adm_despachos AS despacho ");
			if(departmentKey > -1 || municipalityKey > -1 || entityKey > -1) {
				sqlDespachos.append(" WHERE ");
			}
			
			if(departmentKey > -1) {
				sqlDespachos.append(" despacho.des_departamento = ").append(departmentKey).append(" ");
			}
			
			if(municipalityKey > -1) {
				if(departmentKey > -1) {
					sqlDespachos.append(" AND ");
				}
				sqlDespachos.append(" despacho.des_municipio = ").append(municipalityKey).append(" ");
			}
			
			if(entityKey > -1) {
				if(departmentKey > -1 || municipalityKey > -1) {
					sqlDespachos.append(" AND ");
				}
				sqlDespachos.append(" despacho.des_entidad = ").append(entityKey).append(" ");
			}
			
			System.out.println("Query despachos : " + sqlDespachos.toString());

			connection = DatabaseConnection.INSTANCE.getConnection(locationDatasource);
			despachosStatement = DatabaseConnection.INSTANCE.getStatement(connection);
			rs = DatabaseConnection.INSTANCE.executeQuery(despachosStatement, sqlDespachos.toString());

			despachos = Converter.INSTANCE.convertDespachos(rs);
			
			/* Asignar Nombres a los datos adicionales de Despachos */
			setMissingNames(despachos);
		} finally {
			DatabaseConnection.INSTANCE.close(connection, despachosStatement, rs);
		}
		
		return despachos;
	}
	
	private void setMissingNames(List<DespachoJudicial> despachos) {
		initLists();
		
		for (DespachoJudicial despachoJudicial : despachos) {
			if(despachoJudicial.getDepartment() != null) {
				for(Department depto : departments) {
					if(depto.equals(despachoJudicial.getDepartment())) {
						despachoJudicial.setDepartment(depto);
						break;
					}
				}
			}
			
			if(despachoJudicial.getMunicipality() != null) {
				for(Municipality mun : municipalities) {
					if(mun.equals(despachoJudicial.getMunicipality())) {
						despachoJudicial.setMunicipality(mun);
						break;
					}
				}
			}
			
			if(despachoJudicial.getEntity() != null) {
				for(Entity ent : entities) {
					if(ent.equals(despachoJudicial.getEntity())) {
						despachoJudicial.setEntity(ent);
						break;
					}
				}
			}
			
			if(despachoJudicial.getSpecialty() != null) {
				for(Specialty spe : specialties) {
					if(spe.equals(despachoJudicial.getSpecialty())) {
						despachoJudicial.setSpecialty(spe);
						break;
					}
				}
			}
		}
	}

	private void initLists() {
		if(departments == null || municipalities == null) {
			getDepartmentsMap();
		}
		
		if(entities == null) {
			getAllEntities();
		}
		
		if(specialties == null) {
			getAllSpecialties();
		}
	}
}
