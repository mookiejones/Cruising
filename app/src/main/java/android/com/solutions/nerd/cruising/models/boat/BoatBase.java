package android.com.solutions.nerd.cruising.models.boat;


import android.com.solutions.nerd.cruising.models.Vehicle;

/**
 * Created by mookie on 2/7/17.
 * for Nerd.Solutions
 */

public class BoatBase extends Vehicle {

    public Float getBeam(){return width;}
    public void setBeam(Float value){width=value;}
    protected Float draft;

    public Float getDraft(){return draft;}
    public void setDraft(Float draft){this.draft=draft;}

}
