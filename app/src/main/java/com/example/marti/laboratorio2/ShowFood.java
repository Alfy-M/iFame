package com.example.marti.laboratorio2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.io.ByteArrayOutputStream;

public class ShowFood extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    TextView nome;
    TextView quantità;
    TextView descrizione;
    TextView prezzo;
    ImageView immagine;
    String encodedImage=new String();
    int posizione;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food);
        savedInstanceState = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.showFood));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        nome = (TextView) findViewById(R.id.textName);
        quantità = (TextView) findViewById(R.id.textQuantity);
        descrizione = (TextView) findViewById(R.id.editDescription);
        prezzo=(TextView) findViewById(R.id.editPrice);
        immagine= (ImageView) findViewById(R.id.imgView);
        if (savedInstanceState != null) {
            nome.setText(savedInstanceState.getString("Nome", null));
            quantità.setText(savedInstanceState.getString("Quantità", null));
            descrizione.setText(savedInstanceState.getString("Descrizione", null));
            prezzo.setText(savedInstanceState.getString("Prezzo", null));
            posizione=savedInstanceState.getInt("posizione",0);
            encodedImage=savedInstanceState.getString("Immagine");
            if(encodedImage==null)
                encodedImage=new String(encodeTobase64(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.food)));
            immagine.setImageBitmap(decodeBase64(encodedImage));
        }

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();  //prende l'id dell'elemento sulla toolbar premuto

        if (id == R.id.editfood) {
            Bundle bundle = new Bundle();
            bundle.putString("nome",nome.getText().toString());
            bundle.putString("quantita", quantità.getText().toString());
            bundle.putString("descrizione", descrizione.getText().toString());
            bundle.putString("prezzo", prezzo.getText().toString());
            bundle.putString("immagine", encodedImage);
            bundle.putString("chiamante","showfood");
            bundle.putInt("posizione",posizione);
            Intent intent = new Intent(this,FoodGenerator.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_orders) {
            intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_tables) {
            intent = new Intent(this,TablesReservation.class);
            startActivity(intent);
        }else if (id == R.id.nav_profile) {
            intent = new Intent(this,Profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_menu) {
             intent = new Intent(this,Menu_Activity_Fragment.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; questo aggiunge degli elementi alla toolbar se sono presenti
        getMenuInflater().inflate(R.menu.showfoodtool, menu);
        return true;

    }
}
