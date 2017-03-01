public class PrefsActivity extends BaseActivity implements
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // TODO:
    // This method should be moved to HabiticaApplication
    public static HostConfig fromContext(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        HostConfig config;
        String httpPort = BuildConfig.PORT;
        String address = BuildConfig.DEBUG ? BuildConfig.BASE_URL : ctx.getString(R.string.base_url);
        String api = prefs.getString(ctx.getString(R.string.SP_APIToken), null);
        String userID = prefs.getString(ctx.getString(R.string.SP_userID), null);
        config = new HostConfig(address, httpPort, api, userID);
        return config;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_prefs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new PreferencesFragment())
                .commit();
    }

    @Override
    protected void injectActivity(AppComponent component) {
        component.inject(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            onBackPressed();
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragment,
                                           PreferenceScreen preferenceScreen) {
        PreferenceFragmentCompat fragment = createNextPage(preferenceScreen);
        if (fragment != null) {
            Bundle arguments = new Bundle();
            arguments.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    private PreferenceFragmentCompat createNextPage(PreferenceScreen preferenceScreen) {
        PreferenceFragmentCompat fragment = null;
        if (preferenceScreen.getKey().equals("accountDetails")) {
            fragment = new AccountDetailsFragment();
        }

        if (preferenceScreen.getKey().equals("pushNotifications")) {
            fragment = new PushNotificationsPreferencesFragment();
        }
        return fragment;
    }
}
