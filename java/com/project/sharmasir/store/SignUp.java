package com.project.sharmasir.store;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.R.attr.value;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final CheckedTextView fiction = (CheckedTextView)findViewById(R.id.fiction);
        fiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fiction.isChecked()){
                    fiction.setChecked(false);
                }
                else{
                    fiction.setChecked(true);
                }
            }
        });
        final CheckedTextView sports = (CheckedTextView)findViewById(R.id.sports);
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sports.isChecked()){
                    sports.setChecked(false);
                }
                else{
                    sports.setChecked(true);
                }
            }
        });
        final CheckedTextView bio = (CheckedTextView)findViewById(R.id.bio);
        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bio.isChecked()){
                    bio.setChecked(false);
                }
                else{
                    bio.setChecked(true);
                }
            }
        });
        final CheckedTextView edu = (CheckedTextView)findViewById(R.id.edu);
        edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edu.isChecked()){
                    edu.setChecked(false);
                }
                else{
                    edu.setChecked(true);
                }
            }
        });
        final CheckedTextView rom = (CheckedTextView)findViewById(R.id.rom);
        rom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rom.isChecked()){
                    rom.setChecked(false);
                }
                else{
                    rom.setChecked(true);
                }
            }
        });
        final CheckedTextView horror = (CheckedTextView)findViewById(R.id.horror);
        horror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(horror.isChecked()){
                    horror.setChecked(false);
                }
                else{
                    horror.setChecked(true);
                }
            }
        });

        final CheckedTextView msports = (CheckedTextView)findViewById(R.id.msports);
        msports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msports.isChecked()){
                    msports.setChecked(false);
                }
                else{
                    msports.setChecked(true);
                }
            }
        });

        final CheckedTextView medu = (CheckedTextView)findViewById(R.id.medu);
        medu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(medu.isChecked()){
                    medu.setChecked(false);
                }
                else{
                    medu.setChecked(true);
                }
            }
        });
        final CheckedTextView tech = (CheckedTextView)findViewById(R.id.tech);
        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tech.isChecked()){
                    tech.setChecked(false);
                }
                else{
                    tech.setChecked(true);
                }
            }
        });
        final CheckedTextView fashion = (CheckedTextView)findViewById(R.id.fashion);
        fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fashion.isChecked()){
                    fashion.setChecked(false);
                }
                else{
                    fashion.setChecked(true);
                }
            }
        });
        final CheckedTextView lifestyle = (CheckedTextView)findViewById(R.id.lifestyle);
        lifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lifestyle.isChecked()){
                    lifestyle.setChecked(false);
                }
                else{
                    lifestyle.setChecked(true);
                }
            }
        });
        final CheckedTextView women = (CheckedTextView)findViewById(R.id.women);
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(women.isChecked()){
                    women.setChecked(false);
                }
                else{
                    women.setChecked(true);
                }
            }
        });

        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)findViewById(R.id.name)).getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(SignUp.this,"Please Enter Name!!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = ((EditText)findViewById(R.id.email)).getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(SignUp.this,"Please Enter Email Address!!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String pass = ((EditText)findViewById(R.id.pass)).getText().toString();
                if(pass.isEmpty()){
                    Toast.makeText(SignUp.this,"Please Enter valid Password!!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String bpreferences="";

                if(fiction.isChecked()){
                    bpreferences+=fiction.getText().toString()+",";
                }
                if(sports.isChecked()){
                    bpreferences+=sports.getText().toString()+",";
                }
                if(bio.isChecked()){
                    bpreferences+=bio.getText().toString()+",";
                }
                if(edu.isChecked()){
                    bpreferences+=edu.getText().toString()+",";
                }
                if(rom.isChecked()){
                    bpreferences+=rom.getText().toString()+",";
                }
                if(horror.isChecked()){
                    bpreferences+=horror.getText().toString()+",";
                }

                String mpreferences="";

                if(tech.isChecked()){
                    mpreferences+=tech.getText().toString()+",";
                }
                if(msports.isChecked()){
                    mpreferences+=msports.getText().toString()+",";
                }
                if(fashion.isChecked()){
                    mpreferences+=fashion.getText().toString()+",";
                }
                if(medu.isChecked()){
                    mpreferences+=medu.getText().toString()+",";
                }
                if(lifestyle.isChecked()){
                    mpreferences+=lifestyle.getText().toString()+",";
                }
                if(women.isChecked()){
                    mpreferences+=women.getText().toString()+",";
                }
                Log.e("Password+++",pass);
                new Registration(name,email,pass,bpreferences,mpreferences).execute();
            }
        });
    }

    class Registration extends AsyncTask<String,Void,String> {

        String name,email,password,bpreferences,mpreferences;

        public Registration(String name, String email, String password, String bpreferences, String mpreferences) {
            this.name = name;
            this.email=email;
            this.password=password;
            this.bpreferences=bpreferences;
            this.mpreferences=mpreferences;
        }

        @Override
        protected String doInBackground(String... params) {
            String line, response = "";
            String POST_PARAMS = "name="+name ;
            POST_PARAMS+="&email="+email ;
            POST_PARAMS+="&password="+password ;
            POST_PARAMS+="&bpreferences="+bpreferences ;
            POST_PARAMS+="&mpreferences="+mpreferences ;
            Log.e("Password------",password);

            try {

                URL url = new URL("http://192.168.0.184/Reader'sParadise/signup.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                    //conn.setRequestProperty("Connection", "close");
                }
                //conn.setRequestProperty("Accept-Encoding", "");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);

                // For POST only - BEGIN
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(POST_PARAMS.getBytes());
                os.flush();
                os.close();
                // For POST only - END
                //conn.connect();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
// read the response
                    System.out.println("Response Code: " + conn.getResponseCode());
                    //InputStream in = new BufferedInputStream(conn.getInputStream());

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                }

                Log.e("RESPONSE","---"+response+"--");
                //System.out.println(response);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }


        protected void onPostExecute(String result) {
            Log.e("POST","--"+result+"--");
            if (result.equals("1")) {
                Toast.makeText(getApplicationContext(), "Sign Up successful", Toast.LENGTH_SHORT).show();
                SharedPreferences myprefs= getSharedPreferences("user",MODE_PRIVATE);
                myprefs.edit().putString("user", email).commit();
                Intent i = new Intent(getApplicationContext(),SignIn.class);
                startActivity(i);
            }
            else{
                Toast.makeText(getApplicationContext(), "Sign Up Not Successful!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
