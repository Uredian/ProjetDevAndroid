package com.example.projetdevandroid;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public MyDatabase myDatabase;
    public FragmentManager monManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Projet Android");

        myDatabase = new MyDatabase(this);
        monManager = getSupportFragmentManager();

        //Lorsqu'on lance l'application, on se retrouve directement sur le fragment de recherche
        FragmentTransaction maTransaction = monManager.beginTransaction();
        maTransaction.add(R.id.fragContainer, new FragmentRecherche(myDatabase), null).commit();

        //L'activité écoute tout changement sur les préférences partagées
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        loadColorFromPreference(sharedPreferences);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Cette méthode sert à ajouter les options du menu dans l'ActionBar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Lorsque l'utilisateur clique sur un élément du menu, cette méthode est exécutée
        switch (item.getItemId()) {
            case R.id.action_preferences:
                //Show the preferences fragment
                monManager.beginTransaction()
                        .replace(R.id.fragContainer, new FragmentPreferences(), null)
                        .commit();
                return true;
            case R.id.action_favorites:
                //Show the favorites fragment
                monManager.beginTransaction()
                        .replace(R.id.fragContainer, new FragmentFavoris(myDatabase), null)
                        .commit();
                return true;
            case R.id.action_recherche:
                //Show the search fragment
                monManager.beginTransaction()
                        .replace(R.id.fragContainer, new FragmentRecherche(myDatabase), null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadColorFromPreference(SharedPreferences sharedPreferences) {
        //Cette méthode change récupère la couleur définie dans les préférences partagées
        Log.d("MAIN ACTIVITY : ", sharedPreferences.getString("preferedColour", "gris"));
        changeActionBarColor(sharedPreferences.getString("preferedColour", "gris"));
    }

    private void changeActionBarColor(String color) {
        //On change la couleur de l'ActionBar en fonction de la couleur donnée en paramètre
        switch (color) {
            case "rouge":
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
                break;
            case "bleu":
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                break;
            case "vert":
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                break;
            case "gris":
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                break;
            default:
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /*Puisque la seule préférence partagée est la couleur de l'ActionBar, on n'a pas besoin
         ** ici de faire un if/else sur la clé key pour connaître la méthode à éxecuter
         */
        loadColorFromPreference(sharedPreferences);
    }
}