package bbct.android.premium.provider;

import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardProvider;

public class PremiumProvider extends BaseballCardProvider {

    static {
        uriMatcher.addURI(BaseballCardContract.PREMIUM_AUTHORITY,
                BaseballCardContract.TABLE_NAME, ALL_CARDS);
        uriMatcher.addURI(BaseballCardContract.PREMIUM_AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/#", CARD_ID);
        uriMatcher.addURI(BaseballCardContract.PREMIUM_AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/distinct", DISTINCT);
    }

}
