package com.app.charity;

public class userRequest {

    public Integer requestId;
    public Integer resourceId;
    public Integer userId;
    public Integer amountRequested;


    public userRequest(Integer requestId, Integer resourceId, Integer userId, Integer amountRequested) {
        this.requestId = requestId;
        this.resourceId = resourceId;
        this.userId = userId;
        this.amountRequested = amountRequested;
    }

}
