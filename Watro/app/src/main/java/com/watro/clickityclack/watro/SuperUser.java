abstract class SuperUser {
    
    protected String email;
    protected String id;

    public SuperUser(String e, String id) {
        this.email = e;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
    }
}