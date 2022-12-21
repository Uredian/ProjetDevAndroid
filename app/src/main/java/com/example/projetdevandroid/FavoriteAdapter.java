package com.example.projetdevandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FavoriteAdapter extends ArrayAdapter<Bitmap> {
    public MyDatabase myDatabase;
    public ArrayList<Integer> keys;

    public FavoriteAdapter(@NonNull Context context, @NonNull ArrayList<Bitmap> objects, ArrayList<Integer> keys, MyDatabase myDatabase) {
        super(context, 0, objects);
        this.myDatabase = myDatabase;
        this.keys = keys;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /*On récupère l'image sous format Bitmap ainsi que sa clé primaire pour pouvoir la
         ** supprimer de la bdd en cliquant sur son bouton associé
         */
        Bitmap bitmap = getItem(position);
        Integer pkey = keys.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorite_view, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.favorite_view);
        imageView.post(() -> imageView.setImageBitmap(bitmap));

        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase.deleteImage(pkey);
                deleteButton.setClickable(false);
                deleteButton.setEnabled(false);
                deleteButton.setText("Favori Supprimé");

            }
        });

        return convertView;
    }
}
