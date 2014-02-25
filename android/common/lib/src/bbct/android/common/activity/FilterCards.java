package bbct.android.common.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import bbct.android.common.R;

public class FilterCards extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.filter_cards);
    }
}