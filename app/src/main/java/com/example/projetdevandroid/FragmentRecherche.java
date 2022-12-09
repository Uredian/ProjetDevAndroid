package com.example.projetdevandroid;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class FragmentRecherche extends Fragment {
    public ImageService imageService;
    public ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recherche, container, false);
        Log.d("FragmentRecherche","onCreateView");

        imageView = rootView.findViewById(R.id.imageViewRecherche);

        EditText editText = rootView.findViewById(R.id.inputField);
        editText.setOnKeyListener((v, keyCode, event) -> {
            if (((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))) {
                getEditText(editText);
                return true;
            }
            return false;
        });

        Button searchButton = rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditText(editText);
            }
        });


        return rootView;
    }

    public void getEditText(EditText editText){
        Log.d("SEARCH BUTTON", "onKey: "+ editText.getText().toString());
        String search = editText.getText().toString();
        editText.getText().clear();
        Intent intent = new Intent(getActivity(), ImageService.class);
        intent.putExtra("url","https://www.hartz.com/wp-content/uploads/2022/04/small-dog-owners-1.jpg");
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

    public void notifyImage(Bitmap bitmap){
        imageView.post(()->{
            imageView.setImageBitmap(bitmap);
        });
    }


}