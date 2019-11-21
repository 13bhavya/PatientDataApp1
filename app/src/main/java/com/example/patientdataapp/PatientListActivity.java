package com.example.patientdataapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PatientListActivity extends AppCompatActivity {

    /* Member variables */
    final private int REQUEST_INTERNET = 123;
    final private String TAG = "PatientListActivity";

    // URL for the test
    final private String strURLTest = "https://patient-data-management.herokuapp.com/patients";

    // Text view for the test
    TextView textViewTest;

    ListView listViewPatient;
    InfoAdapterPatient infoAdapterPatient;
    //List listPatient = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        // Get Views
        textViewTest = (TextView) findViewById(R.id.textViewTitle);
        listViewPatient = findViewById(R.id.listViewPatient);

        infoAdapterPatient = new InfoAdapterPatient(this,R.layout.row_patient);
        listViewPatient.setAdapter(infoAdapterPatient);


        // Check Permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);

        } else{

            //HashMap<String, String> postDataParams = new HashMap<>();

            //postDataParams.put("title", "post title");
            //postDataParams.put("description", "post description");
            //performPostCall(strURLTest, postDataParams);


            //call this asynchronously
            //new PostPatientsTask().execute(strURLTest);
            new GetPatientsTask().execute(strURLTest);
        }
    }

    public void MoreInfoPatient(View view){
        Intent intent = new Intent(PatientListActivity.this,ShowPatientActivity.class);
        startActivity(intent);
    }

    public void AddonClick(View view){
        Intent intent = new Intent(PatientListActivity.this,Add_Patient_Activity.class);
        startActivity(intent);
    }


    private InputStream openHttpConnection(String strURL, String strMethod) throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod(strMethod);
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }


    private String getPatients(String strURL)
    {
        InputStream in = null;
        try {
            in = openHttpConnection(strURL, "GET");
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }


        String strResult;
        try {
            String strLine = "";
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder stringBuilder = new StringBuilder();
            while ((strLine = bufferedReader.readLine())!=null)
            {
                stringBuilder.append(strLine);
                //listPatient.add(strLine);
            }

            bufferedReader.close();
            in.close();
            //httpURLConnection.disconnect();

            strResult = stringBuilder.toString().trim();
            return strResult;

        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            e.printStackTrace();
            return "";
        }
    }

    // Get Patients
    private class GetPatientsTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return getPatients(urls[0]);
        }
        @Override
        protected void onPostExecute(String result) {

            //textViewTest.setText(result);
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

            try{
                JSONObject jsonObject;
                JSONArray jsonArray = new JSONArray(result);


                int count = 0;
                String id, firstName ,lastName;
                //String title, description;

                infoAdapterPatient = new InfoAdapterPatient(PatientListActivity.this, R.layout.row_patient);

                while (count < jsonArray.length()){
                    jsonObject = jsonArray.getJSONObject(count);
                    //d = jsonObject.getString("name");
                    firstName = jsonObject.getString("first_name");
                    lastName = jsonObject.getString("last_name");
                    //title = jsonObject.getString("first_name");
                    //description = jsonObject.getString("description");


                    InfoDataPatient infoData = new InfoDataPatient(firstName, lastName);
                    infoAdapterPatient.add(infoData);

                    count++;
                }
                listViewPatient.setAdapter(infoAdapterPatient);

            } catch (JSONException e) {
                Log.d(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }

        }
    }



    // Post Patients
    private class PostPatientsTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {



            JSONObject jsonObject = new JSONObject();
            try {
                //jsonObject.put("title", "post_title");
                //jsonObject.put("description", "post_description");
                jsonObject.put("first_name", "Huen");
                jsonObject.put("last_name", "Oh");
            } catch (JSONException e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            URL url;
            String response = "";
            try {
                url = new URL(urls[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();



                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                //os.writeBytes(URLEncoder.encode(jsonObject.toString(), "UTF-8"));
                //String strTmp = URLEncoder.encode(jsonObject.toString(), "UTF-8");
                os.writeBytes(jsonObject.toString());
                String strTmp = jsonObject.toString();

                os.flush();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;

        }
        @Override
        protected void onPostExecute(String result) {



        }
    }


    public void onClickGetPatients(View view){
        new GetPatientsTask().execute(strURLTest);
    }

    public void onClickPostPatients(View view){
        new PostPatientsTask().execute(strURLTest);
    }

}
