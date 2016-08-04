package co.com.tecnocom.csj.core.util.dto;

import java.util.LinkedList;
import java.util.List;

public class ProcessData {

	private String processNumber;
	private String processDate;
	private String processCity;
	private String processSpecialty;
	
	//	Adicionales Diana - 02-04-2014
	private String processCorporation;
	private String processPerson; // Ponente
	
	//	Adicionales Diana - 13-05-2014
	private String processClass;
	
	//	Despacho
	private String actuacionDespacho;
	private String anotacionDespacho;
	private String fechaActuacionDespacho;
	
	//	Secretaria
	private String actuacionSecretaria;
	private String anotacionSecretaria;
	private String fechaActuacionSecretaria;
	private String fechaInicialSecretaria;
	private String fechaFinalSecretaria;
	
	//	Demandantes y Demandados
	private List<String> subjectsCode0001; 
	private List<String> subjectsCode0002;
	
	public ProcessData() {
		subjectsCode0001 = new LinkedList<String>();
		subjectsCode0002 = new LinkedList<String>();
	}
	
	public String getProcessNumber() {
		return processNumber;
	}
	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}
	public String getProcessDate() {
		return processDate;
	}
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}
	public String getProcessCity() {
		return processCity;
	}
	public void setProcessCity(String processCity) {
		this.processCity = processCity;
	}
	public String getProcessSpecialty() {
		return processSpecialty;
	}
	public void setProcessSpecialty(String processSpecialty) {
		this.processSpecialty = processSpecialty;
	}
	public String getProcessClass() {
		return processClass;
	}
	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}
	public String getActuacionDespacho() {
		return actuacionDespacho;
	}
	public void setActuacionDespacho(String actuacionDespacho) {
		this.actuacionDespacho = actuacionDespacho;
	}
	public String getAnotacionDespacho() {
		return anotacionDespacho;
	}
	public void setAnotacionDespacho(String anotacionDespacho) {
		this.anotacionDespacho = anotacionDespacho;
	}
	public String getFechaActuacionDespacho() {
		return fechaActuacionDespacho;
	}
	public void setFechaActuacionDespacho(String fechaActuacionDespacho) {
		this.fechaActuacionDespacho = fechaActuacionDespacho;
	}
	public String getActuacionSecretaria() {
		return actuacionSecretaria;
	}
	public void setActuacionSecretaria(String actuacionSecretaria) {
		this.actuacionSecretaria = actuacionSecretaria;
	}
	public String getAnotacionSecretaria() {
		return anotacionSecretaria;
	}
	public void setAnotacionSecretaria(String anotacionSecretaria) {
		this.anotacionSecretaria = anotacionSecretaria;
	}
	public String getFechaActuacionSecretaria() {
		return fechaActuacionSecretaria;
	}
	public void setFechaActuacionSecretaria(String fechaActuacionSecretaria) {
		this.fechaActuacionSecretaria = fechaActuacionSecretaria;
	}
	public String getFechaInicialSecretaria() {
		return fechaInicialSecretaria;
	}
	public void setFechaInicialSecretaria(String fechaInicialSecretaria) {
		this.fechaInicialSecretaria = fechaInicialSecretaria;
	}
	public String getFechaFinalSecretaria() {
		return fechaFinalSecretaria;
	}
	public void setFechaFinalSecretaria(String fechaFinalSecretaria) {
		this.fechaFinalSecretaria = fechaFinalSecretaria;
	}
	public List<String> getSubjectsCode0001() {
		return subjectsCode0001;
	}
	public void setSubjectsCode0001(List<String> subjectsCode0001) {
		this.subjectsCode0001 = subjectsCode0001;
	}
	public List<String> getSubjectsCode0002() {
		return subjectsCode0002;
	}
	public void setSubjectsCode0002(List<String> subjectsCode0002) {
		this.subjectsCode0002 = subjectsCode0002;
	}
	
	public String getProcessCorporation() {
		return processCorporation;
	}

	public void setProcessCorporation(String processCorporation) {
		this.processCorporation = processCorporation;
	}

	public String getProcessPerson() {
		return processPerson;
	}

	public void setProcessPerson(String processPerson) {
		this.processPerson = processPerson;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((processNumber == null) ? 0 : processNumber.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessData other = (ProcessData) obj;
		if (processNumber == null) {
			if (other.processNumber != null)
				return false;
		} else if (!processNumber.equals(other.processNumber))
			return false;
		return true;
	}
	
}
