package com.example.projetdevandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FragmentFavoris extends Fragment {

    public FragmentFavoris(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favoris, container, false);
        Log.d("FragmentFavoris","onCreateView");
        return rootView;
    }
}