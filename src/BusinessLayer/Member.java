package BusinessLayer;
import DataAccessLayer.MemberData;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class Member extends Person {
    private enum enMode {UpdateMode, AddNewMode}

    private String MembershipKey;
    private String EmergencyContactInfo;
    private SubscriptionPeriod subscriptionPeriod;
    private String belt;
    private int instructorID;
    private enMode mode;
    private boolean markForDelete = false;

// Initialization  constructor for loading members from the file in Update Mode.
    public Member(String MembershipKey, String name, String address, String contactInfo, String emergencyContactInfo, SubscriptionPeriod subscriptionPeriod, String belt, int instructorID) {
        super(name, address, contactInfo);
        this.MembershipKey = MembershipKey;
        EmergencyContactInfo = emergencyContactInfo;
        this.subscriptionPeriod = subscriptionPeriod;
        this.belt = belt;
        this.instructorID = instructorID;
        this.mode = enMode.UpdateMode;
    }
//  Creates a new member with default values in Add New Mode.
    public Member() {
        super("", "", "");
        this.MembershipKey = "MEM" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
        this.EmergencyContactInfo = "";
        this.belt = "";
        this.subscriptionPeriod = null;
        this.instructorID = -1;
        this.mode = enMode.AddNewMode;
    }
    // Setter and getter
    public void setEmergencyContactInfo(String emergencyContactInfo) {
        EmergencyContactInfo = emergencyContactInfo;
    }
    public String getEmergencyContactInfo() {
        return EmergencyContactInfo;
    }


    public void setSubscriptionPeriod(SubscriptionPeriod subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }
    public SubscriptionPeriod getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public void setBelt(String belt) {
        this.belt = belt;
    }
    public String getBelt() {
        return belt;
    }

    public void setInstructorID(int instructorID) {
        this.instructorID = instructorID;
    }
    public int getInstructorID() {
        return instructorID;
    }
    public String getMembershipKey() {
        return MembershipKey;
    }


    public boolean isActive() {
        if (subscriptionPeriod == null)
            return false;
        LocalDate today = LocalDate.now();

        return !subscriptionPeriod.getStartDate().isAfter(today)
                && !subscriptionPeriod.getEndDate().isBefore(today);
    }
// add new member and update member methodes
    private boolean addNewMember() {
        return MemberData.saveNewMember(this);

    }
    private boolean updateMember() {
        ArrayList<Member> members = MemberData.getAllMembers();
        for (Member m : members) {
            if (m.MembershipKey.equals(this.MembershipKey)) {
                m.setName(this.getName());
                m.setAddress(this.getAddress());
                m.setContactInfo(this.getContactInfo());
                m.EmergencyContactInfo = this.EmergencyContactInfo;
                m.subscriptionPeriod = this.subscriptionPeriod;
                m.belt = this.belt;
                m.instructorID = this.instructorID;
                return MemberData.resaveAllMember(members);

            }
        }
        return false;

    }
    public boolean Save() {
        switch (mode) {
            case AddNewMode -> {
                if (addNewMember()) {
                    this.mode = enMode.UpdateMode;
                    return true;
                }
            }
            case UpdateMode -> {
                return updateMember();

            }
        }
        return false;
    }

    public boolean isMarkForDelete() {
        return markForDelete;
    }

    public boolean deleteMember() {
        ArrayList<Member> members = MemberData.getAllMembers();
        for (Member m : members) {
            if (m.MembershipKey.equals(this.MembershipKey)) {
                m.markForDelete = true;
                return MemberData.resaveAllMember(members);
            }
        }
        return false;

    }



    public static Member Find(String MemberShipKey) {
        ArrayList<Member> members = MemberData.getAllMembers();
        for (Member member : members) {
            if (member.MembershipKey.equals(MemberShipKey))
                return member;
        }
        return null;
    }

    public static ArrayList<Member> getAllMembers() {
        return MemberData.getAllMembers();
    }

//we will use later for check if the new member is already in  the file
    public static boolean isExists(String name) {
        ArrayList<Member> members = getAllMembers();
        for (Member member : members) {
            if (member.getName().trim().equals(name.trim()))
                return true;
        }
        return false;
    }
    public String getNextBelt() {

        String[] belts = {
                "White",
                "Yellow",
                "Orange",
                "Green",
                "Blue",
                "Brown",
                "Black"
        };

        for (int i = 0; i < belts.length - 1; i++) {

            if (belt.equalsIgnoreCase(belts[i])) {
                return belts[i + 1];
            }
        }

        return null;
    }


    public boolean promoteBelt() {

        if (!isActive()) {
            return false;
        }

        String nextBelt = getNextBelt();

        if (nextBelt == null) {
            return false;
        }

        this.belt = nextBelt;

        return Save();
    }
}
