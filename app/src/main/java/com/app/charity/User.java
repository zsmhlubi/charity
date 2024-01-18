package com.app.charity;

public class User {

    Integer userID;
    String firstname;
    String surname;
    String email;
    String phone;
    String biography; // max 80 characters
    Integer totalDonations;

    public User(Integer userID, String firstname, String surname, String email, String phone, String biography, Integer totalDonations) {
        this.userID = userID;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.biography = biography;
        this.totalDonations = totalDonations;
    }
}
