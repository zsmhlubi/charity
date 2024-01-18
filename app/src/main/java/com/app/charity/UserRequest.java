package com.app.charity;

public class UserRequest {

    public Integer userId;
    public String firstName;
    public String surname;
    public String email;
    public String phone;
    public String Biography;
    public String Donations;
    public Integer requestId;
    public Integer resourceId;
    public Integer amountRequested;

    public String getBiography() {
        if (Biography.equals("null"))
            return "";
        return Biography;
    }

    public UserRequest(Integer userId, String firstName, String surname, String email, String phone, String biography, String donations, Integer requestId, Integer resourceId, Integer amountRequested) {
        this.userId = userId;
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        Biography = biography;
        Donations = donations;
        this.requestId = requestId;
        this.resourceId = resourceId;
        this.amountRequested = amountRequested;
    }
}

