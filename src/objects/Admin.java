package objects;

import java.io.Serializable;

public class Admin extends User implements Serializable {

    public Admin(String username, String hashedPassword) {
        super(username, hashedPassword);
    }
}
