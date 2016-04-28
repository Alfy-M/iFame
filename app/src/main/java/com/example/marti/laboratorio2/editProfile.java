package com.example.marti.laboratorio2;

import android.app.TimePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class editProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText restname;
    private EditText address;
    private Spinner category;
    private Button opening;
    private Button closing;
    private EditText phone;
    String openingtime;
    String closingtime;
    int hour;
    int minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.nav_profile));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        restname = (EditText) findViewById(R.id.e_restname);
        address = (EditText) findViewById(R.id.e_address);
        category = (Spinner) findViewById(R.id.e_category);
        opening = (Button) findViewById(R.id.e_opening);
        closing = (Button) findViewById(R.id.e_closing);
        phone = (EditText) findViewById(R.id.e_phone);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        category.setAdapter(adapter);
        //appena l'activity viene creata leggiti le shared preferences
        readPrefs();
    }

    public void setOpeningTime(View v){

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(editProfile.this,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                openingtime=selectedHour+":"+selectedMinute;
                opening.setText(openingtime);
            }
        },hour,minute,true);

        mTimePicker.setTitle("Pick Time");
        mTimePicker.show();
    }

    public void setClosingTime(View v){

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(editProfile.this,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                closingtime=selectedHour+":"+selectedMinute;
                closing.setText(closingtime);
            }
        },hour,minute,true);

        mTimePicker.setTitle("Pick Time");
        mTimePicker.show();

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
        // Inflate the menu; questo aggiunge degli elementi alla toolbar se sono presenti
        getMenuInflater().inflate(R.menu.edit_profile_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();  //prende l'id dell'elemento sulla toolbar premuto

        if (id == R.id.savebutton) {
            //per passare le scritte all'altra activity quando si clicca il bottone salva
            Intent intent= new Intent(this,Profile.class);
            Bundle bundle = new Bundle ();
            bundle.putString("restname",restname.getText().toString());
            bundle.putString("address",address.getText().toString());
            bundle.putString("category",String.valueOf(category.getSelectedItem()));
            bundle.putString("opening",opening.getText().toString());
            bundle.putString("closing",closing.getText().toString());
            bundle.putString("phone",phone.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        if (id==R.id.photomanager){
            Intent intent= new Intent(this,Profile_Gallery.class);
            startActivity(intent);
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
    public void readPrefs () {


        ArrayAdapter myAdap = (ArrayAdapter) category.getAdapter(); //cast to an ArrayAdapter

        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        String rName = prefs.getString("restname", restname.getText().toString());
        String rAddress = prefs.getString("address", address.getText().toString());
        String rCategory = prefs.getString("category","Category");
        String rOpening = prefs.getString("opening", opening.getText().toString());
        String rClosing = prefs.getString("closing", closing.getText().toString());
        String rPhone = prefs.getString("phone", phone.getText().toString());

        int spinnerPosition = myAdap.getPosition(rCategory);


        if (prefs != null) {
            restname.setText(rName);
            address.setText(rAddress);
            category.setSelection(spinnerPosition);
            opening.setText(rOpening);
            closing.setText(rClosing);
            phone.setText(rPhone);
        }
    }
}
