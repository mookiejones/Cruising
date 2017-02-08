package android.com.solutions.nerd.cruising.interfaces;

import android.com.solutions.nerd.cruising.models.Condition;

/**
 * Created by mookie on 2/7/17.
 * for Nerd.Solutions
 */

public interface IVehicle {
    Float getPrice();

    void setPrice(Float price);

    int getYear();

    Condition getCondition();

    void setCondition(Condition condition);

    Float getLength();

    void setLength(Float length);

    Float getWidth();

    void setWidth(Float width);
}
