// Generated code from Butter Knife. Do not modify!
package bbct.android.common.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BaseballCardDetails$$ViewInjector {
  public static void inject(Finder finder, final bbct.android.common.activity.BaseballCardDetails target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427333, "field 'autographCheckBox'");
    target.autographCheckBox = (android.widget.CheckBox) view;
    view = finder.findRequiredView(source, 2131427335, "field 'conditionSpinner'");
    target.conditionSpinner = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131427334, "field 'brandText'");
    target.brandText = (android.widget.AutoCompleteTextView) view;
    view = finder.findRequiredView(source, 2131427348, "field 'yearText'");
    target.yearText = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427338, "field 'numberText'");
    target.numberText = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427347, "field 'valueText'");
    target.valueText = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427336, "field 'countText'");
    target.countText = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427339, "field 'playerNameText'");
    target.playerNameText = (android.widget.AutoCompleteTextView) view;
    view = finder.findRequiredView(source, 2131427345, "field 'teamText'");
    target.teamText = (android.widget.AutoCompleteTextView) view;
    view = finder.findRequiredView(source, 2131427340, "field 'playerPositionSpinner'");
    target.playerPositionSpinner = (android.widget.Spinner) view;
  }

  public static void reset(bbct.android.common.activity.BaseballCardDetails target) {
    target.autographCheckBox = null;
    target.conditionSpinner = null;
    target.brandText = null;
    target.yearText = null;
    target.numberText = null;
    target.valueText = null;
    target.countText = null;
    target.playerNameText = null;
    target.teamText = null;
    target.playerPositionSpinner = null;
  }
}
