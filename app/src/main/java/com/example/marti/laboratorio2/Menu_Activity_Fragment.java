package com.example.marti.laboratorio2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;


import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.util.List;

public class Menu_Activity_Fragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,Menu_Fragment.OnButtonPressListener{
private RecyclerAdapterMenu myAdapter;
    private String myString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu__activity__fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.nav_menu));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    public String getMyData() {
        return myString;
    }

    @Override
    public void onButtonPressed(String position,String name, String quantity, String price,String description, String image) {
        //Log.i("Prova1",pos);
        Show_Order_Fragment obj = (Show_Order_Fragment)getSupportFragmentManager().findFragmentById(R.id.ordini);
        if ((obj!=null)&&obj.isInLayout()) {
            obj.setText(name, quantity, price, description, image);
        } else {
            Intent intent = new Intent(this,ShowFood.class);
            intent.putExtra("Nome",name);
            intent.putExtra("Quantità",quantity);
            intent.putExtra("Descrizione", description);
            intent.putExtra("posizione",position);
            intent.putExtra("Immagine",image);
            intent.putExtra("Prezzo",price);
            startActivity(intent);
        }
    }
    @Override
    public void onButtonPressedDelete(Boolean check){
        if (check) {
            Show_Order_Fragment obj = (Show_Order_Fragment)getSupportFragmentManager().findFragmentById(R.id.ordini);
            obj.setText("", "", "", "", "");
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
        // Inflate the menu; questo aggiunge degli elementi alla toolbar se sono presenti

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(),"Ciao",Toast.LENGTH_SHORT);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("testo",newText);
                Menu_Fragment articleFrag = (Menu_Fragment)
                        getSupportFragmentManager().findFragmentById(R.id.menuFragment);
                articleFrag.filterRecycler(newText);
                /*Menu_Fragment newFragment = new Menu_Fragment();
                Bundle args = new Bundle();
                args.putString("filter",newText);
*/
                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();  //prende l'id dell'elemento sulla toolbar premuto

        if (id == R.id.savebutton) {
            Intent intent=new Intent(getApplicationContext(),FoodGenerator.class);
            intent.putExtra("chiamante","button");
            //intent.putExtra("wewe","valore");
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    //Implemento il Navigation Drawer per l'activity contenitore dei fragments
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orders) {
            Intent intent = new Intent(this,TablesReservation.class);
            startActivity(intent);
        } else if(id == R.id.nav_tables) {
            Intent intent = new Intent(this,TablesReservation.class);
            startActivity(intent);
        }else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this,Profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_menu) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
