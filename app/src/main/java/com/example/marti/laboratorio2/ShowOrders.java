package com.example.marti.laboratorio2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;


/*
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

         String     JsonString = app_preferences.getString("json", loadJSONFromAsset());
        Log.i("PREFERENCES", JsonString);*/

/*
        //Layout
        JSONObject obj= null;
        JSONArray reservationData=null;
        try {
            obj = new JSONObject(JsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reservationData=obj.getJSONArray( "reservationData");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


public class ShowOrders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.order));

        setSupportActionBar(toolbar);


        ArrayList<TextView> menuList=new ArrayList<TextView>();
        ArrayList<TextView> quantityList=new ArrayList<TextView>();
        ArrayList<Integer> menuIds=new ArrayList<Integer>();
        ArrayList<Integer> quantityIds=new ArrayList<Integer>();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView time = (TextView) findViewById(R.id.time);


      /*
        TextView menuitem1 = (TextView) findViewById(R.id.menu1);
        TextView menuitem2 = (TextView) findViewById(R.id.menu2);
        TextView menuitem3 = (TextView) findViewById(R.id.menu3);
        TextView quantita1 = (TextView) findViewById(R.id.quantity1);
        TextView quantita2 = (TextView) findViewById(R.id.quantity2);
        TextView quantita3 = (TextView) findViewById(R.id.quantity3);*/


        TextView nome = (TextView) findViewById(R.id.nome);
        TextView cognome = (TextView) findViewById(R.id.cognome);
        TextView email = (TextView) findViewById(R.id.email);
        TextView telefono = (TextView) findViewById(R.id.telefono);
        Bundle extras = getIntent().getExtras();


        time.setText(extras.getString("Orario"));
        /*menuitem1.setText("- "+listaOrdini.get(0));
        menuitem2.setText("- "+listaOrdini.get(1));
        menuitem3.setText("- "+listaOrdini.get(2));
        quantita1.setText("("+listaQuantita.get(0).toString()+")");
        quantita2.setText("("+listaQuantita.get(1).toString()+")");
        quantita3.setText("("+listaQuantita.get(2).toString()+")");*/
        //note.setText("Note: "+extras.getString("Note"));
        nome.setText(extras.getString("Nome"));
        cognome.setText(extras.getString("Cognome"));
        email.setText(extras.getString("Email"));
        telefono.setText(extras.getString("Telefono"));





        //View contentView = mInflater.inflate(R.layout.activity_main, null);
        LinearLayout root = (LinearLayout) this .findViewById(R.id.orderMenu);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout rootQuantity = (LinearLayout) this .findViewById(R.id.orderQuantity);
        rootQuantity.setOrientation(LinearLayout.VERTICAL);



//        TextView menuTextView = new TextView(this);
        //      menuTextView.setText("c elai fattah");
        //   root.addView(menuTextView);

        ArrayList<String> listaOrdini=extras.getStringArrayList("Ordini");
        ArrayList<Integer> listaQuantita=extras.getIntegerArrayList("Quantita");
        int size=(listaOrdini!=null)? listaOrdini.size():0;
        for(int k=0; k<size+1;k++)
        {
            TextView menuTextView = new TextView(this);
            TextView quantityTextView = new TextView(this);

            int menuID;
            int quantityID;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                menuID=generateViewId();
                quantityID=generateViewId();

            } else
            {

                menuID=View.generateViewId();
                quantityID=generateViewId();
            }
            if(k<size)
            {
                menuTextView.setId(menuID);
                menuTextView.setText(listaOrdini.get(k));
                menuTextView.setTypeface(null, Typeface.ITALIC);
                menuTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                quantityTextView.setId(quantityID);
                quantityTextView.setText(listaQuantita.get(k).toString());
                quantityTextView.setGravity(Gravity.END);
                quantityTextView.setTypeface(null, Typeface.ITALIC);
                quantityTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            }
            else
            {
                menuTextView.setId(menuID);
                menuTextView.setText("Note: " + extras.getString("Note"));
                menuTextView.setTypeface(null, Typeface.ITALIC);
                menuTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                menuTextView.setPadding(0, 15, 0, 0);


            }

            root.addView(menuTextView);
            rootQuantity.addView(quantityTextView);


        }

    }

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
       */
       /* return super.onOptionsItemSelected(item);
    }
*/
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
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("reservationData.json");

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



}

