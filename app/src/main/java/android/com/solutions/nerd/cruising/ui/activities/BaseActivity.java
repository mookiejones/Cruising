package android.com.solutions.nerd.cruising.ui.activities;

import android.com.solutions.nerd.cruising.BuildConfig;
import android.com.solutions.nerd.cruising.R;
import android.com.solutions.nerd.cruising.interfaces.IVehicle;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.firebase.ui.auth.ui.ActivityHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

/**
 * Created by mookie on 1/28/17.
 * for Nerd.Solutions
 */

public abstract class BaseActivity  extends AppCompatActivity {
    private static final String TAG=BaseActivity.class.getSimpleName();
    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    private static final String UNCHANGED_CONFIG_VALUE = "CHANGE-ME";
    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";


    protected View mView;
    ScaleAnimation shrinkAnim;
    protected ActivityHelper mActivityHelper;
    private IVehicle vehicle;
    private boolean destroyed;

    public IVehicle getVehicle(){return vehicle;}

    public void setVehicle(IVehicle vehicle){this.vehicle=vehicle;}

    protected abstract int getLayoutResId();

    public boolean isDestroyed() {
        return destroyed;
    }
    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG,"Signed in");


        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }


        Snackbar.make(mView,"Unknown Response",Snackbar.LENGTH_SHORT).show();



    }

    private void showSnackbar(String content){

        View view = findViewById(R.id.overlayFrameLayout);
        if(view ==null)
            setContentView(R.layout.activity_main);
        view = findViewById(R.id.main_content);

        Snackbar.make(view,content,Snackbar.LENGTH_SHORT).show();
    }
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
           showSnackbar("Signed in");
            showContent();
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar("Sign in cancelled");
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar("No internet connection");
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar("An unknown error occurred");
                return;
            }
            showSnackbar("Unknown response from AuthUI sign-in");
        }


    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Check firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Get Current user
        FirebaseUser user=auth.getCurrentUser();
        //scale animation to shrink floating actionbar
        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


        if(BuildConfig.DEBUG && user!=null){
            Log.w(TAG,"Logging user out");
            user.delete();
            auth= FirebaseAuth.getInstance();
        }

        if(auth.getCurrentUser()!=null){
            showContent();

            Log.d(TAG,"Allready signed in");
        }else{
            Log.d(TAG,"creating sign in");
            SignIn();
//            startActivity(SigninActivity.createIntent(this,null));
//            finish();
          //  SignIn();
        }
    }

    private void showContent(){
        Log.d(TAG,"showContent");
        setContentView(R.layout.activity_main);

    }
    public static final String TOS_URL="https://www.google.com/policies/terms/";
    private void SignIn(){


        startActivityForResult(


                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(AuthUI.getDefaultTheme())
                        .setLogo(R.drawable.firebase_auth_120dp)
                        .setTosUrl(TOS_URL)


                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                ,new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()

                        ))

                        /*
                        By default, FirebaseUI uses Smart Lock for Passwords to store the user's credentials
                        and automatically sign users into your app on subsequent attempts. Using Smart Lock is
                        recommended to provide the best user experience, but in some cases you may want to disable
                        Smart Lock for testing or development. To disable Smart Lock, you can use the setIsSmartLockEnabled
                        method when building your sign-in Intent:
                         */
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)

                        .build(),
                RC_SIGN_IN);
    }

}
