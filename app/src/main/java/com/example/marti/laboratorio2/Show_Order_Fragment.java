package com.example.marti.laboratorio2;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by alfonsomartino on 18/04/16.
 */
public class Show_Order_Fragment extends Fragment {
    String string="";
    TextView nome;
    TextView quantità;
    TextView descrizione;
    TextView prezzo;
    ImageView immagine;
    String encodedImage=new String();
    int posizione;
    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_show_order,container,false);
        nome = (TextView) view.findViewById(R.id.textName);
        quantità = (TextView) view.findViewById(R.id.textQuantity);
        descrizione = (TextView) view.findViewById(R.id.editDescription);
        prezzo=(TextView) view.findViewById(R.id.editPrice);
        immagine= (ImageView) view.findViewById(R.id.imgView);
        return view;
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
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void setText(String name,String quantity, String description,String price,String image) {
        this.nome.setText(name);
        this.quantità.setText(quantity);
        this.descrizione.setText(description);
        this.prezzo.setText(price);
        this.immagine.setImageBitmap(decodeBase64(image));
    }
}
