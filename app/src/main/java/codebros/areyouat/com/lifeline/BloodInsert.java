package codebros.areyouat.com.lifeline;

/**
 * Created by BiOs on 11-09-2017.
 */

public class BloodInsert {

    public BloodInsert(String id, String bloodtype, String quantity, String expiry, String blooduid) {
        this.id = id;
        this.bloodtype = bloodtype;
        this.quantity = quantity;
        this.expiry = expiry;
        this.blooduid = blooduid;
    }

    String id;
    String bloodtype;
    String quantity;
    String expiry;
    String blooduid;

}
