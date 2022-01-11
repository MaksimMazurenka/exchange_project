package com.example.finalproject.entity;

public class User {
    private int iduser;
    private String name;
    private String email;
    private int password;

    public User(){}

    public User(int iduser, String name, String email, int password) {
        this.iduser = iduser;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + iduser + "} name:'" + name + '\'' + ", email:'" + email + '\'' + ", password:'" + password + '\'';
    }
}
