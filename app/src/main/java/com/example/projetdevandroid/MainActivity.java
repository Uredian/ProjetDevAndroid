package com.example.projetdevandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager monManager = getSupportFragmentManager();
        FragmentTransaction maTransaction = monManager.beginTransaction();
        maTransaction.add(R.id.fragContainer, new FragmentRecherche(),null).commit();

        Button boutonFragmentRecherche = findViewById(R.id.boutonRecherche);
        Button boutonFragmentFavoris = findViewById(R.id.boutonFavoris);

        boutonFragmentRecherche.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                monManager.beginTransaction().replace(R.id.fragContainer, new FragmentRecherche(),null).commit();
            }
        });

        boutonFragmentFavoris.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                monManager.beginTransaction().replace(R.id.fragContainer, new FragmentFavoris(),null).commit();
            }
        });

    }
}