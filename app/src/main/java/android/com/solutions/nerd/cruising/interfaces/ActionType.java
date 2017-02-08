package android.com.solutions.nerd.cruising.interfaces;

/**
 * Created by mookie on 1/28/17.
 * for Nerd.Solutions
 */

public final class ActionType {
    /**
     * Specifies the Action that was taken when data changes
     */
    /**
     * The model called {@link Model#save()}
     */
    public static final int SAVE=1;
    /**
     * The model called {@link Model#insert()}
     */
    public static final int INSERT=2;
    /**
     * The model called {@link Model#update()}
     */
    public static final int UPDATE=3;
    /**
     * The model called {@link Model#delete()}
     */
    public static final int DELETE=4;
    /**
     * The model was changed. used in prior to {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR1}
     */
    public static final int CHANGE=5;



}
