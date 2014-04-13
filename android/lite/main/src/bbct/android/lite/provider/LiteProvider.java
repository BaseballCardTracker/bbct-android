package bbct.android.lite.provider;

import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardProvider;

public class LiteProvider extends BaseballCardProvider {

    static {
        uriMatcher.addURI(BaseballCardContract.LITE_AUTHORITY,
                BaseballCardContract.TABLE_NAME, ALL_CARDS);
        uriMatcher.addURI(BaseballCardContract.LITE_AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/#", CARD_ID);
        uriMatcher.addURI(BaseballCardContract.LITE_AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/distinct", DISTINCT);
    }

}
