package com.example.customauthenticationfirebase;

public class Users {

    private String FullName;
    private String Username;
    private String Email;
    private String Contact;
    private String UID;
    private String UserType;

    public void Users(String fullName, String username, String email, String contact, String uid,
                      String userType){
        this.FullName = fullName;
        this.Username = username;
        this.Email = email;
        this.Contact = contact;
        this.UID = uid;
        this.UserType = userType;
    }


    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }


}
