package de.savrasov.kaysquared;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


/**
 * Created by Stopp on 18.02.2018.
 */

public class PrefActivity extends PreferenceActivity
{
    public void onCreate(final Bundle savedInstanceState){
        applyPreferences();
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public void applyPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("pref_Colorsetting", "wi");
        if (theme.equals("fa")) setTheme(R.style.AppTheme_NoActionBarFall);
        else if (theme.equals("sp")) setTheme(R.style.AppTheme_NoActionBarSpring);
        else if (theme.equals("su")) setTheme(R.style.AppTheme_NoActionBarSummer);
    }
}