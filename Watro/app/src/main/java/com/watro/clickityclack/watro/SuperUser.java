package com.watro.clickityclack.watro;

abstract class SuperUser {
    
    protected String email;
    protected String id;

    public SuperUser(String email, String id) {
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}