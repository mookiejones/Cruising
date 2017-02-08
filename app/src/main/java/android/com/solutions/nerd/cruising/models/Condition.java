package android.com.solutions.nerd.cruising.models;

/**
 * Created by mookie on 2/7/17.
 * for Nerd.Solutions
 */

public class Condition {
    private static final int MAX=10;
    private static final int MIN=1;
    private int value=MIN;
    public int getValue(){return value;}
    public void setValue(int value){this.value=value;}

}
