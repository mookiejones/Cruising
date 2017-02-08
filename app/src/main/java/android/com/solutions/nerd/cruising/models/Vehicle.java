package android.com.solutions.nerd.cruising.models;

import android.com.solutions.nerd.cruising.interfaces.IVehicle;

/**
 * Created by mookie on 2/7/17.
 * for Nerd.Solutions
 */

public class Vehicle extends Model implements IVehicle

{

    public Vehicle(){
        condition=new Condition();
    }

    private Float price;
    @Override
    public Float getPrice(){return price;}
    @Override
    public void setPrice(Float price){this.price=price;}

    private int year;
    @Override
    public int getYear(){
        return year;
    }
    private void setYear(int year){
        this.year=year;
    }

    private Condition condition;
    @Override
    public Condition getCondition(){return condition;}
    @Override
    public void setCondition(Condition condition){this.condition=condition;}

    protected Float length;


    @Override
    public Float getLength() {
        return length;
    }


    @Override
    public void setLength(Float length) {
        this.length = length;
    }

    @Override
    public Float getWidth() {
        return width;
    }

    @Override
    public void setWidth(Float width) {
        this.width = width;
    }

    protected Float width;
}
