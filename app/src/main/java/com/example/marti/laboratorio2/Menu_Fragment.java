package com.example.marti.laboratorio2;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alfonsomartino on 18/04/16.
 */
public class Menu_Fragment extends Fragment  {
    RecyclerView recyclerView;
    RecyclerAdapterMenu adapter;
    String JsonObject;
    OnButtonPressListener buttonListener;
    OnButtonPressListener buttonListenerDelete;
    private List<MenuData> mData;

    public interface OnButtonPressListener {
        public void onButtonPressed(String position, String name, String quantity, String price,String description, String image);
        public void onButtonPressedDelete(Boolean check);

    }



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
        //Faccio tutto quello che c'e nell' OnCreate dell'Activity vecchia
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        JsonObject = app_preferences.getString("jsonMenu",loadJSONFromAsset());
        View view = inflater.inflate(R.layout.frag_menu,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            buttonListener = (OnButtonPressListener)getActivity();
            buttonListenerDelete = (OnButtonPressListener)getActivity();
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+"must implement onButtonPressed");
        }
    }

    @Override
    public void onViewCreated(View view , Bundle savedInstanceState) {
        setUpRecyclerView();
    }

    public void filterRecycler(String query) {
        try {
            mData = MenuData.getData(JsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final List<MenuData> filteredModelList = filter(mData, query);
        adapter.setFilter(filteredModelList);
    }

    private List<MenuData> filter(List<MenuData> models, String query) {
        query = query.toLowerCase();

        final List<MenuData> filteredModelList = new ArrayList<>();
        for (MenuData model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void setUpRecyclerView () {
        adapter = null;
        try {
            adapter = new RecyclerAdapterMenu(getActivity(), MenuData.getData(JsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(adapter);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        adapter.SetOnItemClickListener(new RecyclerAdapterMenu.OnItemClickListener() {

            @Override
            public void onItemClick(View view,String position,String name, String quantity, String price,String description, String image) {
                Log.i("Prova dentro",name);
                buttonListener.onButtonPressed(position, name, quantity, price, description, image);
            }


        });
        adapter.SetOnItemClickListenerDelete(new RecyclerAdapterMenu.OnDeleteClickListener() {
            @Override
            public void onItemClickDelete(View view, Boolean check) {
                Log.i("Prova delete",String.valueOf(check));
                buttonListenerDelete.onButtonPressedDelete(check);
            }
        });



    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        JsonObject = app_preferences.getString("jsonMenu",loadJSONFromAsset());
        setUpRecyclerView();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("MenuData.json");
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
