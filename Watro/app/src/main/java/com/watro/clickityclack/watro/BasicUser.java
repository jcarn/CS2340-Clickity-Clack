public class BasicUser extends SuperUser {

    protected String address;
    protected String firstName;
    protected String lastName;

    public BasicUser(String fn, String ln, String e, String id, String a) {
        super(e, id);
        this.firstName = fn;
        this.lastName = ln;
        this.address = a;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String f) {
        this.firstName = f;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String l) {
        this.lastName = l;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String a) {
        this.address = a;
    }
}