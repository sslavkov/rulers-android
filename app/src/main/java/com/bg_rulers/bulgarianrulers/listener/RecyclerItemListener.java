package com.bg_rulers.bulgarianrulers.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class RecyclerItemListener implements RecyclerView.OnItemTouchListener {
	private RecyclerTouchListener listener;
	private GestureDetector gd;

	public interface RecyclerTouchListener {
		void onClickItem(View v, int position);

		void onLongClickItem(View v, int position);
	}

	public RecyclerItemListener(Context context, final RecyclerView recyclerView, final RecyclerTouchListener listener) {
		this.listener = listener;
		gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public void onLongPress(MotionEvent e) {
				// We find the view
				View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
				// Notify the even
				listener.onLongClickItem(view, recyclerView.getChildAdapterPosition(view));
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
				// Notify the even
				listener.onClickItem(view, recyclerView.getChildAdapterPosition(view));
				return true;
			}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {
		View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
		return (child != null && gd.onTouchEvent(e));
	}

	@Override
	public void onTouchEvent(RecyclerView recyclerView, MotionEvent e) {
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
	}
}