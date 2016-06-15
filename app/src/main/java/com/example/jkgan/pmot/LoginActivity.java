package com.example.jkgan.pmot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.jkgan.pmot.Http.HttpRequest;
import com.example.jkgan.pmot.person.User;
import com.example.jkgan.pmot.service.QuickstartPreferences;

public class LoginActivity extends AppCompatActivity {

    protected User user = new User();
    public static final String LOGGED_IN = "loggedIN";
    public static String TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText txtEmail = (EditText) findViewById(R.id.editTextEmail);
        final EditText txtPassword = (EditText) findViewById(R.id.editTextPassword);
        Button btnlogin = (Button) findViewById(R.id.buttonLogin);
        TextView register = (TextView) findViewById(R.id.link_signup);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

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
            String strURL = MyApplication.getApiUrl() + "/auth/login";
            HttpRequest request = new HttpRequest();

            return request.makeHttpRequest(strURL, "POST", parameters);
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            String token = "";
            token = result.optString("token");
            user.setToken(token);
            PmotDB db = new PmotDB(getApplicationContext());

            try {
                JSONObject userData = result.getJSONObject("user");
                user.setName(userData.optString("name"));
                user.setEmail(userData.optString("email"));

                db.fnRunSQL("INSERT INTO users (id, name, email, token) VALUES (1, \""+userData.optString("name")+"\", \""+userData.optString("email")+"\", \""+token+"\");",
                        getApplicationContext());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (!token.equals("")) {

                final String THE_TOKEN = token;

                new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                SharedPreferences sharedPreferences =
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                sharedPreferences.edit().putBoolean(LOGGED_IN, true).apply();
                                sharedPreferences.edit().putString(TOKEN, THE_TOKEN).apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("TOKEN", TOKEN);
                                MyApplication myApp = ((MyApplication) getApplicationContext());
                                myApp.setUser(user);
                                startActivity(intent);
                                finish();
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
