package com.bg_rulers.bulgarianrulers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bg_rulers.bulgarianrulers.R;
import com.bg_rulers.bulgarianrulers.model.Ruler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class RulerRecycleViewAdapter extends RecyclerView.Adapter<RulerRecycleViewAdapter.RulerViewHolder> {
	private List<Ruler> rulers;


	// Provide a suitable constructor (depends on the kind of dataset)
	public RulerRecycleViewAdapter(List<Ruler> rulers) {
		this.rulers = rulers;
	}


	@Override
	public RulerRecycleViewAdapter.RulerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ruler_card, parent, false);

		return new RulerViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RulerRecycleViewAdapter.RulerViewHolder holder, int position) {
		Ruler ruler = rulers.get(position);
		DateFormat simpleDateFormat = new SimpleDateFormat("y");
		holder.reignStart.setText(simpleDateFormat.format(ruler.getReignStart()));
		holder.reignEnd.setText(" - " + simpleDateFormat.format(ruler.getReignEnd()));
		holder.title.setText(ruler.getTitle().getTitleType().toString());
		holder.name.setText(ruler.getName().toString());
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return rulers.size();
	}

	// View Holder Class
	public static class RulerViewHolder extends RecyclerView.ViewHolder {
		protected TextView reignStart;
		protected TextView reignEnd;
		protected TextView title;
		protected TextView name;



		// Provide a reference to the views for each data item
		// Complex data items may need more than one view per item, and
		// you provide access to all the views for a data item in a view holder
		public RulerViewHolder(View itemView) {
			super(itemView);
			reignStart = (TextView) itemView.findViewById(R.id.ruler_card_reign_start);
			reignEnd = (TextView) itemView.findViewById(R.id.ruler_card_reign_end);
			title = (TextView) itemView.findViewById(R.id.ruler_card_title);
			name = (TextView) itemView.findViewById(R.id.ruler_card_name);
		}
	}
}