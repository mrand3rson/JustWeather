package com.example.justweather.ui.adapters

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Andrei on 04.05.2018.
 */
class VerticalSpaceItemDecorator(val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State?) {
        outRect.bottom = verticalSpaceHeight
    }
}