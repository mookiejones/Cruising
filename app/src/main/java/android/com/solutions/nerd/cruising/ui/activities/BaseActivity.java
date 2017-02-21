package android.com.solutions.nerd.cruising.ui.activities;

import android.com.solutions.nerd.cruising.BuildConfig;
import android.com.solutions.nerd.cruising.interfaces.IVehicle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ActivityHelper;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by mookie on 1/28/17.
 * for Nerd.Solutions
 */

public abstract class BaseActivity  extends AppCompatActivity {
    private static final String TAG=BaseActivity.class.getSimpleName();
    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    protected ActivityHelper mActivityHelper;
    private IVehicle vehicle;
    private boolean destroyed;

    public IVehicle getVehicle(){return vehicle;}

    public void setVehicle(IVehicle vehicle){this.vehicle=vehicle;}

    protected abstract int getLayoutResId();

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Check firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            // Allready signed in
        }else{
            Log.d(TAG,"creating sign in");
            SignIn();
        }
    }

    private void SignIn(){


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
//                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
//                                ,new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
//                                ,new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
//                                ,new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()
//
                        //                      ))
                        /*
                        By default, FirebaseUI uses Smart Lock for Passwords to store the user's credentials
                        and automatically sign users into your app on subsequent attempts. Using Smart Lock is
                        recommended to provide the best user experience, but in some cases you may want to disable
                        Smart Lock for testing or development. To disable Smart Lock, you can use the setIsSmartLockEnabled
                        method when building your sign-in Intent:
                         */
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
//                        .setTheme(R.style.Sup)
                        .build(),
                RC_SIGN_IN);
    }
}
