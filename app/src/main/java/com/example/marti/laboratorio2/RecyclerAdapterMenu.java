package com.example.marti.laboratorio2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marti on 05/04/2016.
 * **/
public class RecyclerAdapterMenu extends RecyclerView.Adapter<RecyclerAdapterMenu.MyViewHolderMenu> {
    private  List<MenuData> mData;
    private LayoutInflater mInflater;
    private static Context context;

    static OnItemClickListener mItemClickListener;
    static OnDeleteClickListener mItemClickListenerDelete;


    //Communication with Activity

    public RecyclerAdapterMenu (Context context,List<MenuData> data)
    {
        this.context= context;
        this.mData=data;
        this.mInflater=LayoutInflater.from(context);

    }



    public  class MyViewHolderMenu extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView quantity;
        TextView name;
        TextView price;
        ImageView image;
        ImageButton btnInfo;
        public View view;
        public MenuData data;
        public CardView mCardView;


        public MyViewHolderMenu(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.res_name_dish);
            quantity = (TextView) itemView.findViewById(R.id.res_quantity);
            btnInfo = (ImageButton) itemView.findViewById(R.id.buttonInfoDish);
            price= (TextView) itemView.findViewById(R.id.res_price);
            image= (ImageView) itemView.findViewById(R.id.previewDish);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                String immagine;
                if(data.getPhoto().equals("nophoto")) {
                    immagine = encodeTobase64(BitmapFactory.decodeResource(v.getResources(), R.drawable.food));
                }
                else {
                    immagine=(data.getPhoto());
                }
                mItemClickListener.onItemClick(v,String.valueOf(getAdapterPosition()) ,data.getName(),data.getQuantity(),data.getPrice(),data.getDescription(),immagine);
            }
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view , String position, String name, String quantity, String price,String description, String image);

    }
    public interface OnDeleteClickListener {
        public void onItemClickDelete(View view , Boolean check);

    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;

    }
    public void SetOnItemClickListenerDelete (final OnDeleteClickListener mItemClickListenerDelete) {
        this.mItemClickListenerDelete = mItemClickListenerDelete;
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
    @Override
    public MyViewHolderMenu onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = mInflater.inflate(R.layout.card_list_menu,parent,false);
        MyViewHolderMenu holder = new MyViewHolderMenu(view);
        return holder;
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }



    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void setFilter(List<MenuData> countryModels) {
        mData = new ArrayList<>();
        mData.addAll(countryModels);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolderMenu holder, final int position) {
        final MenuData currentObj = mData.get(position);

        holder.data = mData.get(position);
        //holder.setData(currentObj,position);
        holder.quantity.setText(String.valueOf(currentObj.getQuantity()));
        holder.name.setText(String.valueOf(currentObj.getName()));
        holder.price.setText(currentObj.getPrice());
        if(currentObj.getPhoto().equals("nophoto"))
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.food));
        else
            holder.image.setImageBitmap(decodeBase64(currentObj.getPhoto()));

        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog delete = new AlertDialog.Builder(context).setTitle(context.getString(R.string.delete)).setMessage(context.getString(R.string.askDelete)).setIcon(R.drawable.ic_delete_black_18dp).setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            removeItem(position);
                            mItemClickListenerDelete.onItemClickDelete(v,true);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                delete.show();

            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = context.getAssets().open("MenuData.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    public void removeItem(int position) throws JSONException
    {
        //PROBLEMI CON L'AGGIORNAMENTO IN JSON
        //reservationDataJSON.remove(position);
        // NON GESTISCE L'AGGIORNAMENTO DELLE LISTE!!! E' SOLO UN FORMATO.
        //PER QUESTO MOTIVO, MODIFICARE I DATI RICHIEDE L'USO DI ARRAYLIST.
        //DA QUESTE ARRAYLIST CREEREMO UN NUOVO OGGETTO JSON CHE RIASSUME (DOPO L'ELIMINAZIONE)
        //TUTTI GLI ORDINI RIMANENTI.
        //SALVEREMO NELLE SHARED PREFERENCES IL NUOVO OGGETTO JSON obj2



        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String JsonObject = app_preferences.getString("jsonMenu", loadJSONFromAsset());

        JSONObject obj = new JSONObject(JsonObject);
        JSONObject obj2 = new JSONObject();
        JSONArray reservationDataJSON=obj.getJSONArray("MenuData");
        JSONArray reservationDataJSON2=new JSONArray();

        ArrayList<JSONObject> jsList= new ArrayList<JSONObject>();
        for(int k=0; k<reservationDataJSON.length();k++)
        {
            jsList.add(reservationDataJSON.getJSONObject(k));
        }


        //reservationDataJSON.remove(position);
        jsList.remove(position);

        for(int k=0;k<jsList.size();k++)
        {
            reservationDataJSON2.put(k,jsList.get(k));

        }
        obj2.put("MenuData",(Object) reservationDataJSON2);

        // logs for debugging
        Log.i("IOqualcosa", "OLD OBJECT" + obj.toString());
        Log.i( "IOqualcosa","OLD POSITION IS" + position);
        Log.i( "IOqualcosa","NEW OBJECT" + obj2.toString());

        SharedPreferences.Editor editor = app_preferences.edit();

        editor.putString("jsonMenu", obj2.toString());
        editor.commit();
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

}