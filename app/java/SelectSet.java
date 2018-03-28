package de.savrasov.kaysquared;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ListIterator;

import javax.net.ssl.HttpsURLConnection;

public class SelectSet extends AppCompatActivity {

    SetOrganizer so;
    ListView setlv;
    ArrayList<String> setList;
    ArrayAdapter<String> setListAdapter;
    boolean filterApplied;
    boolean setEditStarted;
    boolean loggedIn;
    String username;
    String token;
    Context context;
    final String setAdress = "https://savrasov.de/k-squared/app/json/json.php?action=qr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_set);
        context = getApplicationContext();
        setSetLv();
        filterApplied = false;
        setEditStarted = false;
        loggedIn = false;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            loggedIn = extras.getBoolean("loggedIn");
            String dataString = extras.getString("dataString");
            if (dataString != null){
                if (loggedIn){
                    getIntent().removeExtra("dataString");
                    dataString = dataString.substring(dataString.indexOf("id=")+3, dataString.length());
                    so = SetOrganizer.loadSetOrganizer(getApplicationContext());
                    retrieveSetAsyncTask ctu = new retrieveSetAsyncTask();
                    ctu.execute(username, token, dataString);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "You need to log in to download from an QR-Code", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        }

    }
    @Override
    public void onResume(){
        super.onResume();
        if (setEditStarted) this.recreate();
        else applyPreferences();
        so = SetOrganizer.loadSetOrganizer(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {         //called whenever .invalidat() is called
        getMenuInflater().inflate(R.menu.select_set_appbar_menu, menu);
        MenuItem cancel = menu.findItem(R.id.selectSetCancelBt);
        if (!filterApplied){
            cancel.setVisible(false);
        } else {
            cancel.setVisible(true);
        }

        return true;
    }

    public void applyPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("pref_Colorsetting", "wi");
        if (theme.equals("fa")) setTheme(R.style.AppTheme_NoActionBarFall);
        else if (theme.equals("sp")) setTheme(R.style.AppTheme_NoActionBarSpring);
        else if (theme.equals("su")) setTheme(R.style.AppTheme_NoActionBarSummer);
        else if (theme.equals("wi")) setTheme(R.style.AppTheme_NoActionBarWinter);
        username = sp.getString("username", null);
        token = sp.getString("token", null);
    }

    public boolean onOptionsItemSelected(MenuItem item) {   //toolbar actionhandling

        switch (item.getItemId()) {
            case R.id.selectSetSettingsBt:
                Intent intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                return true;
            case R.id.selectSetSearchBt:
                if (setlv.getAdapter() == setListAdapter) {
                    searchSet();
                } else {
                    setlv.setAdapter(setListAdapter);
                    filterApplied = false;
                    searchSet();
                }
                return true;

            case R.id.selectSetCancelBt:
                if (setlv.getAdapter() != setListAdapter) {
                    setlv.setAdapter(setListAdapter);
                }
                filterApplied = false;
                invalidateOptionsMenu();

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void searchSet(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Search set by name");
        alert.setMessage("Enter the name of the Set you are searching for");
        final MyInputText input = new MyInputText(this, "");
        alert.setView(input);
        alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    ArrayList<String> searchList = new ArrayList<String>();
                    for (String e: setList){
                        if (e.toLowerCase().contains(value.toLowerCase())) searchList.add(e);
                    }
                    ArrayAdapter<String> searchListAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.string_lv_item, R.id.list_content,searchList);
                    setlv.setAdapter(searchListAdapter);
                    filterApplied = true;
                    invalidateOptionsMenu();
                    return;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                invalidateOptionsMenu();
                return;
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                invalidateOptionsMenu();
                return;
            }
        });
        alert.show();
    }
    public void setSetLv(){                         //creates the ListView code for Set selection
        setlv = (ListView) findViewById(R.id.setlv);
        setListAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.string_lv_item, R.id.list_content, findSavedSets());
        setlv.setAdapter(setListAdapter);
        setlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s = (String)adapterView.getItemAtPosition(i);
                if (!s.equals("You have no sets yet :(")){
                    Intent kk;
                    kk = new Intent(getApplicationContext(), KKActivity.class);
                    String name = s.substring(0, s.indexOf(getApplicationContext().getString(R.string.set_name)));
                    int chapter = Integer.parseInt(s.substring(s.indexOf("#")+1));
                    kk.putExtra("SetName", name);
                    kk.putExtra("SetChapter", chapter);
                    startActivity(kk);
                }else {
                    createNewSet();
                }

            }
        });
        setlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l){
                final String s = (String)adapterView.getItemAtPosition(i);
                if (!s.equals("You have no sets yet :(")){
                    PopupMenu popup = new PopupMenu(context, view);
                    popup.getMenuInflater().inflate(R.menu.ss_lvitem_popup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            CharSequence input = item.getTitle();
                            if (input.equals("Edit Set")){
                                Intent createSet = new Intent(context, SetEdit.class);

                                String name = s.substring(0, s.indexOf(context.getString(R.string.set_name)));
                                int chapter = Integer.parseInt(s.substring(s.indexOf("#")+1));
                                createSet.putExtra("SetName", name);
                                createSet.putExtra("SetChapter", chapter);
                                setEditStarted = true;
                                startActivity(createSet);
                                return true;
                            }
                            if (input.equals("Delete Set")) {
                                deleteSet((String)adapterView.getItemAtPosition(i));
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
                return true;
            }
        });
    }

    private void deleteSet(final String item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Deletion");
        alert.setMessage("Do you really want to delete this Set?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!so.removeSet(context, item)){
                    Toast toast = Toast.makeText(getApplicationContext(), "Couldn't delete set", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    setListAdapter.remove(item);
                    so.saveSetOrganizer(context);
                }
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                return;
            }
        });

        alert.show();
    }

    public ArrayList<String> findSavedSets(){                         //finds sets on users phone. tbimplemented
        setList = new ArrayList<String>();
        so = SetOrganizer.loadSetOrganizer(getApplicationContext());
        if (so != null){
            ListIterator<CardSet> li = so.listIterator();
            while (li.hasNext()){
                CardSet cs = li.next();
                setList.add(cs.name + getApplicationContext().getString(R.string.set_name) + cs.chapter);
            }
            Collections.sort(setList, new Comparator<String>() {
                @Override
                public int compare(String a, String b)
                {
                    return  a.toLowerCase().compareTo(b.toLowerCase());
                }
            });
        } else {
            so = new SetOrganizer();
        }
        if (setList.isEmpty()) setList.add("You have no sets yet :(");
        return setList;
    }

    public void popupSetCreation(View v){                       //Creates popup menu for + button
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.fab_popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                CharSequence input = item.getTitle();
                if (input.equals("Create new set")) createNewSet();
                if (input.equals("Get new set from internet")) {
                    retrieveSet();
                }
                return true;
            }
        });

        popup.show();
    }


    public void retrieveSet(){
        if (loggedIn){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Retrieve set");
            alert.setMessage("Please enter the sharelink or set-ID");
            final MyInputText input = new MyInputText(this, "Link/ID");
            alert.setView(input);
            alert.setPositiveButton("Retrieve", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    if (value.contains("id=")){
                        value = value.substring(value.indexOf("id=")+3, value.length());
                    }
                    so = SetOrganizer.loadSetOrganizer(getApplicationContext());
                    retrieveSetAsyncTask ctu = new retrieveSetAsyncTask();
                    ctu.execute(username, token, value);

                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast toast = Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    Toast toast = Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            alert.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "You need to log in first", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void createNewSet(){                     //Creates new Set. Asks for data first
        AlertDialog.Builder alert = new AlertDialog.Builder(SelectSet.this);
        alert.setTitle("Set creation");
        alert.setMessage("Please enter a name for your new set");
        final MyInputText input = new MyInputText(SelectSet.this, "Setname");
        alert.setView(input);
        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Intent createSet;
                createSet = new Intent(context, SetEdit.class);
                createSet.putExtra("SetName", value);
                setEditStarted = true;
                startActivity(createSet);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast toast = Toast.makeText(getApplicationContext(), "Set creation cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                Toast toast = Toast.makeText(getApplicationContext(), "Set creation cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        alert.show();
    }


    public class retrieveSetAsyncTask extends AsyncTask<String, Void, Void>{
        final ProgressDialog dialog = new ProgressDialog(SelectSet.this);
        JSONObject jsonObject;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Processing...");
            dialog.show();
            jsonObject = null;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                String username = URLEncoder.encode(strings[0], "UTF-8");
                String token = URLEncoder.encode(strings[1], "UTF-8");
                String id = URLEncoder.encode(strings[2], "UTF-8");
                URL url = new URL(setAdress);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write("user="+username+"&token="+token+"&id="+id);
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
                jsonObject = new JSONObject(jsonString);
                if (jsonObject != null && jsonObject.getString("success").equals("success")){
                    String setname = jsonObject.getString("setname");
                    int chapter = Integer.parseInt(jsonObject.getString("chapter"));
                    String description = jsonObject.getString("description");
                    long created_on = Long.parseLong(jsonObject.getString("created_on"));
                    CardSet cardSet = new CardSet(id, setname, chapter, description, created_on);
                    int c = Integer.parseInt(jsonObject.getString("cards"));
                    for (int n = 0; n < c; n++){
                        Card newCard = new Card(jsonObject.getString(n+"fronttext"),
                                                jsonObject.getString(n+"backtext"),
                                                jsonObject.getString(n+"frontimageurl"),
                                                jsonObject.getString(n+"backimageurl"),
                                                Long.parseLong(jsonObject.getString(n+"timetolearn")));
                        cardSet.addCard(newCard);
                    }
                    if ((so.findSet(SelectSet.this, setname + SelectSet.this.getString(R.string.set_name) + chapter)==null)){
                        so.addSet(cardSet);
                        so.saveSetOrganizer(SelectSet.this);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                recreate();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(SelectSet.this, "Set with that name already exists", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }

                }

            } catch (IOException|JSONException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}

