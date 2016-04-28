package com.example.marti.laboratorio2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marti on 06/04/2016.
 */
public class ReservationDataTables {
    String reservationTime;
    String reservationName;
    String reservationSurname;
    String notes;
    String email;
    String phone;
    String number;

    public void setReservationTime (String reservationTime) {this.reservationTime=reservationTime;}
    public void setReservationName (String reservationName) {this.reservationName=reservationName;}
    public void setEmail (String email) {this.email=email;}
    public void setPhone (String phone) {this.phone=phone;}
    public void setReservationSurname (String reservationName) {this.reservationSurname= reservationName;}
    public void setNotes (String notes) {this.notes=notes;}

    public void setNumber (String number) {this.number=number;}
    public String getReservationTime() {return reservationTime;}
    public String getReservationName(){return reservationName;}
    public String getReservationSurname() {return reservationSurname;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public String getNotes() {return notes;}
    public String getNumber() {return number;}


    public static ArrayList<ReservationDataTables> getData(String JsonObject) throws JSONException {
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
        String [] number=new String[dim];
        for (int n = 0; n<reservationDataJSON.length(); n++) {
            JSONObject object = reservationDataJSON.getJSONObject(n);
            names[n]=object.get("Nome").toString();
            surnames[n]=object.get("Cognome").toString();
            times[n]=object.get("Orario").toString();
            email[n]=object.get("Email").toString();
            phone[n]=object.get("Telefono").toString();
            notes[n]=object.get("Note").toString();
            number[n]=object.get("Numero").toString();
        }
        ArrayList<ReservationDataTables> dataList = new ArrayList<>();

        for (int i=0; i<names.length;i++)
        {
            ReservationDataTables reservationDataTables = new ReservationDataTables();
            reservationDataTables.setReservationTime(times[i]);
            reservationDataTables.setReservationName(names[i]);
            reservationDataTables.setReservationSurname(surnames[i]);
            reservationDataTables.setEmail(email[i]);
            reservationDataTables.setPhone(phone[i]);
            reservationDataTables.setNotes(notes[i]);
            reservationDataTables.setNumber(number[i]);
            dataList.add(reservationDataTables);
        }
        return dataList;
    }
}
