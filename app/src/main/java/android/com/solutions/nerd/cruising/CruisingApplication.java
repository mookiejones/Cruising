package android.com.solutions.nerd.cruising;

/**
 * Created by cberman on 3/3/2017.
 */

public class CruisingApplication extends CruisingBaseApplication{
    @Override
    protected AppComponent initDagger() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .developerModule(new ReleaseDeveloperModule())
                .build();
    }
}
