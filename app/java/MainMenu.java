package de.savrasov.kaysquared;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class MainMenu extends AppCompatActivity {
    EditText usernameTF;
    EditText passwordTF;
    String token;
    String username;
    JSONObject jsonNewToken;
    Context context;
    String dataString;
    boolean wifi;
    boolean loggedIn;
    final String loginAdress = "https://savrasov.de/k-squared/app/json/json.php?action=login";
    final String checkTokenAdress = "https://savrasov.de/k-squared/app/json/json.php?action=check_token";
    protected void onCreate(Bundle savedInstanceState) {
        applyPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        context = getApplicationContext();
        jsonNewToken = null;
        wifi = checkForInternet();
        dataString = null;
        loggedIn = false;
        Intent intent = getIntent();
        if (intent != null){
            dataString = intent.getDataString();
        }
        tokenAvailable();
        //urlString = "http://echo.jsontest.com/key/value/one/two";
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyPreferences();
    }
    @Override
    protected void onPause(){
        super.onPause();
        loggedIn = false;
        requireLogin();
        dataString = null;
    }

    public void applyPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("pref_Colorsetting", "wi");
        token = sp.getString("token", null);
        username = sp.getString("username", null);
        if (theme.equals("fa")) setTheme(R.style.AppTheme_NoActionBarFall);
        else if (theme.equals("sp")) setTheme(R.style.AppTheme_NoActionBarSpring);
        else if (theme.equals("su")) setTheme(R.style.AppTheme_NoActionBarSummer);
    }

    public void tokenAvailable(){
        if (token == null || username == null) {
            requireLogin();
            return;
        }
        else {
            LoginAsyncTask ctu = new LoginAsyncTask();
            ctu.execute("checkLogin", username, token);
            return;
        }
    }

    public void requireLogin(){
        usernameTF = (EditText) findViewById(R.id.usernameTF);
        passwordTF = (EditText) findViewById(R.id.passwordTF);
        LinearLayout ll = (LinearLayout)findViewById(R.id.loginForm);
        ll.setVisibility(View.VISIBLE);
        if (username != null) {
            usernameTF.setText(username);
            usernameTF.setKeyListener(null);
        }

    }

    public void startSetSelection(View v){
        Intent intent = new Intent(context, SelectSet.class);
        if (dataString != null){
            intent.putExtra("dataString", dataString);
        }
        intent.putExtra("loggedIn", loggedIn);
        startActivity(intent);
    }

    public void openHomepage(View v){
        if (wifi){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://savrasov.de/k-squared#register"));
            startActivity(browserIntent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),"You are not connected to the internet.", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void loginButtonPressed(View v){
        String username = usernameTF.getText().toString();
        String password = passwordTF.getText().toString();
        if(!username.equals("") && !password.equals("")){
            try{
                LoginAsyncTask ctu = new LoginAsyncTask();
                ctu.execute("login", username, password);
            } catch (Exception ex){
                Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public boolean setToken(){
        try {
            if(jsonNewToken == null){
                Toast toast = Toast.makeText(getApplicationContext(), "Communication with server failed", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (jsonNewToken.getString("success").equals("success")){
                    token = jsonNewToken.getString("token");
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("token", token);
                    editor.putString("username", jsonNewToken.getString("user"));
                    editor.commit();
                    return true;
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Login data wrong.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        } catch (JSONException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Communication with server failed.", Toast.LENGTH_SHORT);
            toast.show();
        }
        return false;
    }

    private boolean checkForInternet() { // code from https://stackoverflow.com/questions/3841317/how-do-i-see-if-wi-fi-is-connected-on-android
                                         // LukeMovement, Jul 9'11, 9:28, last edit Peter Mortensen, Sep 25'16, 8:42
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected() || mobile.isConnected()){
            return true;
        }else return false;
    }



    public class LoginAsyncTask extends AsyncTask<String, Void, Void>{

        final ProgressDialog dialog = new ProgressDialog(MainMenu.this);
        boolean isLogin;
        @Override
        protected Void doInBackground(String... strings) {
            try {
                if (wifi){
                    if(strings[0].equals("login")) isLogin = true;
                    else isLogin = false;
                    URL url;
                    String username;
                    String tokenPW;
                    username = strings[1];
                    tokenPW = strings[2];
                    username = URLEncoder.encode(username, "UTF-8");
                    tokenPW = URLEncoder.encode(tokenPW, "UTF-8");
                    if (isLogin){
                        url = new URL(loginAdress);
                    } else {
                        url = new URL(checkTokenAdress);
                    }
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    if (isLogin){
                        writer.write("user="+username+"&password="+tokenPW);
                    } else {
                        writer.write("user="+username+"&token="+tokenPW);
                    }
                    writer.flush(); writer.close();
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    String jsonString = sb.toString();
                    jsonNewToken = new JSONObject(jsonString);
                }


            } catch (IOException|JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void o) {
            dialog.dismiss();
            if (wifi){
                if (isLogin){
                    if (setToken()){
                        loggedIn = true;
                        startSetSelection(null);
                    }
                    else requireLogin();
                } else {
                    try {
                        if (jsonNewToken != null){
                            if (jsonNewToken.getString("success").equals("success")){
                                loggedIn = true;
                                startSetSelection(null);
                            } else {
                                jsonNewToken = null;
                                requireLogin();
                            }
                        } else {
                            Toast toast = Toast.makeText(MainMenu.this, "Communication with server failed.", Toast.LENGTH_SHORT);
                            toast.show();
                            requireLogin();
                        }

                    } catch (JSONException e) {
                        requireLogin();
                        e.printStackTrace();
                    }
                }
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainMenu.this);
                alert.setTitle("No Wifi");
                alert.setMessage("Couldn't connect to internet. Proceed offline?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        loggedIn = false;
                        startSetSelection(null);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        requireLogin();
                    }
                });
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        requireLogin();
                    }
                });

                alert.show();
                requireLogin();
            }

            super.onPostExecute(o);
        }
    }

}
