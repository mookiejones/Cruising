package android.com.solutions.nerd.cruising.ui.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by mookie on 1/28/17.
 * for Nerd.Solutions
 */

public class MainActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        LanguageHelper languageHelper = new LanguageHelper(sharedPreferences.getString("language", "en"));
        Locale.setDefault(languageHelper.getLocale());
        Configuration configuration = new Configuration();
        getResources().updateConfiguration(configuration,
                getResources().getDisplayMetrics());
        if (!HabiticaApplication.checkUserAuthentication(this, hostConfig)) {
            return;
        }
    }
}
