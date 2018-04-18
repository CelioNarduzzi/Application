package com.example.narduzzice.jungle2;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by OsamaArshad on 07-Nov-16.
 */
public class HTTP_Handler {
    private static final String classtag= HTTP_Handler.class.getSimpleName();  //return name of underlying class

    public String makeHTTPCall(String URLinput) { // method which will request to server when provided with url
        String response = null;
        try {
            URL url = new URL(URLinput);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //to open connection with url
            conn.setRequestMethod("GET");   //request type is of GET, which means to get information from the server

            InputStream in = new BufferedInputStream(conn.getInputStream()); //storing the input stream we are getting for the url
            response = InputStreamToString(in); //now storing string from the input stream
        } catch (MalformedURLException e) {
            Log.e(classtag, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(classtag, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(classtag, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(classtag, "Exception: " + e.getMessage());
        }
        return response; //returning whatever we fetching from the string to the method.
    }
    private String InputStreamToString(InputStream is) {   //fetching string form the input stream
        BufferedReader br = new BufferedReader(new InputStreamReader(is)); //it will read form stream
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n'); //whatever we read from the stream, we will append it to the stringbuilder
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public static Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(HTTP_Handler.class.getSimpleName(), "Error getting bitmap", e);
        }
        return bm;
    }
}
