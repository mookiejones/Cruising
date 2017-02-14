package android.com.solutions.nerd.cruising.ui.activities;

import android.com.solutions.nerd.cruising.interfaces.IVehicle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ActivityHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

/**
 * Created by mookie on 1/28/17.
 * for Nerd.Solutions
 */

public abstract class BaseActivity  extends AppCompatActivity {
    private static final String TAG=BaseActivity.class.getSimpleName();

    private IVehicle vehicle;
    public IVehicle getVehicle(){return vehicle;}
    public void setVehicle(IVehicle vehicle){this.vehicle=vehicle;}
    private boolean destroyed;

    protected abstract int getLayoutResId();

    public boolean isDestroyed() {
        return destroyed;
    }

    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;


    protected ActivityHelper mActivityHelper;
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
//            startActivityForResult(
//                    // Get an instance of AuthUI based on top of app
//                    AuthUI.getInstance().createSignInIntentBuilder().build(),
//                    RC_SIGN_IN
//            );

            // Not signed in
        }
    }

    private void SignIn(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }
}
