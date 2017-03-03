package android.com.solutions.nerd.cruising;

import android.app.Activity;
import android.com.solutions.nerd.cruising.ui.activities.IntroActivity;
import android.com.solutions.nerd.cruising.ui.activities.LoginActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Cache;

import java.io.File;

/**
 * Created by cberman on 3/3/2017.
 */

public abstract class CruisingBaseApplication extends MultiDexApplication {

    public static HabitRPGUser User;
    public static Activity currentActivity = null;
    private static AppComponent component;
    public RefWatcher refWatcher;
    @Inject
    Lazy<APIHelper> lazyApiHelper;
    @Inject
    SharedPreferences sharedPrefs;
    @Inject
    CrashlyticsProxy crashlyticsProxy;
    /**
     * For better performance billing class should be used as singleton
     */
    @NonNull
    private Billing billing;
    /**
     * Application wide {@link Checkout} instance (can be used
     * anywhere in the app).
     * This instance contains all available products in the app.
     */
    @NonNull
    private Checkout checkout;

    public static CruisingBaseApplication getInstance(Context context) {
        return (CruisingBaseApplication) context.getApplicationContext();
    }

    public static boolean exists(@NonNull Context context) {
        try {
            File dbFile = context.getDatabasePath(String.format("%s.db", HabitDatabase.NAME));
            return dbFile.exists();
        } catch (Exception exception) {
            Log.e("DbExists", "Database %s doesn't exist.", exception);
            return false;
        }
    }


    public static void logout(Context context) {
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
    }

    public static boolean checkUserAuthentication(Context context, HostConfig hostConfig) {
        if (hostConfig == null || hostConfig.getApi() == null || hostConfig.getApi().equals("") || hostConfig.getUser() == null || hostConfig.getUser().equals("")) {
            startActivity(IntroActivity.class, context);

            return false;
        }

        return true;
    }

    private static void startActivity(Class activityClass, Context context) {
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // region SQLite overrides

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        setupDagger();
        crashlyticsProxy.init(this);
        setupLeakCanary();
        setupFlowManager();
        setupFacebookSdk();
        createBillingAndCheckout();
        registerActivityLifecycleCallbacks();

        if (!BuildConfig.DEBUG) {
            try {
                Amplitude.getInstance().initialize(this, getString(R.string.amplitude_app_id)).enableForegroundTracking(this);
            } catch (Resources.NotFoundException e) {
                //pass
            }
        }

        Fresco.initialize(this);
        checkIfNewVersion();
    }

    private void checkIfNewVersion() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MyApplication", "couldn't get package info!");
        }

        if (info == null) {
            return;
        }

        int lastInstalledVersion = sharedPrefs.getInt("last_installed_version", 0);
        if (lastInstalledVersion < info.versionCode) {
            sharedPrefs.edit().putInt("last_installed_version", info.versionCode).apply();
            APIHelper apiHelper = this.lazyApiHelper.get();

            apiHelper.apiService.getContent(apiHelper.languageCode)
                    .compose(this.lazyApiHelper.get().configureApiCallObserver())
                    .subscribe(contentResult -> {
                    }, throwable -> {
                    });
        }

    }

    private void setupDagger() {
        component = initDagger();
        component.inject(this);
    }

    protected abstract AppComponent initDagger();

    private void setupLeakCanary() {
        refWatcher = LeakCanary.install(this);
    }

    private void setupFlowManager() {
        FlowManager.init(this);
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

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                HabiticaBaseApplication.currentActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                HabiticaBaseApplication.currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (currentActivity == activity)
                    currentActivity = null;
            }
        });
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

    @Override
    public boolean deleteDatabase(String name) {
        if (!name.endsWith(".db")) {
            name += ".db";
        }

        FlowManager.destroy();
        reflectionHack(getApplicationContext());

        boolean deleted = super.deleteDatabase(getDatabasePath(name).getAbsolutePath());

        if (deleted) {
            Log.i("hack", "Database deleted");
        } else {
            Log.e("hack", "Database not deleted");
        }

        if (exists(getApplicationContext())) {
            Log.i("hack", "Database exists before FlowManager.init");
        } else {
            Log.i("hack", "Database does not exist before FlowManager.init");
        }

        return deleted;
    }

    // Hack for DBFlow - Not deleting Database
    // https://github.com/kaeawc/dbflow-sample-app/blob/master/app/src/main/java/io/kaeawc/flow/app/ui/MainActivityFragment.java#L201
    private void reflectionHack(@NonNull Context context) {

        try {
            Field field = FlowManager.class.getDeclaredField("mDatabaseHolder");
            setFinalStatic(field, null);
        } catch (NoSuchFieldException noSuchField) {
            Log.e("nosuchfield", "No such field exists in FlowManager", noSuchField);
        } catch (IllegalAccessException illegalAccess) {
            Log.e("illegalaccess", "Illegal access of FlowManager", illegalAccess);
        }

        FlowManager.init(context);

        if (exists(context)) {
            Log.i("Database", "Database exists after FlowManager.init with reflection hack");
        } else {
            Log.i("Database", "Database does not exist after FlowManager.init with reflection hack");
        }
    }

    @Override
    public File getDatabasePath(String name) {
        File dbFile = new File(getExternalFilesDir(null), "HabiticaDatabase/" + name);
        //Crashlytics.setString("Database File", dbFile.getAbsolutePath());
        return dbFile;
    }

    private void createBillingAndCheckout() {
        billing = new Billing(this, new Billing.DefaultConfiguration() {
            @NonNull
            @Override
            public String getPublicKey() {
                return "DONT-NEED-IT";
            }

            @Nullable
            @Override
            public Cache getCache() {
                return Billing.newCache();
            }

            @Override
            public PurchaseVerifier getPurchaseVerifier() {
                return new HabiticaPurchaseVerifier(HabiticaBaseApplication.this, lazyApiHelper.get());
            }
        });


        checkout = Checkout.forApplication(billing);
    }

    @NonNull
    public Checkout getCheckout() {
        return checkout;
    }

    // endregion

    public Billing getBilling() {
        return billing;
    }
}
