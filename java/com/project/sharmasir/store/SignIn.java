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
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        final EditText emailbox = ((EditText)findViewById(R.id.em));


        SharedPreferences myprefs= getSharedPreferences("user", MODE_PRIVATE);
        String email= myprefs.getString("user", null);
        if(email!=null){
            emailbox.setText(email);
        }

        Button signin = (Button)findViewById(R.id.login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailbox.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(SignIn.this,"Please enter Email id!!!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String pass = ((EditText)findViewById(R.id.password)).getText().toString();
                if(pass.isEmpty()){
                    Toast.makeText(SignIn.this,"Please enter valid password!!!",Toast.LENGTH_SHORT).show();
                    return;
                }

                new Login(email,pass).execute();
            }
        });
    }
    class Login extends AsyncTask<String,Void,String> {

        String email,password;

        public Login(String email, String password) {
            this.email=email;
            this.password=password;
        }

        @Override
        protected String doInBackground(String... params) {
            String line, response = "";
            String POST_PARAMS ="email="+email ;
            POST_PARAMS+="&password="+password ;

            try {

                URL url = new URL("http://192.168.0.184/Reader'sParadise/signin.php");
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

                Log.e("RESPONSE",response+"----------");
                //System.out.println(response);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }


        protected void onPostExecute(String result) {
            if(result.equals("0")) {
                Toast.makeText(getApplicationContext(), "Invalid Email or password", Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("")){
                Toast.makeText(getApplicationContext(), "Server Error!!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Welcome to Readers Paradise!!", Toast.LENGTH_SHORT).show();
                SharedPreferences myprefs= getSharedPreferences("user",MODE_PRIVATE);
                myprefs.edit().putString("user", email).commit();
                myprefs.edit().putString("username", result).commit();
                Log.e("Name",result);
                Intent i = new Intent(getBaseContext(),ExploreActivity.class);
                startActivity(i);
            }
        }
    }
}
