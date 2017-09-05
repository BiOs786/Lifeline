package codebros.areyouat.com.lifeline.sync;

/**
 * Created by BiOs on 05-09-2017.
 */

public class Hospital {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    String id;
    String latitude;
    String longitude;
    String address;
    String hospital;

    public Hospital(String id, String latitude, String longitude, String address, String hospital) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.hospital = hospital;
    }
}
