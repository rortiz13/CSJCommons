package co.com.tecnocom.csj.core.util.dto;

import java.util.Date;

public class PQRRecord {
    private Long id;
    private Long statusId;
    private String statusName;
    private Long pqrId;
    private String comment;
    private Date updated;
    private Long liferayUserId;
    private String liferayUserName;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getStatusId() {
	return statusId;
    }

    public void setStatusId(Long statusId) {
	this.statusId = statusId;
    }

    public Long getPqrId() {
	return pqrId;
    }

    public void setPqrId(Long pqrId) {
	this.pqrId = pqrId;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public Date getUpdated() {
	return updated;
    }

    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    public Long getLiferayUserId() {
	return liferayUserId;
    }

    public void setLiferayUserId(Long liferayUserId) {
	this.liferayUserId = liferayUserId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getLiferayUserName() {
        return liferayUserName;
    }

    public void setLiferayUserName(String liferayUserName) {
        this.liferayUserName = liferayUserName;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
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
	PQRRecord other = (PQRRecord) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }
}
