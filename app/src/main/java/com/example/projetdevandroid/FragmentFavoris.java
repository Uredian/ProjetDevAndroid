package com.example.projetdevandroid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Map;

public class FragmentFavoris extends Fragment {
    public View rootView;
    public MyDatabase myDatabase;

    public FragmentFavoris(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favoris, container, false);
        Log.d("FragmentFavoris", "onCreateView");

        Button refreshButton = rootView.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFavorites();
            }
        });

        //On charge les images stockées dans la bdd une première fois
        getFavorites();
        return rootView;
    }

    public void getFavorites() {
        //On récupère les images ainsi que leur clé primaire et on les passe à l'Adapter
        Map<Integer, Bitmap> bitmaps = myDatabase.readDatabase();
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        ArrayList<Integer> integerArrayList = new ArrayList<>();

        for (Integer key : bitmaps.keySet()) {
            bitmapArrayList.add(bitmaps.get(key));
            integerArrayList.add(key);
        }

        ListView listView = rootView.findViewById(R.id.listViewFavorites);
        FavoriteAdapter imageAdapter = new FavoriteAdapter(listView.getContext(),
                bitmapArrayList,
                integerArrayList,
                myDatabase);
        listView.post(() -> listView.setAdapter(imageAdapter));

    }
}