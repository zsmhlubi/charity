package com.app.charity;

public class Transaction {

    public Integer transactionID; // made by going to databse and finding minimum unused ID number
    public Integer requestID;
    public Integer donationID;
    public Integer recipientID;
    public Integer resourceID;
    public Integer quantityDonated;


    public Transaction(Integer transactionID, Integer requestID, Integer donationID, Integer recipientID, Integer resourceID, Integer quantityDonated) {
        this.transactionID = transactionID;
        this.requestID = requestID;
        this.donationID = donationID;
        this.recipientID = recipientID;
        this.resourceID = resourceID;
        this.quantityDonated = quantityDonated;
    }
}
