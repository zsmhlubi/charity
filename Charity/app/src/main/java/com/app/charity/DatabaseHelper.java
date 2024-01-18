package com.app.charity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Map;


public class DatabaseHelper {
    static Integer  userId;
    //Login, Signup,
    public static void getUserID(Context context, String email, String phone, String password){
        Map<String, String> map = new HashMap<>();
        map.put("email",email );
        map.put("phoneNum", phone);
        map.put("password", password);

            String url = "https://lamp.ms.wits.ac.za/~s2460303/login.php";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject j = arr.getJSONObject(i);
                                userId =Integer.parseInt( j.getString("USER_ID"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data=map;
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
    }

    public static void addUsertoDB(Context context,String firstname, String surname, String email, String phone, String password, String idNumber){

        Map<String, String> map = new HashMap<>();
        map.put("name", firstname);
        map.put("surname", surname);
        map.put("email",email );
        map.put("phoneNum", phone);
        map.put("password", password);
        map.put("id", idNumber);

        Save(context, map, "signUp");
        //check if email is unique amd check if phone is unique (i.e. there should be no rows with either of these values)
        //find minimum unused ID number and insert it along with the above in to a new column
        // default value for total donation should be zero (should probably make it default in mysql)
    }


    public static User getUser(Context context, Integer userId){

        User user = new User(12, "","","","","",200);
        return user;
    }

    //MainActivity.
    //From our previous activities (Launcher, Signup, Login) we have a sharedPreference that holds the logged in userId

    //We use it to get values from the user Table and to create a user class instance
    // Since the user can set and change their biography in the mainActivity we need a function that'll update it in the DB
    public static void saveBiographytoDB(Integer userID, String biography){

        return;
    }

    // WE then create a user instance to keep the relevant data grouped up. ** WILL PROBABLY MOVE TO USER CLASS** but please complete it anyway
    public static User makeUserInstance(Integer userId){
        // get user data from  User table and input it below

        User user = new User(12, "Ulrich", "Main", "ulrichmain28@gmail.com","0833576032", "Hello and thanks", 0 ); // insert user values here
        return user;
    }

    //returns an array list used for making the ranking list
    public static ArrayList<User> getRankingList() {
        // should return a descending list of Users with the most totaldonations with max length 100;
        ArrayList<User> arrayList = new ArrayList<User>();
        // for top 100 userIDs { ....
            arrayList.add(DatabaseHelper.makeUserInstance(1));  //temp
            return arrayList;
    }



    //Resources
    //Since the users will see strings we need a function that will return the resource ID given its string
    public static Integer  getResourceId(String resourceName){
        return 1; //temp
    }
    // vice versa
    public static String  getResourceName(Integer resourceId){
        return "coffee"; //temp
    }
    // Need a function that will return a list of all resources in resource table as strings
    public static ArrayList<String> makeResourceArray(){
        ArrayList<String> resources = new ArrayList<>();
        resources.add("Milk"); //temp
        return resources;
    }





    //Requests
    //need a function that will get all requests for a specific resource
    public static ArrayList<userRequest> makeRequestArray(Integer resourceId) {
        ArrayList<userRequest> requestArray = new ArrayList<>();

        requestArray.add(new userRequest(1,1,1,12));
        requestArray.add(new userRequest(2,2,2,42));

        return requestArray;
    }

    //need a function that will add a request to the DB
    public static Integer addRequesttoDB(Integer userId, Integer resourceId, Integer Quantity){
        //needs to update the users total donations
        //needs to update the quantity of the resource in the resource table
        //needs to add the donation to the table and return the donationId

        return 1;
    }

    public static void updateRequest(Integer requestId, Integer changeQuantitybyThisValue){
        // should find the request in the request table and should subtract the second parameter from its  quantity value
        // after doing this it should check if the quantityvalue is zero and delete the request if so.
    }




    //Donations
    // need a function that will add a donation to the donation table (given resourceId, Quantity and userId) and return its donationId
    public static Integer addDonationtoDB(Integer userId, Integer resourceId, Integer Quantity){
        //needs to update the users total donations
        //needs to update the quantity of the resource in the resource table
        //needs to add the donation to the table and return the donationId

        return 1;
    }

    public static void updateDonation(Integer donationId, Integer changeQuantitybyThisValue){
        // should find the donation in the donation table and should subtract the second parameter from its value)
        // after doing this it should check if the donation value is zero and delete it if so.
    }



    //Transactions

    // similar to previous add____toDB functions
    public static Integer addTransactiontoDB(Integer donationId, Integer requestId){

        return 1;
    }

    // should delete transaction from transaction table
    public static void deleteTransactionfromDB(Integer donationId, Integer requestId){

    }





    public static void Save(Context context, Map<String,String> data_to_send , String PhpName){

        String url = "https://lamp.ms.wits.ac.za/~s2460303/"+PhpName+".php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response.trim(), Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString().trim(), Toast.LENGTH_SHORT).show();
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



}
