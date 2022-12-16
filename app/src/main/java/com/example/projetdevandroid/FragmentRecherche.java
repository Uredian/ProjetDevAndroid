package com.example.projetdevandroid;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentRecherche extends Fragment {
    public ImageService imageService;
    public ImageView imageView;
    public View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recherche, container, false);
        Log.d("FragmentRecherche","onCreateView");

        imageView = rootView.findViewById(R.id.image_view);

        EditText editText = rootView.findViewById(R.id.inputField);
        editText.setOnKeyListener((v, keyCode, event) -> {
            if (((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))) {
                try {
                    getEditText(editText);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });

        Button searchButton = rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getEditText(editText);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });


        return rootView;
    }

    public void getEditText(EditText editText) throws MalformedURLException {
        Log.d("SEARCH BUTTON", "onKey: "+ editText.getText().toString());
        String keyWord = editText.getText().toString();
        String urlString = "https://www.flickr.com/services/feeds/photos_public.gne?tags="+keyWord+"&format=json";
        editText.getText().clear();
        Intent intent = new Intent(getActivity(), ImageService.class);
        intent.putExtra("url",urlString);
        getActivity().bindService(intent, connection, BIND_AUTO_CREATE);
        getActivity().startService(intent);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("MainActivity","onServiceConnected");
            ImageService.MonBinder binder = (ImageService.MonBinder) service;
            imageService = binder.getService();
            imageService.setMain(FragmentRecherche.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    public void notifyImage(List<Bitmap> bitmaps){
        ListView listView = rootView.findViewById(R.id.listViewRecherche);
        ImageAdapter imageAdapter = new ImageAdapter(listView.getContext(),new ArrayList<>(bitmaps));
        listView.post(() -> listView.setAdapter(imageAdapter));
    }


}