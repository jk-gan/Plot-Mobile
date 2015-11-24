package com.example.jkgan.pmot;

import person.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText txtEmail = (EditText) findViewById(R.id.editTextNewEmail);
        final EditText txtPassword = (EditText) findViewById(R.id.editTextPass);
        final EditText txtName = (EditText) findViewById(R.id.editTextName);
        final EditText txtPassComf = (EditText) findViewById(R.id.editTextPassComf);
        Button btnRegister = (Button) findViewById(R.id.buttonRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();
                final String name = txtName.getText().toString();
                final String passwordComf = txtPassComf.getText().toString();

                if (email.matches("") || password.matches("") || name.matches("") || passwordComf.matches("")) {
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

                    RegisterASYNC registerTask = new RegisterASYNC();
                    registerTask.execute(name, email, password, passwordComf);
                }
            }
        });
    }

//    public JSONObject makeHttpRequest(String url, String method,
//                                      List<NameValuePair> params) {
//
//        InputStream is = null;
//        String json = "";
//        JSONObject jObj = null;
//
//        // Making HTTP request
//        try {
//
//            // check for request method
//            if (method.equals("POST")) {
//                // request method is POST
//                // defaultHttpClient
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                String paramString = URLEncodedUtils.format(params, "utf-8");
//                url += "?" + paramString;
//                url = URLDecoder.decode(url);
//                HttpPost httpPost = new HttpPost(url);
//
////                jObj.accumulate("name", person.getName());
////                jObj.accumulate("country", person.getCountry());
////                jObj.accumulate("twitter", person.getTwitter());
//
//                json = jObj.toString();
////                httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//                HttpResponse httpResponse = httpClient.execute(httpPost);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                is = httpEntity.getContent();
//
//            } else if (method.equals("GET")) {
//                // request method is GET
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                String paramString = URLEncodedUtils.format(params, "utf-8");
//                url += "?" + paramString;
//                url = URLDecoder.decode(url);
//                HttpGet httpGet = new HttpGet(url);
//
//                HttpResponse httpResponse = httpClient.execute(httpGet);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                is = httpEntity.getContent();
//            }
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    is, "iso-8859-1"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            is.close();
//            json = sb.toString();
//        } catch (Exception e) {
//            //Log.e("Buffer Error", "Error converting result " + e.toString());
//        }
//
//        // try parse the string to a JSON object
//        try {
//            jObj = new JSONObject(json);
//        } catch (JSONException e) {
//            // Log.e("JSON Parser", "Error parsing data " + e.toString());
//        }
//
//        // return JSON String
//        return jObj;
//
//    }

    public static JSONObject POST(String url, User user){
        InputStream inputStream = null;
        String result = "";
        JSONObject jObj = null;

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("name", user.getName());
            jsonObject.accumulate("email", user.getEmail());
            jsonObject.accumulate("password", user.getPassword());
            jsonObject.accumulate("password_confirmation", user.getPassword_comfirmation());

            JSONObject usersObj = new JSONObject();
            usersObj.put("user", jsonObject);

            // 4. convert JSONObject to JSON to String
            json = usersObj.toString();
            System.out.println(json);

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
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

    private class RegisterASYNC extends AsyncTask<String, Void, JSONObject> {

        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Registering...");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            User user = new User();
            user.setName(params[0]);
            user.setEmail(params[1]);
            user.setPassword(params[2]);
            user.setPassword_comfirmation(params[3]);
            
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();

            // For emulator
//            String strURL = "http://10.0.2.2:3000/api/v1/auth/login";

            // For other device
            String strURL = MyApplication.getUrl() + "/users/new";

                        /*JSONParser objJSONParser = new JSONParser();*/
//            final JSONObject jsonObj = makeHttpRequest(strURL, "POST", parameters);

            return POST(strURL, user);

        }

        @Override
        protected void onPostExecute(JSONObject result) {

            String email = null;
            email = result.optString("email");

            if (!email.equals("")) {

                new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);

                                Toast toast;
                                toast = Toast.makeText(getApplicationContext(), "Register successfully", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                }.start();


            } else {
                dialog.dismiss();
                Toast toast;
                toast = Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }
}
