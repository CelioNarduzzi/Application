package com.example.narduzzice.jungle2;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import android.widget.ImageView;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class Decouvrir extends AppCompatActivity {

    private String classtag= Decouvrir.class.getSimpleName();  //return name of underlying class
    private ListView lv;
    private ProgressDialog progress;
    private String url="http://157.26.160.33:82/Jungle/public/api/v1/categorie"; //passing url
    ArrayList<HashMap<String,String>> studentslist; //arraylist to save key value pair from json
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decouvrir);
        studentslist=new ArrayList<>();
        lv= (ListView) findViewById(R.id.list); //from home screen list view
        new Decouvrir.getStudents().execute(); // it will execute your AsyncTask
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ProgressDialog progress ;

        @Override     protected void onPreExecute()
        {
            progress = ProgressDialog.show(Decouvrir.this,"Chargememt…",
                    "L'icon météo est en cours de chargement");
        }

        protected Bitmap doInBackground(String... args)
        {
            String url = args[0];
            return HTTP_Handler.getImageBitmap(url);
        }

        protected void onPostExecute(Bitmap bitmap)
        {
                        ImageView imageView = (ImageView) findViewById(R.id.List_Image);
            imageView.setImageBitmap(bitmap);
            progress.dismiss();
        }
    }

    //--------------------------//-------------------------------//---------------------//
    public class getStudents extends AsyncTask<Void,Void,Void> {
        protected void onPreExecute(){
            super.onPreExecute(); //it will use pre defined preExecute method in async task
            progress=new ProgressDialog(Decouvrir.this);
            progress.setMessage("Fetching JSON.,."); // show what you want in the progress dialog
            progress.setCancelable(false); //progress dialog is not cancellable here
            progress.show();
        }
        protected Void doInBackground(Void...arg0){
            HTTP_Handler hh = new HTTP_Handler(); // object of HTTP_Handler
            String jString = hh.makeHTTPCall(url); //calling makeHTTPCall method and string its response in a string
            Log.e(classtag, "Response from URL: " + jString);
            if (jString != null) {
                try {
                    JSONObject jObj = new JSONObject(jString); //our json data starts with the object
                    JSONArray students = jObj.getJSONArray("categorie"); //fetch array from studentsinfo object
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(i); //get object from i index
                        String id=student.getString("id");   //save string from variable 'id' to string
                        String name=student.getString("name");
                        String image=student.getString("image");


                        HashMap<String, String> studentdata = new HashMap<>(); //create a hash map to set key and value pair

                        studentdata.put("id", id); //hash map will save key and its value
                        studentdata.put("name", name);
                        studentdata.put("image", image);
                        studentslist.add(studentdata); //now save all of the key value pairs to arraylist
                    }
                } catch (final JSONException e) {
                    Log.e(classtag, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show(); //show if you catch any exception with data
                        }
                    });
                }
            } else {
                Log.e(classtag, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check internet connection!",
                            Toast.LENGTH_LONG).show();//show if you are unable to connect with the internet or if jString is null
                    }
                });
            }
            return null;
        }
        protected void onPostExecute(Void Result){
            super.onPostExecute(Result);
            if(progress.isShowing()){
                progress.dismiss();
            }
            ListAdapter adapter=new SimpleAdapter(
                    Decouvrir.this,
                    studentslist,
                    R.layout.bucket_list,
                    new String[]{"id","name","image"},
                    new int[]{R.id.List_Id,R.id.List_Name,R.id.List_Image});
                    new DownloadImageTask().execute(("image"));

            lv.setAdapter(adapter);
//            SimpleAdapter (Context context,                   //
//                    List<? extends Map<String, ?>> data,      //
//            int resource,                                     //
//            String[] from,                                    //
//            int[] to)                                         //
//this will pass your json values to the bucket_list xml and whatever it is stored of key 'name' will be
// displayed to text view list_Name
        }
    }
}

