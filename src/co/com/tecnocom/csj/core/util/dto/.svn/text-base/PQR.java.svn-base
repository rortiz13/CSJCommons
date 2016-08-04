package co.com.tecnocom.csj.core.util.dto;

import java.util.Date;
import java.util.List;

public class PQR {
    private long id;
    private String name;
    private Date submitted;
    private String originalComment;
    private String currentStatus;
    // Detail
    private String pqrType;
    private String documentType;
    private String documentNumber;
    private String phone;
    private String email;
    private String address;
    private String city;
    private Long liferayUserId;
    private String liferayUserName;
    private String filename;

    private List<PQRRecord> records;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Date getSubmitted() {
	return submitted;
    }

    public void setSubmitted(Date submitted) {
	this.submitted = submitted;
    }

    public String getOriginalComment() {
	return originalComment;
    }

    public void setOriginalComment(String originalComment) {
	this.originalComment = originalComment;
    }

    public String getCurrentStatus() {
	return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
	this.currentStatus = currentStatus;
    }

    public String getPqrType() {
	return pqrType;
    }

    public void setPqrType(String pqrType) {
	this.pqrType = pqrType;
    }

    public String getDocumentType() {
	return documentType;
    }

    public void setDocumentType(String documentType) {
	this.documentType = documentType;
    }

    public String getDocumentNumber() {
	return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
	this.documentNumber = documentNumber;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public List<PQRRecord> getRecords() {
	return records;
    }

    public void setRecords(List<PQRRecord> records) {
	this.records = records;
    }

    public Long getLiferayUserId() {
	return liferayUserId;
    }

    public void setLiferayUserId(Long liferayUserId) {
	this.liferayUserId = liferayUserId;
    }

    public String getLiferayUserName() {
	return liferayUserName;
    }

    public void setLiferayUserName(String liferayUserName) {
	this.liferayUserName = liferayUserName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
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
	PQR other = (PQR) obj;
	if (id != other.id)
	    return false;
	return true;
    }
}
