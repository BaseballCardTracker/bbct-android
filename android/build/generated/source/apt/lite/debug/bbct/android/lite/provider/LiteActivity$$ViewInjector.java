// Generated code from Butter Knife. Do not modify!
package bbct.android.lite.provider;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LiteActivity$$ViewInjector {
  public static void inject(Finder finder, final bbct.android.lite.provider.LiteActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427332, "field 'adViewContainer'");
    target.adViewContainer = (android.view.ViewGroup) view;
    view = finder.findRequiredView(source, 2131427461, "field 'premiumText'");
    target.premiumText = (android.widget.TextView) view;
  }

  public static void reset(bbct.android.lite.provider.LiteActivity target) {
    target.adViewContainer = null;
    target.premiumText = null;
  }
}
