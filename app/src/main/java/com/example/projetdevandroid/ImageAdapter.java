package com.example.projetdevandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<Bitmap> {
    public MyDatabase myDatabase;

    public ImageAdapter(@NonNull Context context, @NonNull ArrayList<Bitmap> objects, MyDatabase myDatabase) {
        super(context, 0, objects);
        this.myDatabase = myDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //On récupère l'image et on l'ajoute à la listView accompagnée d'un bouton d'ajout aux favoris
        Bitmap bitmap = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_view, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        imageView.post(() -> imageView.setImageBitmap(bitmap));

        ImageButton imageButton = convertView.findViewById((R.id.imageButton));
        imageButton.setTag(android.R.drawable.btn_star_big_off);

        /*On ajoute un tag au bouton pour savoir dans quel état il est afin de changer son image
         ** et aussi de pouvoir ajouter ou retirer l'image de la bdd
         */
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer resource = (Integer) imageButton.getTag();
                if (resource == android.R.drawable.btn_star_big_off) {
                    imageButton.setImageResource(android.R.drawable.btn_star_big_on);
                    //On insert l'image dans la bdd et on tag le bouton de la clé primaire
                    Long pkey = myDatabase.insertData(bitmap);
                    imageButton.setTag(pkey.intValue());

                } else {
                    imageButton.setImageResource(android.R.drawable.btn_star_big_off);
                    /*On supprime l'image associée à la clé primaire et on met le logo desactivé
                     ** dans le tag
                     */
                    imageButton.setTag(android.R.drawable.btn_star_big_off);
                    myDatabase.deleteImage(resource);
                }

            }
        });

        return convertView;
    }
}
