package com.example.marti.laboratorio2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marti on 05/04/2016.
 */

public class ReservationData {
    String reservationTime;
    String reservationName;
    String reservationSurname;
    String notes;
    String email;
    String phone;
    ArrayList<String> orders;
    ArrayList<Integer> quantity;



    public void setReservationTime (String reservationTime) {this.reservationTime=reservationTime;}
    public void setReservationName (String reservationName) {this.reservationName=reservationName;}
    public void setEmail (String email) {this.email=email;}
    public void setPhone (String phone) {this.phone=phone;}
    public void setReservationSurname (String reservationName) {this.reservationSurname= reservationName;}
    public void setNotes (String notes) {this.notes=notes;}
    public String getReservationTime() {return reservationTime;}
    public String getReservationName(){return reservationName;}
    public String getReservationSurname() {return reservationSurname;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public ArrayList<String> getOrders() {return orders;}
    public ArrayList<Integer> getQuantity() {return quantity;}
    public String getNotes() {return notes;}




    public static ArrayList<ReservationData> getData(String JsonObject) throws JSONException
    {
        JSONArray reservationDataJSON;
        JSONObject obj = new JSONObject(JsonObject);
        reservationDataJSON=obj.getJSONArray("reservationData");
        int dim = reservationDataJSON.length();
        String [] names = new String[dim];
        String [] surnames = new String [dim] ;
        String [] times = new String [dim] ;
        String [] email = new String[dim];
        String [] phone = new String[dim];
        String [] notes = new String[dim];
        ArrayList<ArrayList<String>> ordersList= new ArrayList<ArrayList<String>>();
        ArrayList<Integer> dimensionList=new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> quantityList= new ArrayList<ArrayList<Integer>>();


        for (int n = 0; n<reservationDataJSON.length(); n++) {
            JSONObject object = reservationDataJSON.getJSONObject(n);
            names[n]=object.get("Nome").toString();
            surnames[n]=object.get("Cognome").toString();
            times[n]=object.get("Orario").toString();
            email[n]=object.get("Email").toString();
            phone[n]=object.get("Telefono").toString();
            notes[n]=object.get("Note").toString();
            JSONArray ordini = object.getJSONArray("Ordini");
            JSONArray quantita = object.getJSONArray("Quantita");
            ordersList.add(n,new ArrayList<String>());
            quantityList.add(n,new ArrayList<Integer>());
            dimensionList.add(ordini.length());
            for  (int j=0;j< ordini.length();j++)
            {
                ordersList.get(n).add(j, ordini.getString(j));
                quantityList.get(n).add(j, quantita.getInt(j));
            }
        }
        ArrayList<ReservationData> dataList = new ArrayList<>();
        for (int i=0; i<names.length;i++)
        {
            ReservationData reservationData = new ReservationData();
            reservationData.orders = new ArrayList<String>();
            reservationData.quantity = new ArrayList<Integer>();
            reservationData.setReservationTime(times[i]);
            reservationData.setReservationName(names[i]);
            reservationData.setReservationSurname(surnames[i]);
            reservationData.setEmail(email[i]);
            reservationData.setPhone(phone[i]);
            reservationData.setNotes(notes[i]);

            for(int j=0; j <dimensionList.get(i);j++)
            {
                reservationData.orders.add(ordersList.get(i).get(j));
                reservationData.quantity.add(quantityList.get(i).get(j));
            }
            dataList.add(reservationData);
        }
        return dataList;
    }
}
