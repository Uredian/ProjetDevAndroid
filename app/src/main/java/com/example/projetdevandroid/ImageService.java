package com.example.projetdevandroid;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ImageService extends Service {
    private final IBinder binder = new MonBinder();
    public Bitmap bitmap;
    public FragmentRecherche fragmentRecherche;

    public void setMain(FragmentRecherche fragmentRecherche) {
        this.fragmentRecherche = fragmentRecherche;
    }

    public class MonBinder extends Binder {
        ImageService getService() {
            return ImageService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void onCreate() {
        // Création du service
    }

    public void onDestroy() {
        // Destruction du service
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //On récupère l'url disponible dans l'intent
        Bundle extras = intent.getExtras();
        if (extras.containsKey("url")) {
            try {
                URL url = new URL(extras.getString("url"));
                //On crée un Thread qui envoie les images récupérées au fragment pour les afficher
                new Thread(() -> {
                    try {
                        String test = readUrlContent(url);
                        JSONObject json = new JSONObject(test);
                        Log.d("IMAGE SERVICE", "JSON FROM URL: " + json);

                        List<String> linkList = parseJson(json);
                        List<Bitmap> bitmaps = new ArrayList<>();
                        for (String link : linkList) {
                            bitmap = BitmapFactory.decodeStream(new URL(link).openConnection().getInputStream());
                            bitmaps.add(bitmap);
                        }

                        fragmentRecherche.notifyImage(bitmaps);

                    } catch (IOException e) {
                        System.out.println(e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return START_REDELIVER_INTENT;
    }

    public List<String> parseJson(JSONObject json) throws JSONException {
        //On récupère l'ensemble des urls d'image retourné par la requête initiale
        JSONArray jsonArray = json.getJSONArray("items");
        List<String> result = new ArrayList<>();

        //On parse l'objet selon le format donné par flickr
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            item = item.getJSONObject("media");
            Log.d("IMAGE SERVICE", "link for item: " + item.getString("m"));
            result.add(item.getString("m"));
        }

        return result;
    }

    public String readUrlContent(URL url) throws IOException {
        InputStream is = url.openStream();
        Reader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        String result = sb.toString();
        result = result.substring(15, result.length() - 1);
        return result;
    }

}