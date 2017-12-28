package com.example.android.newsappstage2project;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
/**
 * Created by Emanuele on 28/12/2017.
 */
public class CustomGridLayoutManager extends GridLayoutManager {
    private int columnWidth;
    private boolean columnWidthChanged = true;
    private final float VALUE = 48;
    /**
     * Construct a new Custom GridLayoutManager
     *
     * @param context     context reference
     * @param columnWidth the desire width for the column
     */
    public CustomGridLayoutManager(Context context, int columnWidth) {
        super(context, 1);
        setColumnWidth(checkedColumnWidth(context, columnWidth));
    }
    /**
     * Returns a floating point value for the width
     *
     * @param context     context reference
     * @param columnWidth the desire width for the column
     * @return floating point value for the width
     */
    private int checkedColumnWidth(Context context, int columnWidth) {
        if (columnWidth <= 0) {
            columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, VALUE, context.getResources().getDisplayMetrics());
        }
        return columnWidth;
    }
    /**
     * Set a new value for column width
     *
     * @param newColumnWidth the new column width
     */
    private void setColumnWidth(int newColumnWidth) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth;
            columnWidthChanged = true;
        }
    }
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (columnWidthChanged && columnWidth > 0) {
            int totalSpace;
            if (getOrientation() == VERTICAL) {
                totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
            } else {
                totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            }
            int spanCount = Math.max(1, totalSpace / columnWidth);
            setSpanCount(spanCount);
            columnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}