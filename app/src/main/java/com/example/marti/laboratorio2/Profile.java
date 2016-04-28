package com.example.marti.laboratorio2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView restname;
    private TextView address;
    private TextView category;
    private TextView opening;
    private TextView closing;
    private TextView phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manager);
        Button btn = (Button) findViewById(R.id.goGallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.nav_profile));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        restname = (TextView) findViewById(R.id.s_restname);
        address = (TextView) findViewById(R.id.s_address);
        category = (TextView) findViewById(R.id.s_category);
        opening = (TextView) findViewById(R.id.s_opening);
        closing = (TextView) findViewById(R.id.s_closing);
        phone = (TextView) findViewById(R.id.s_phone);

    }

     public void goTo() {
         Intent intent = new Intent(this,Profile_Gallery.class);
         startActivity(intent);
     }

    @Override
    protected void onStart() {
        super.onStart();

        readPrefs();

        Bundle extras = getIntent().getExtras();
        if(extras!= null){

            SharedPreferences prefs2 = getSharedPreferences("preferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs2.edit();

            restname.setText(extras.getString("restname"));
            address.setText(extras.getString("address"));
            category.setText(extras.getString("category"));
            opening.setText(extras.getString("opening"));
            closing.setText(extras.getString("closing"));
            phone.setText(extras.getString("phone"));

            //dopo che hai recuperato le scritte dall'intent e le hai settate nelle view, salvati nelle shared preferences
            if(prefs2 != null) {
                editor.putString("restname", extras.getString("restname"));
                editor.putString("address", extras.getString("address"));
                editor.putString("category", extras.getString("category"));
                editor.putString("opening", extras.getString("opening"));
                editor.putString("closing", extras.getString("closing"));
                editor.putString("phone", extras.getString("phone"));
                editor.apply();
            }

        }
    }

    public void readPrefs () {
        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        String rName = prefs.getString("restname", restname.getText().toString());
        String rAddress = prefs.getString("address", address.getText().toString());
        String rCategory = prefs.getString("category", category.getText().toString());
        String rOpening = prefs.getString("opening", opening.getText().toString());
        String rClosing = prefs.getString("closing", closing.getText().toString());
        String rPhone = prefs.getString("phone", phone.getText().toString());


        if (prefs != null) {
            restname.setText(rName);
            address.setText(rAddress);
            category.setText(rCategory);
            opening.setText(rOpening);
            closing.setText(rClosing);
            phone.setText(rPhone);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_manager_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();  //prende l'id dell'elemento sulla toolbar premuto

        if (id == R.id.editbutton) {
            Intent intent= new Intent(this,editProfile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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

        } else if (id == R.id.nav_menu) {
            Intent intent = new Intent(this,Menu_Activity_Fragment.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
