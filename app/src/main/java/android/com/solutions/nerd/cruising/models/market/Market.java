package android.com.solutions.nerd.cruising.models.market;

import android.com.solutions.nerd.cruising.interfaces.IMarketCategory;

import java.util.List;

/**
 * Created by mookie on 2/13/17.
 * for Nerd.Solutions
 */

public class Market {
    private List<IMarketCategory> marketCategorys;
    public List<IMarketCategory> getMarketCategorys(){return marketCategorys;}
    public void setMarketCategorys(List<IMarketCategory> categorys){marketCategorys=categorys;}
}
