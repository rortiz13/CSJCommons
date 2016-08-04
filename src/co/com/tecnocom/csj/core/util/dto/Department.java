package co.com.tecnocom.csj.core.util.dto;

import java.util.Map;

public class Department {
    private int id;
    private int code;
    private String name;
    private Map<Integer, City> cities;

    public int getCode() {
	return code;
    }

    public void setCode(int code) {
	this.code = code;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Map<Integer, City> getCities() {
	return cities;
    }

    public void setCities(Map<Integer, City> cities) {
	this.cities = cities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + code;
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
	Department other = (Department) obj;
	if (code != other.code)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Department [code=" + code + ", name=" + name + ", cities=" + cities + "]";
    }

}
