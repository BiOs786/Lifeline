package codebros.areyouat.com.lifeline;

/**
 * Created by BiOs on 07-09-2017.
 */

public class HospitalLoginDetails {

    String id;
    String username;
    String email;
    String mobile;
    String hospital;

    String regno;
    String city;
    String password;

    String address;
    String latitude;
    String longitude;

    public HospitalLoginDetails(String id, String username, String email, String mobile,
                                String hospital, String regno, String city, String password,
                                String address, String latitude, String longitude) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.hospital = hospital;
        this.regno = regno;
        this.city = city;
        this.password = password;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
