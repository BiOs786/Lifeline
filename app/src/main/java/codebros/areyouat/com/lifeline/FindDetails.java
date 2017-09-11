package codebros.areyouat.com.lifeline;

/**
 * Created by BiOs on 11-09-2017.
 */

public class FindDetails {

    public FindDetails(String id, String latitude, String longitude, String bloodtype, String city, String quantity) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bloodtype = bloodtype;
        this.city = city;
        this.quantity = quantity;
    }

    String id;
    String latitude;
    String longitude;
    String bloodtype;
    String city;
    String quantity;

}
