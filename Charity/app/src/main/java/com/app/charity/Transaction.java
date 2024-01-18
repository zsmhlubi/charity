package com.app.charity;

public class Transaction {

    public Integer transactionID; // made by going to databse and finding minimum unused ID number
    public Integer donaterID;
    public Integer recipientID;
    public Integer quantityDonated;



    public Transaction(Donation donation, userRequest request, Integer quantityDonated) {
        this.donaterID = donation.userId;
        this.recipientID= request.userId;
        this.quantityDonated=quantityDonated;
    }
}
