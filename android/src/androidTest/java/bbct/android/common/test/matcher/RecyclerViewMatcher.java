/*
 * See https://github.com/dannyroa/espresso-samples
 */

package bbct.android.common.test.matcher;

import android.content.res.Resources;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMatcher {
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private final int recyclerViewId;

    private RecyclerViewMatcher(int recyclerViewId) {
        this.recyclerViewId = recyclerViewId;
    }

    public Matcher<View> atPosition(final int position) {
        return atPositionOnView(position, -1);
    }

    public Matcher<View> atPositionOnView(final int position, final int targetViewId) {
        return new TypeSafeMatcher<View>() {
            Resources resources = null;
            View childView;
            View targetView;

            public void describeTo(Description description) {
                String idDescription = Integer.toString(recyclerViewId);
                if (this.resources != null) {
                    try {
                        idDescription = this.resources.getResourceName(recyclerViewId);
                    } catch (Resources.NotFoundException var4) {
                        idDescription = String.format(
                            "%s (resource name not found)",
                            recyclerViewId
                        );
                    }
                }

                description.appendText(String.format("with id: %s", idDescription));
            }

            public boolean matchesSafely(View view) {
                this.resources = view.getResources();

                if (childView == null) {
                    RecyclerView recyclerView =
                        view.getRootView().findViewById(recyclerViewId);
                    if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                        RecyclerView.ViewHolder viewHolder =
                            recyclerView.findViewHolderForAdapterPosition(position);

                        if (viewHolder != null) {
                            childView = viewHolder.itemView;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }

                if (targetViewId == -1) {
                    return view == childView;
                } else {
                    if (targetView == null) {
                        targetView = childView.findViewById(targetViewId);
                    }
                    return view == targetView;
                }
            }
        };
    }
}
