package com.example.jkgan.pmot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

                    dialog.setMessage("Loging in...");
                    dialog.setCancelable(false);
                    dialog.setInverseBackgroundForced(false);
                    dialog.show();

                    LoginASYNC loginTask = new LoginASYNC(dialog);
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
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                url = URLDecoder.decode(url);
                HttpPost httpPost = new HttpPost(url);
//                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method.equals("GET")) {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
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

         ProgressDialog dialog;
         private LoginASYNC(ProgressDialog dialog) {
             this.dialog = dialog;
         }

         @Override
         protected void onPreExecute() {
//             new Thread() {
//                 public void run() {
//                     runOnUiThread(new Runnable() {
//                         public void run() {
//                             dialog.setMessage("Loging in");
//                             dialog.setCancelable(false);
//                             dialog.setInverseBackgroundForced(false);
//                             dialog.show();
//                         }
//                     });
//                 }
//             }.start();


         }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("email", params[0]));
            parameters.add(new BasicNameValuePair("password", params[1]));

            // For emulator
//            String strURL = "http://10.0.2.2:3000/api/v1/auth/login";

            // For other device
            String strURL = "http://pmot-web.192.168.1.5.xip.io/api/v1/auth/login";

                        /*JSONParser objJSONParser = new JSONParser();*/
            final JSONObject jsonObj = makeHttpRequest(strURL, "POST", parameters);

            return jsonObj;

        }

        @Override
        protected void onPostExecute(JSONObject result){

            String token = null;
            token = result.optString("token");

            if (!token.equals("")) {
                final String TOKEN = token;

                new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("TOKEN", TOKEN);
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
