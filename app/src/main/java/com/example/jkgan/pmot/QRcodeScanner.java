package com.example.jkgan.pmot;

/**
 * Created by JKGan on 01/11/2015.
 */
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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


public class QRcodeScanner extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private FloatingActionButton scanButton;
    private ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_scanner);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        initControls();
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.qrcode_scanner,container, false);
//        initControls(rootView);
//
//        return rootView;
//    }

    private void initControls() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toast toast;
        toast = Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT);
        toast.show();

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb,
                autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanButton = (FloatingActionButton) findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
        return onKeyDown(keyCode, event);
    }


    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {

                    Log.i("<<<<<<Asset Code>>>>> ",
                            "<<<<Bar Code>>> " + sym.getData());
                    String scanResult = sym.getData().trim();

                    if(scanResult.contains("Pmot@")) {
                        releaseCamera();
                        String str = scanResult.substring(5);
                        CheckASYNC scanTask = new CheckASYNC();

                        MyApplication appState = ((MyApplication) getApplicationContext());
                        scanTask.execute(str, appState.getUser().getToken());
                    } else
                        showAlertDialog(scanResult);

                  /*  Toast.makeText(BarcodeScanner.this, scanResult,
                            Toast.LENGTH_SHORT).show();*/

                    barcodeScanned = true;

                    break;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


    private void showAlertDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }

    private class CheckASYNC extends AsyncTask<String, Void, JSONObject> {
        final ProgressDialog dialog = new ProgressDialog(QRcodeScanner.this);

        @Override
        protected void onPreExecute() {
             new Thread() {
                 public void run() {
                     runOnUiThread(new Runnable() {
                         public void run() {
                             dialog.setMessage("Loading...");
                             dialog.setCancelable(false);
                             dialog.setInverseBackgroundForced(false);
                             dialog.show();
                         }
                     });
                 }
             }.start();


        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("identity", params[0]));
            parameters.add(new BasicNameValuePair("token", params[1]));

            // For emulator
//            String strURL = "http://10.0.2.2:3000/api/v1/auth/login";

            // For other device
            String strURL = MyApplication.getApiUrl() + "/shops";

                        /*JSONParser objJSONParser = new JSONParser();*/
            final JSONObject jsonObj = makeHttpRequest(strURL, "GET", parameters);

            return jsonObj;

        }

        @Override
        protected void onPostExecute(JSONObject result){

            String shopName = null;
            String shopId = null;
            shopName = result.optString("name");
            shopId = result.optString("id");
            if (!shopName.equals("")) {
                final String NAME = shopName;
                final String SHOP_ID = shopId;
                final String SHOP_ADDRESS = result.optString("address");
                try {
                    final String SHOP_IMAGE = result.getJSONObject("image").getJSONObject("medium").optString("url");
                    final String SHOP_PHONE = result.optString("phone");
                    final String SHOP_DESCRIPTION = result.optString("description");

                    new Thread() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                    Intent intent = new Intent(QRcodeScanner.this, ShopActivity.class);
                                    intent.putExtra("NAME", NAME);
                                    intent.putExtra("ADDRESS", SHOP_ADDRESS);
                                    intent.putExtra("IMAGE", SHOP_IMAGE);
                                    intent.putExtra("PHONE", SHOP_PHONE);
                                    intent.putExtra("DESCRIPTION", SHOP_DESCRIPTION);
                                    intent.putExtra("SHOP_ID", SHOP_ID);
                                    intent.putExtra("SUBSCRIBED", false);

//                                ShopActivity fragment = new ShopActivity();
//                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                fragmentTransaction.replace(R.id.frame, fragment);
//
//                                // Remove the tab
//                                ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
//                                TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
//                                tabLayout.setVisibility(View.GONE);
//                                viewPager.setVisibility(View.GONE);
//
//                                fragmentTransaction.commit();

                                    startActivity(intent);
                                    finish();

                                    Toast toast;
                                    toast = Toast.makeText(getApplicationContext(), NAME, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                    }.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            } else {
                dialog.dismiss();
                Toast toast;
                toast = Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

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
//                    String paramString = URLEncodedUtils.format(params, "utf-8");
//                    url += "?" + paramString;
//                    url = URLDecoder.decode(url);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

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

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }
//
    @Override
    public void onResume(){
        super.onResume();
        try
        {
            if(mCamera == null) {
                mCamera.setPreviewCallback(null);
                mPreview.getHolder().removeCallback(mPreview);
                mCamera = getCameraInstance();
                //mCamera.setPreviewCallback(null);
                mPreview = new CameraPreview(this, mCamera, previewCb,
                        autoFocusCB);//set preview
                FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
                preview.addView(mPreview);
            }
//
        } catch (Exception e){
//            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            e.printStackTrace();
        }
    }

}