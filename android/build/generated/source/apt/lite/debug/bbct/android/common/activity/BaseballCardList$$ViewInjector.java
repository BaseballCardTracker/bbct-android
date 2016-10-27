// Generated code from Butter Knife. Do not modify!
package bbct.android.common.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BaseballCardList$$ViewInjector {
  public static void inject(Finder finder, final bbct.android.common.activity.BaseballCardList target, Object source) {
    View view;
    view = finder.findRequiredView(source, 16908292, "field 'emptyList'");
    target.emptyList = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 16908298, "field 'listView'");
    target.listView = (android.widget.ListView) view;
  }

  public static void reset(bbct.android.common.activity.BaseballCardList target) {
    target.emptyList = null;
    target.listView = null;
  }
}
