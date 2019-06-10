package objects;

import storingOfData.DB_Connection;

import java.io.Serializable;
import java.util.ArrayList;

public class Member extends User implements Serializable {
    private int memberId;
    private final String name;
    private final String ssn;
    private String address;
    private String phoneNumber;


    /**
     * CONSTRUCTOR 1:
     * Is a constructor that instantiates a temp Member object lacking a memberId value
     * Only need to validate this constructor.
     *
     *  @param username
     * @param hashedPassword
     * @param name
     * @param ssn
     * @param address
     * @param phoneNumber
     */
    public Member(String username, String hashedPassword,
                  String name, String ssn, String address, String phoneNumber) throws IllegalArgumentException
    {
        super(username, hashedPassword);

        DB_Connection connection = new DB_Connection();
        ArrayList<Member> appMembers = connection.readTableMember();
        for (Member currentMember : appMembers) {
            if (currentMember.getSsn().equals(ssn)) {
                throw new IllegalArgumentException(
                        "SSN is already in use");
            }
        }

        this.name = name;
        this.ssn = ssn;
        this.address = address;
        this.phoneNumber = phoneNumber;

    }


    /**
     * CONSTRUCTOR 2:
     * Is a constructor that instantiates the 'real' Member object (with memberId) after DB entry
     *
     * @param username
     * @param hashedPassword
     * @param memberId
     * @param name
     * @param ssn
     * @param address
     * @param phoneNumber
     */
    public Member(String username, String hashedPassword,
                  int memberId, String name, String ssn, String address, String phoneNumber) {
        super(username, hashedPassword);
        this.memberId = memberId;
        this.name = name;
        this.ssn = ssn;
        this.address = address;
        this.phoneNumber = phoneNumber;

    }




    public String getName() {
        return name;
    }

    public String getSsn() {
        return ssn;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getMemberId() {
        return memberId;
    }
}
