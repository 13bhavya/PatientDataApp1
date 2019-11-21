package com.example.patientdataapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class Add_Patient_Activity extends AppCompatActivity {

    private static final String TAG = "Add_Patient_Activity";

    public String Firstname, Lastname, Add, Gende, Birth, Depart, relDoctor;

    EditText FirstName, LastName, Address, Gender, BirthDate, Department, RelDoctor;

    final private String strURLTest = "https://patient-data-management.herokuapp.com/patients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        FirstName = findViewById(R.id.firstName);
        LastName = findViewById(R.id.lastName);
        Address = findViewById(R.id.PatientAddr);
        Gender = findViewById(R.id.patientSex);
        BirthDate = findViewById(R.id.patientDob);
        Department = findViewById(R.id.patientDepart);
        RelDoctor = findViewById(R.id.patientDoc);

    }

    public void submitPatient(View view) {

        Firstname = FirstName.getText().toString();
        Lastname = LastName.getText().toString();
        Add = Address.getText().toString();
        Gende = Gender.getText().toString();
        Birth = BirthDate.getText().toString();
        Depart = Department.getText().toString();
        relDoctor = RelDoctor.getText().toString();

        if (Firstname.trim().equals("")) {
            FirstName.setError("First Name must not be Empty");

        } else if (Lastname.trim().equals("")) {
            LastName.setError("Last Name must not be Empty");

        } else {
            Intent intent = new Intent(Add_Patient_Activity.this, PatientListActivity.class);
            new PostPatientsTask().execute(strURLTest);
            startActivity(intent);
        }
    }


    // Post Patients
    private class PostPatientsTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {


            JSONObject jsonObject = new JSONObject();
            try {
                //jsonObject.put("title", "post_title");
                //jsonObject.put("description", "post_description");
                jsonObject.put("first_name", Firstname);
                jsonObject.put("last_name", Lastname);
                jsonObject.put("address", Add);
                jsonObject.put("sex", Gende);
                jsonObject.put("date_of_birth", Birth);
                jsonObject.put("department", Depart);
                jsonObject.put("doctor", relDoctor);
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
                conn.setRequestProperty("Accept", "application/json");
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
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
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

    private InputStream openHttpConnection(String strURL, String strMethod) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod(strMethod);
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    private String getPatients(String strURL) {
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
            while ((strLine = bufferedReader.readLine()) != null) {
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


}