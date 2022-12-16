package com.example.projetdevandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends ArrayAdapter<Bitmap> {


    public ImageAdapter(@NonNull Context context, @NonNull ArrayList<Bitmap> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Bitmap bitmap = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_view, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        imageView.post(() -> imageView.setImageBitmap(bitmap));
        return convertView;
    }
}
