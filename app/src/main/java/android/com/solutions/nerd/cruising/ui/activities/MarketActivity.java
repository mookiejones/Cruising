package android.com.solutions.nerd.cruising.ui.activities;

import android.com.solutions.nerd.cruising.R;
import android.com.solutions.nerd.cruising.interfaces.IMarketCategory;
import android.com.solutions.nerd.cruising.ui.fragments.MarketCategoryFragment;
import android.com.solutions.nerd.cruising.ui.views.MarketCategoryView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mookie on 2/7/17.
 * for Nerd.Solutions
 */

public class MarketActivity extends BaseActivity {

    private List<IMarketCategory> marketCategoryFragments = new ArrayList<>();
    public List<IMarketCategory> getMarketCategoryFragments(){return marketCategoryFragments;}
    public void setMarketCategoryFragments(List<IMarketCategory> categories){marketCategoryFragments=categories;}

    private View mView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_layout);

        GridView view = (GridView)findViewById(R.id.market_items);

        ArrayAdapter<IMarketCategory> adapter= new ArrayAdapter<IMarketCategory>(this,
                R.layout.market_layout_item,marketCategoryFragments);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }
}
