package com.example.jkgan.pmot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import org.apache.http.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import person.User;

public class LoginActivity extends AppCompatActivity {

    protected User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        emailWrapper.setHint("Email");
        passwordWrapper.setHint("Password");

        final EditText txtEmail = (EditText) findViewById(R.id.editTextEmail);
        final EditText txtPassword = (EditText) findViewById(R.id.editTextPassword);
        Button btnlogin = (Button) findViewById(R.id.buttonLogin);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();

                if (email.matches("") || password.matches("")) {
                    new Thread() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Please fill all empty fields", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }.start();

                } else {

                    LoginASYNC loginTask = new LoginASYNC();
                    loginTask.execute(email, password);
                }
            }
        });
    }

    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params) {

        InputStream is = null;
        String json = "";
        JSONObject jObj = null;

        // Making HTTP request
        try {

            // check for request method
            if (method.equals("POST")) {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }




            } else if (method.equals("GET")) {
                // request method is GET
                HttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                url = URLDecoder.decode(url);
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            //Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            // Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }


     private class LoginASYNC extends AsyncTask<String, Void, JSONObject> {

         final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

         @Override
         protected void onPreExecute() {
             dialog.setMessage("Loging in...");
             dialog.setCancelable(false);
             dialog.setInverseBackgroundForced(false);
             dialog.show();

         }

        @Override
        protected JSONObject doInBackground(String... params) {

            final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("email", params[0]));
            parameters.add(new BasicNameValuePair("password", params[1]));
            String strURL = MyApplication.getUrl() + "/auth/login";

            return makeHttpRequest(strURL, "POST", parameters);
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            String token = "";
            token = result.optString("token");
            user.setToken(token);

            try {
                JSONObject userData = result.getJSONObject("user");
                user.setName(userData.optString("name"));
                user.setEmail(userData.optString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (!token.equals("")) {

                new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("TOKEN", TOKEN);
                                MyApplication myApp = ((MyApplication) getApplicationContext());
                                myApp.setUser(user);
                                startActivity(intent);

                                Toast toast;
                                toast = Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                }.start();

            } else {
                dialog.dismiss();
                Toast toast;
                toast = Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }
}
