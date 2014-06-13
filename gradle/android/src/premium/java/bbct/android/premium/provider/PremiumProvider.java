package bbct.android.premium.provider;

import android.content.Context;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardProvider;
import bbct.android.common.provider.BaseballCardSQLHelper;

public class PremiumProvider extends BaseballCardProvider {

    static {
        uriMatcher.addURI(BaseballCardContract.PREMIUM_AUTHORITY,
                BaseballCardContract.TABLE_NAME, ALL_CARDS);
        uriMatcher.addURI(BaseballCardContract.PREMIUM_AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/#", CARD_ID);
        uriMatcher.addURI(BaseballCardContract.PREMIUM_AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/distinct", DISTINCT);
    }

    @Override
    protected BaseballCardSQLHelper getSQLHelper(Context context) {
        return new PremiumSQLHelper(context);
    }

}
