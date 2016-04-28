package com.example.marti.laboratorio2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MenuData {

    String Name;
    String quantity;
    String description;
    String price;
    String photo;



    public void setName (String reservationName) {this.Name=reservationName;}
    public void setQuantity(String quantity){this.quantity=quantity;}
    public void setDescription(String description){this.description=description;}
    public void setPrice(String price){this.price=price;}
    public void setPhoto(String photo){this.photo=photo;}
    public String getName(){return Name;}
    public String getQuantity(){return quantity;}
    public String getDescription(){return description;}
    public String getPrice(){return price;}
    public String getPhoto(){return photo;}


    public static ArrayList<MenuData> getData(String JsonObject) throws JSONException
    {
        JSONArray menuDataJSON;
        JSONObject obj = new JSONObject(JsonObject);
        menuDataJSON=obj.getJSONArray("MenuData");
        int dim = menuDataJSON.length();
        String [] names = new String[dim];
        String [] quantities = new String[dim];
        String [] descriptions = new String[dim];
        String [] prices= new String[dim];
        String [] photos=new String[dim];

        for (int n = 0; n<menuDataJSON.length(); n++) {
            JSONObject object = menuDataJSON.getJSONObject(n);
            names[n]=object.get("Nome").toString();
            quantities[n]=object.get("Quantità").toString();
            descriptions[n]=object.get("Descrizione").toString();
            prices[n]=object.get("Prezzo").toString();
            if (object.has("Immagine"))
                photos[n]=object.get("Immagine").toString();
            else photos[n]="nophoto";
        }
        ArrayList<MenuData> dataList = new ArrayList<>();
        for (int i=0; i<names.length;i++)
        {
            MenuData menuData = new MenuData();
            menuData.setQuantity(quantities[i]);
            menuData.setName(names[i]);
            menuData.setDescription(descriptions[i]);
            menuData.setPrice(prices[i]);
            menuData.setPhoto(photos[i]);

            dataList.add(menuData);
        }
        return dataList;
    }
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
}
