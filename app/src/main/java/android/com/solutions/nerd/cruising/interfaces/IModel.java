package android.com.solutions.nerd.cruising.interfaces;

/**
 * Created by mookie on 1/28/17.
 * for Nerd.Solutions
 */

public interface IModel {
    /**
     * Saves the object in the DB.
     */
    void save();

    /**
     * Deletes the object in the DB
     */
    void delete();

    /**
     * Updates an object in the DB
     */
    void update();

    /**
     * Inserts the object into the DB
     */
    void insert();

    /**
     * Returns whether this Model exists or not
     *
     * @return
     */
    boolean exists();

}
