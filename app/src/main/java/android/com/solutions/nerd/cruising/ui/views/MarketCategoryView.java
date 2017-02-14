package android.com.solutions.nerd.cruising.ui.views;

import android.com.solutions.nerd.cruising.ui.fragments.MarketCategoryFragment;
import android.content.Context;
import android.view.View;

/**
 * Created by mookie on 2/13/17.
 * for Nerd.Solutions
 */

public class MarketCategoryView extends View {
    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public MarketCategoryView(Context context) {
        super(context);
    }

    private MarketCategoryFragment marketCategoryFragment;
    public void setMarketItem(MarketCategoryFragment fragment){
        marketCategoryFragment=fragment;

    }
}
