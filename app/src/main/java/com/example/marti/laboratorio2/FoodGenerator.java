package com.example.marti.laboratorio2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FoodGenerator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CAMERA=0;
    private static final int SELECT_FILE=1;
    String imgDecodableString;
    String encodedImage=new String();
    ImageView  immagine;
    EditText nome;
    EditText prezzo;
    EditText quantità;
    EditText descrizione;
    String chiamante;
    int posizione;
    String JsonString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_generator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.nav_menu));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nome = (EditText) findViewById(R.id.editName);
        quantità = (EditText) findViewById(R.id.editQuantity);
        descrizione= (EditText) findViewById(R.id.editDescription);
        prezzo=(EditText)findViewById(R.id.editPrice);
        immagine= (ImageView) findViewById(R.id.imgView);
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            nome.setText(extras.getString("nome", null));
            quantità.setText(extras.getString("quantita",null));
            descrizione.setText(extras.getString("descrizione",null));
            prezzo.setText(extras.getString("prezzo",null));
            chiamante = new String(extras.getString("chiamante",null));
            encodedImage=extras.getString("immagine", null);
            Log.i("oncreate", "encoded Image is" + encodedImage);
            imageDefault();
            if(encodedImage!=null) immagine.setImageBitmap(decodeBase64(encodedImage));
            posizione=extras.getInt("posizione",0);
        }
    }

    public void imageDefault()
    {
        if(encodedImage==null)
            encodedImage=new String(encodeTobase64(BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.food)));
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

    public void aggiungi(View view)
    {   boolean complete;
        if(encodedImage==null) encodedImage=new String(encodeTobase64(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.food)));
        EditText name=(EditText) findViewById(R.id.editName);
        EditText description=(EditText)findViewById(R.id.editDescription);
        EditText quantity=(EditText) findViewById(R.id.editQuantity);
        EditText price=(EditText) findViewById(R.id.editPrice);
        JSONObject addedObject=null;
        JSONObject modifiedObject=null;
        if (name.getText().toString().isEmpty()||description.getText().toString().isEmpty()||quantity.getText().toString().isEmpty())
        {complete=false;
            //  Log.i("INSERIMENTO","ISeMPTY");
        }
        else complete=true;
        if(complete)
        {
            if (chiamante.equals("button")) {
                addedObject = new JSONObject();
                try {
                    addedObject.put("Nome", name.getText().toString());
                    addedObject.put("Quantità", quantity.getText().toString());
                    addedObject.put("Immagine", encodedImage);
                    addedObject.put("Descrizione", description.getText().toString());
                    addedObject.put("Prezzo", price.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String JsonObject = app_preferences.getString("jsonMenu", loadJSONFromAsset());
                JSONObject obj = null;
                try {
                    obj = new JSONObject(JsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject obj2 = new JSONObject();
                JSONArray reservationDataJSON = null;
                try {
                    reservationDataJSON = obj.getJSONArray("MenuData");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray reservationDataJSON2 = new JSONArray();
                ArrayList<JSONObject> jsList = new ArrayList<JSONObject>();
                int k;
                for (k = 0; k < reservationDataJSON.length(); k++) {
                    try {
                        jsList.add(reservationDataJSON.getJSONObject(k));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("IOqualcosa", "OLD OBJECT" + addedObject.toString());
                jsList.add(addedObject);
                for (k = 0; k < jsList.size(); k++) {
                    try {
                        reservationDataJSON2.put(k, jsList.get(k));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    obj2.put("MenuData", (Object) reservationDataJSON2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // logs for debugging
                Log.i("IOqualcosa", "OLD OBJECT" + obj.toString());
                // Log.i("IOqualcosa", "OLD POSITION IS" + position);
                Log.i("IOqualcosa", "NEW OBJECT" + obj2.toString());

                SharedPreferences.Editor editor = app_preferences.edit();

                editor.putString("jsonMenu", obj2.toString());
                editor.commit();
                finish();
            } else
            {
                // CHIAMATO DA SHOW FOOD--SOLO MODIFICA

                modifiedObject = new JSONObject();

                try {
                    modifiedObject.put("Nome", name.getText().toString());

                    modifiedObject.put("Quantità", quantity.getText().toString());

                    modifiedObject.put("Immagine", encodedImage);
                    modifiedObject.put("Prezzo", price.getText().toString());
                    modifiedObject.put("Descrizione", description.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String JsonObject = app_preferences.getString("jsonMenu", loadJSONFromAsset());

                JSONObject obj = null;
                try {
                    obj = new JSONObject(JsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject obj2 = new JSONObject();
                JSONArray reservationDataJSON = null;
                try {
                    reservationDataJSON = obj.getJSONArray("MenuData");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray reservationDataJSON2 = new JSONArray();

                ArrayList<JSONObject> jsList = new ArrayList<JSONObject>();
                int k;
                for (k = 0; k < reservationDataJSON.length(); k++) {
                    try {
                        jsList.add(reservationDataJSON.getJSONObject(k));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("IOqualcosa", "modified OBJECT" + modifiedObject.toString());
                //  jsList.add(addedObject);
                //reservationDataJSON.remove(position);
                // jsList.remove(position);

                for (k = 0; k < jsList.size(); k++) {
                    try {
                        if(k==posizione)
                            reservationDataJSON2.put(k,modifiedObject);
                        else
                            reservationDataJSON2.put(k, jsList.get(k));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    obj2.put("MenuData", (Object) reservationDataJSON2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // logs for debugging
                Log.i("IOqualcosa", "OLD OBJECT" + obj.toString());
                // Log.i("IOqualcosa", "OLD POSITION IS" + position);
                Log.i("IOqualcosa", "NEW OBJECT" + obj2.toString());

                SharedPreferences.Editor editor = app_preferences.edit();

                editor.putString("jsonMenu", obj2.toString());
                editor.commit();
                finish();
       /*
        mData.add(mData.get(mData.size()-1));


        notifyItemRangeInserted(mData.size()-1,mData.size());

*/

            }
        }
        else
        {
            Toast.makeText(this,"Completa il form",Toast.LENGTH_LONG).show();
        }
    }

    public void nonAggiungere(View view)
    {
        finish();
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("MenuData.json");

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




    public void selectImage(View view) {
        final CharSequence[] items = { this.getString(R.string.alertOpt1), this.getString(R.string.alertOpt2), this.getString(R.string.alertOpt3) };
        AlertDialog.Builder builder = new AlertDialog.Builder(FoodGenerator.this);
        builder.setTitle(this.getString(R.string.addPhoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getBaseContext().getString(R.string.alertOpt1))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals(getBaseContext().getString(R.string.alertOpt2))) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals(getBaseContext().getString(R.string.alertOpt3))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    boolean onActivityResultCalled=false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivityResultCalled=true;
        Log.i("onactivityresult", "   invoked");
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_CAMERA) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;

                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    immagine.setImageBitmap(thumbnail);
                    Log.i("CODIFICA", "  case=camera");


                    encodedImage=new String(encodeTobase64(  thumbnail));
                    Log.i("CODIFICA",encodedImage);

                } else if (requestCode == SELECT_FILE) {
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri.toString().contains("images")) {
                        String[] projection = {MediaStore.MediaColumns.DATA};
                        android.support.v4.content.CursorLoader cursorLoader = new android.support.v4.content.CursorLoader(this, selectedImageUri, projection, null, null, null);
                        Cursor cursor = cursorLoader.loadInBackground();
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        cursor.moveToFirst();
                        String selectedImagePath = cursor.getString(column_index);
                        Bitmap bm;
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(selectedImagePath, options);
                        final int REQUIRED_SIZE = 200;
                        int scale = 1;
                        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                            scale *= 2;
                        options.inSampleSize = scale;
                        options.inJustDecodeBounds = false;
                        bm = BitmapFactory.decodeFile(selectedImagePath, options);
                        Bitmap orientedBitmap = ExifUtil.rotateBitmap(selectedImagePath, bm);
                        immagine.setImageBitmap(orientedBitmap);
                        Log.i("CODIFICA", "  case=select file");
                        encodedImage=new String(encodeTobase64(orientedBitmap) );

                        Log.i("CODIFICA",encodedImage);
                    }
                } else {
                    Toast.makeText(FoodGenerator.this, "data null", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("onsaveinstancestate","invoked");

        if(outState!=null) {
            outState.putString("nome", (nome.getText().toString()));

            outState.putString("quantità", quantità.getText().toString());


            outState.putString("prezzo", prezzo.getText().toString());
            outState.putString("descrizione", descrizione.getText().toString());
            outState.putString("immagine", encodedImage);

       /*     BitmapDrawable drawable = (BitmapDrawable) immagine.getDrawable();
            Bitmap image = drawable.getBitmap();
            outState.putParcelable("img", image);*/
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("onrestoreinstancestate", "   invoke");
        encodedImage=savedInstanceState.getString("immagine", null);
        ImageView imgView = (ImageView) findViewById(R.id.imgView);
        imageDefault();
        if(encodedImage!=null)  imgView.setImageBitmap(decodeBase64(encodedImage));


        nome.setText(savedInstanceState.getString("nome"));


        quantità.setText(savedInstanceState.getString("quantità"));


        descrizione.setText(savedInstanceState.getString("descrizione"));
        prezzo.setText(savedInstanceState.getString("prezzo"));

    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orders) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        } else if(id == R.id.nav_tables) {
            Intent intent = new Intent(this,TablesReservation.class);
            startActivity(intent);
        }else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this,Profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_menu) {
            Intent intent = new Intent(this,Menu_Activity_Fragment.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
