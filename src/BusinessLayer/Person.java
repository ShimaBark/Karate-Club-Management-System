package BusinessLayer;

public class Person {

    private String name;
    private String address;
    private String contactInfo;
    public Person(String name, String address, String contactInfo)
    {
        this.name = name;
        this.address=address;
        this.contactInfo=contactInfo;
    }
    public Person(){
        this.name="";
        this.address="";
        this.contactInfo="";
    }
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() {
        return contactInfo;
    }



}
