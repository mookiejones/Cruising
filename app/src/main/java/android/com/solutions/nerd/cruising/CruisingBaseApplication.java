package android.com.solutions.nerd.cruising;

import android.app.Activity;
import android.com.solutions.nerd.cruising.ui.activities.IntroActivity;
import android.com.solutions.nerd.cruising.ui.activities.LoginActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Cache;

import java.io.File;

/**
 * Created by cberman on 3/3/2017.
 */

public abstract class CruisingBaseApplication extends MultiDexApplication {

    public static Activity currentActivity = null;

    public RefWatcher refWatcher;


    public static CruisingBaseApplication getInstance(Context context) {
        return (CruisingBaseApplication) context.getApplicationContext();
    }




    public static void logout(Context context) {
        /*
        getInstance(context).deleteDatabase(HabitDatabase.NAME);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean use_reminder = preferences.getBoolean("use_reminder", false);
        String reminder_time = preferences.getString("reminder_time", "19:00");
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putBoolean("use_reminder", use_reminder);
        editor.putString("reminder_time", reminder_time);
        editor.apply();
        getInstance(context).lazyApiHelper.get().updateAuthenticationCredentials(null, null);
        startActivity(LoginActivity.class, context);
        */
    }
/*
    public static boolean checkUserAuthentication(Context context, HostConfig hostConfig) {
        if (hostConfig == null || hostConfig.getApi() == null || hostConfig.getApi().equals("") || hostConfig.getUser() == null || hostConfig.getUser().equals("")) {
            startActivity(IntroActivity.class, context);

            return false;
        }

        return true;
    }*/


    private static void startActivity(Class activityClass, Context context) {
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // region SQLite overrides

//    public static AppComponent getComponent() {
 //       return component;
  //  }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//        setupDagger();
      //  crashlyticsProxy.init(this);
        setupLeakCanary();
//        setupFlowManager();
        setupFacebookSdk();
//        createBillingAndCheckout();
    //    registerActivityLifecycleCallbacks();


//        checkIfNewVersion();
    }


    private void setupLeakCanary() {
        refWatcher = LeakCanary.install(this);
    }

    private void setupFacebookSdk() {
        String fbApiKey = null;
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            fbApiKey = bundle.getString(FacebookSdk.APPLICATION_ID_PROPERTY);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("FB Error", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("FB Error", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        if (fbApiKey != null) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name,
                                               int mode, SQLiteDatabase.CursorFactory factory) {
        return super.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), mode, factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name,
                                               int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return super.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), mode, factory, errorHandler);
    }

    // endregion

    // region IAP - Specific


}
