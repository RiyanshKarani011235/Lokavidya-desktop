package com.iitb.lokavidya.core.data;

import java.io.Serializable;
import java.util.Objects;

import com.iitb.lokavidya.core.utils.GeneralUtils;

/**
 * A LayeringObject.
 */
enum LayeringType{
	TYPE1,TYPE2
}
public class LayeringObject implements Serializable {

	public LayeringObject() {
		this.id= GeneralUtils.generateRandomNumber(11).intValue();
	}
	
    private Double start;

    private Double end;

    private String extra;

    private LayeringType type;

    private Integer id;
    
    public LayeringObject(LayeringObject lo)
    {
    	setStart(lo.getStart());
    	setEnd(lo.getEnd());
    	setExtra(lo.getExtra());
    	setType(lo.getType());
    	setId(lo.getId());
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    public Double getEnd() {
        return end;
    }

    public void setEnd(Double end) {
        this.end = end;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public LayeringType getType() {
        return type;
    }

    public void setType(LayeringType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LayeringObject layeringObject = (LayeringObject) o;
        return Objects.equals(id, layeringObject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LayeringObject{" +
            "id=" + id +
            ", start='" + start + "'" +
            ", end='" + end + "'" +
            ", extra='" + extra + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
