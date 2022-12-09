package com.example.projetdevandroid;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

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
        Bundle extras = intent.getExtras();
        if(extras.containsKey("url")){
            try {
                URL url = new URL(extras.getString("url"));
                new Thread(() -> {
                    try {
                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        fragmentRecherche.notifyImage(bitmap);
                        String test = readUrlContent(new URL("https://www.flickr.com/services/feeds/photos_public.gne?tags=cats&format=json"));
                        JSONObject json = new JSONObject(test);
                        Log.d("IMAGE SERVICE", "JSON FROM URL: "+json);
                        List<String> linkList = parseJson(json);
                        for (String link: linkList) {
                            bitmap = BitmapFactory.decodeStream(new URL(link).openConnection().getInputStream());
                            fragmentRecherche.notifyImage(bitmap);
                        }



                    } catch(IOException e) {
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
        JSONArray jsonArray = json.getJSONArray("items");
        List<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            item = item.getJSONObject("media");
            Log.d("IMAGE SERVICE", "link for item: "+ item.getString("m"));
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
        result = result.substring(15,result.length()-1);
        return result;
    }

}