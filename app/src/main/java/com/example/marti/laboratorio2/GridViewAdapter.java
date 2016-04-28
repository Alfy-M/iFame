package com.example.marti.laboratorio2;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewAdapter extends ArrayAdapter<Bitmap> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Bitmap> data = new ArrayList<>();
    public static final String MyPREFERENCES = "MyPrefs";
    private final SharedPreferences sharedPrefs;


    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Bitmap> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        sharedPrefs = context.getSharedPreferences(MyPREFERENCES, 0);
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;


        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Bitmap item = data.get(position);
        holder.image.setImageBitmap(item);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int duration = Toast.LENGTH_SHORT; //Durata del messaggio di cancellazione
                AlertDialog delete = new AlertDialog.Builder(context).setTitle(context.getString(R.string.delete)).setMessage(context.getString(R.string.askDelete)).setIcon(R.drawable.ic_delete_black_18dp).setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, context.getString(R.string.foto_eliminata), duration).show();
                        saveArray();
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
        return row;
    }

    static class ViewHolder {
        ImageView image;
    }

    public boolean saveArray() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("Status_size", data.size());
        for (int j=0; j<20; j++) {
            mEdit1.remove("Status_"+j);
        }
        for (int i=0; i<data.size();i++) {
            mEdit1.putString("Status_"+i,encodeTobase64(data.get(i)));
        }
        return  mEdit1.commit();
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
