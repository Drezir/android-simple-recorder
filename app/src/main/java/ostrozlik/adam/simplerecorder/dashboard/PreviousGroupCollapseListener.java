package ostrozlik.adam.simplerecorder.dashboard;

import android.widget.ExpandableListView;

public class PreviousGroupCollapseListener implements ExpandableListView.OnGroupExpandListener {

    private int previousGroup;
    private final ExpandableListView expandableListView;

    public PreviousGroupCollapseListener(ExpandableListView expandableListView) {
        this.expandableListView = expandableListView;
        this.previousGroup = -1;
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if (previousGroup == -1) {
            previousGroup = groupPosition;
        } else if (previousGroup != groupPosition) {
            expandableListView.collapseGroup(previousGroup);
            previousGroup = groupPosition;
        }
    }
}
