package com.example.petway.Model;

public class Users {

    private String username;
    private String email;
    private String phone;
    private String fName;
    private String lname;
    private String birth;
    private String gender;
    private String image;

    public Users() {
    }

    public Users(String username, String email, String phone, String fName, String lname, String birth, String gender, String image) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.fName = fName;
        this.lname = lname;
        this.birth = birth;
        this.gender = gender;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
