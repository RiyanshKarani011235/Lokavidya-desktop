package com.iitb.lokavidya.core.data;



import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Reference.
 */

public class Reference implements Serializable {

    private String videoID;

    public Reference(Reference r)
    {
    	setVideoID(r.getVideoID());
    }
    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    @Override
    public String toString() {
        return "Reference{" +
            ", videoID='" + videoID + "'" +
            '}';
    }
}
