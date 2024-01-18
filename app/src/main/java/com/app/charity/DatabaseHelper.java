package com.app.charity;


import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.util.ArrayMap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DatabaseHelper {

    //Users
    public static void addUsertoDB(Context context, String firstname, String surname, String email, String phone, String password, Callback callback){

        Map<String, String> map = new HashMap<>();
        map.put("name", firstname);
        map.put("surname", surname);
        map.put("email",email );
        map.put("phone", phone);
        map.put("password", password);
        Save(context, map, "signUp", callback);
    }
    public static void getUserId(Context context, String email, String phone, String password, Callback callback){

        Map<String, String> map = new HashMap<>();
        map.put("email",email );
        map.put("phone", phone);
        map.put("password", password);
        Get(context, map, "Login", callback);
    }
    public static void updateUserDonationAmount(Context context, Integer userId, Integer quantity){

        Map<String, String> map = new HashMap<>();
        map.put("UserId", userId.toString() );
        map.put("Quantity", quantity.toString());
        Save(context, map, "updateUserDonationAmount", new Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {
                ;
            }
        });
    }


    //MainActivity
    public static void updateBiography(Context context, Integer userId, String biography, Callback callback){

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId.toString());
        map.put("biography", biography);
        Save(context, map, "updateBiography", callback);
    }
    public static void getTopUsers(Context context, Callback callback){

        Get(context, null, "getSortedUsers", callback);

    }
    public static void getUser(Context context,Integer userId, Callback callback){

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId.toString());
        Get(context, map, "getUser", callback);
    }


    // ResourceList
    public static void makeResourceList(Context context, Callback callback){

        Get(context, null,  "getResources", callback);

    }
    public static void getResourceId(Context context, String resourceName, Callback callback){

        Map<String, String> map = new HashMap<>();
        map.put("resourceName", resourceName);
        Get(context, map, "getResourceId", callback);
    }
    public static ArrayList<User> makeUserList(String response) throws JSONException {

        /* Works with makeUSerfromJSONObject to create a list of all returned Users from some php request */

        ArrayList<User> users = new ArrayList<>();
        JSONArray arr = new JSONArray(response);

        for (int i=0; i< arr.length(); i++){
            User user= makeUserfromJSONObject(arr, i);
            users.add(user);
        }

        return users;
    }
    public static User makeUserfromJSONObject(JSONArray arr, Integer i) throws JSONException {

        JSONObject obj = arr.getJSONObject(i);

        User user = new User(obj.getInt("UserId"), obj.getString("FirstName"),obj.getString("Surname"),
                obj.getString("Email"), obj.getString("Phone"),obj.getString("Biography"),obj.getInt("Donations"));
        return user;
    }
    public static void getResourceName(Context context, Integer resourceId, Callback callback){

        Map<String, String> map = new HashMap<>();
        map.put("ResourceId", resourceId.toString());
        Get(context, map, "getResourceName", callback);
    }

    // Requests


    public static void getRequests(Context context,Integer userId, Integer resourceId, Callback callback){

        Map<String, String> map = new HashMap<>();
        map.put("ResourceId", resourceId.toString() );
        map.put("UserId", userId.toString() );
        Get(context, map, "getRequests", callback);
    }
    public static ArrayList<UserRequest> makeRequestList(String response) throws JSONException {

        ArrayList<UserRequest> requests = new ArrayList<>();
        JSONArray arr = new JSONArray(response);

        for (int i=0; i< arr.length(); i++){
            UserRequest userRequest= makeRequestfromJSONObject(arr, i);
            requests.add(userRequest);
        }

        return requests;
    }
    public static UserRequest makeRequestfromJSONObject(JSONArray arr, Integer i) throws JSONException {

        JSONObject obj = arr.getJSONObject(i);

        UserRequest userrequest = new UserRequest(obj.getInt("UserId"), obj.getString("FirstName"), obj.getString("Surname"), obj.getString("Email"),
                obj.getString("Phone"), obj.getString("Biography"),
                obj.getString("Donations"), obj.getInt("RequestId"), obj.getInt("ResourceId"), obj.getInt("ResourceQuantity"));

        return userrequest;
    }
    public static void updateRequest(Context context, Integer requestId, Integer quantity){

        Map<String, String> map = new HashMap<>();
        map.put("RequestId", requestId.toString() );
        map.put("Quantity", quantity.toString());
        Save(context, map, "updateRequest", new Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {
                ;
            }
        });
    }
    public static void addRequest(Context context,Integer userId, Integer resourceId, Integer quantity){

        Map<String, String> map = new HashMap<>();
        map.put("UserId", userId.toString());
        map.put("ResourceId", resourceId.toString());
        map.put("Quantity", quantity.toString());
        Save(context, map, "addRequest", new Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {
                ;
            }
        });
    }

    // Donations

    public static void addDonation(Context context, Integer userId, Integer resourceId, Integer quantity, Callback callback){

        Map<String, String> map = new HashMap<>();
        map.put("UserId", userId.toString() );
        map.put("ResourceId", resourceId.toString());
        map.put("Quantity", quantity.toString());
        Save(context, map, "addDonation", callback);


    }
    public static void updateDonation(Context context, Integer donationId, Integer quantity){

        Map<String, String> map = new HashMap<>();
        map.put("DonationId", donationId.toString() );
        map.put("Quantity", quantity.toString());
        Save(context, map, "updateDonation", new Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {
                ;
            }
        });
    }


    // Transactions
    public static void addTransactions(Context context, Integer requestId, Integer donationId, Integer quantity, Integer resourceId){

        Map<String, String> map = new HashMap<>();
        map.put("RequestId", requestId.toString() );
        map.put("DonationId", donationId.toString());
        map.put("Quantity", quantity.toString());
        map.put("ResourceId", resourceId.toString() );
        Save(context, map, "addTransactions", new Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {
                ;
            }
        });
    }
    public static void getTransactions(Context context, Integer userId, Callback callback){

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId.toString());
        Get(context, map, "getTransaction", callback);
    }
    public static ArrayList<Transaction> makeTransactionList(String response) throws JSONException {

        ArrayList<Transaction> transactions = new ArrayList<>();
        JSONArray arr = new JSONArray(response);

        for (int i=0; i< arr.length(); i++){
            Transaction transaction = makeTransactionfromJSONObject(arr, i);
            transactions.add(transaction);
        }

        return transactions;
    }
    public static Transaction makeTransactionfromJSONObject(JSONArray arr, Integer i) throws JSONException {

        JSONObject obj = arr.getJSONObject(i);

        Transaction transaction = new Transaction(obj.getInt("TRANSACTIONS_ID"), obj.getInt("REQUEST_ID"),  obj.getInt("DONATION_ID"),
                obj.getInt("RequesterId"), obj.getInt("RESOURCE_ID"), obj.getInt("QUANTITY"));

        return transaction;
    }
    public static void updateTransaction(Context context,Integer transactionId){
        Map<String, String> map = new HashMap<>();
        map.put("TransactionId", transactionId.toString() );
        Save(context, map, "updateDonation", new Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {
                ;
            }
        });


    }


    //Utility

    // Be careful when messing with this
    public static void Get(Context context, Map<String,String> referenceData , String PhpName, Callback callback){

        String url = "https://lamp.ms.wits.ac.za/~s1701173/"+PhpName+".php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    callback.myResponseCallback(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"error" /*error.toString().trim()*/, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data=referenceData;
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void Save(Context context, Map<String,String> data_to_send , String PhpName, Callback callback){

        String url = "https://lamp.ms.wits.ac.za/~s1701173/"+PhpName+".php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    callback.myResponseCallback(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"error" /*error.toString().trim()*/, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data=data_to_send;
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    interface Callback{
        void myResponseCallback(String response) throws JSONException;//whatever your return type is: string, integer, etc.
    }


}
