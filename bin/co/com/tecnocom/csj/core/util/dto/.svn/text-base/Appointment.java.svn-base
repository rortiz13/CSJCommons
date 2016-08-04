package co.com.tecnocom.csj.core.util.dto;

import java.util.Date;

public class Appointment {
	
	private int id;
	private Department department;
	private City city;
	private long appointmentTypeId;
	private Date startDate;
	private String fullName;
	private String telephone;
	private String email;
	private String comments;
	private AppointmentConstants appointmentHours;
	private long scopeGroupId;
	
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public long getAppointmentTypeId() {
		return appointmentTypeId;
	}
	public void setAppointmentTypeId(long appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}
	public AppointmentConstants getAppointmentHours() {
		return appointmentHours;
	}
	public void setAppointmentHours(AppointmentConstants appointmentHours) {
		this.appointmentHours = appointmentHours;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((appointmentHours == null) ? 0 : appointmentHours.hashCode());
		result = prime * result
				+ (int) (appointmentTypeId ^ (appointmentTypeId >>> 32));
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result
				+ ((telephone == null) ? 0 : telephone.hashCode());
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
		Appointment other = (Appointment) obj;
		if (appointmentHours != other.appointmentHours)
			return false;
		if (appointmentTypeId != other.appointmentTypeId)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (id != other.id)
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!telephone.equals(other.telephone))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Appointment [id=" + id + ", department=" + department
				+ ", city=" + city + ", appointmentTypeId=" + appointmentTypeId
				+ ", startDate=" + startDate + ", fullName=" + fullName
				+ ", telephone=" + telephone + ", email=" + email
				+ ", comments=" + comments + ", appointmentHours="
				+ appointmentHours + "]";
	}
	public long getScopeGroupId() {
		return scopeGroupId;
	}
	public void setScopeGroupId(long scopeGroupId) {
		this.scopeGroupId = scopeGroupId;
	}
}
