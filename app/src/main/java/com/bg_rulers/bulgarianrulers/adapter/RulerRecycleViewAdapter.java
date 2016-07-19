package com.bg_rulers.bulgarianrulers.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bg_rulers.bulgarianrulers.R;
import com.bg_rulers.bulgarianrulers.model.Ruler;
import org.apache.commons.lang3.text.WordUtils;

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
		// TODO - see if only 1 view for reign is needed and populated with start+end
		Ruler ruler = rulers.get(position);
		DateFormat simpleDateFormat = new SimpleDateFormat("y");
        Resources resources = holder.itemView.getResources();

//		holder.reignStart.setText(simpleDateFormat.format(ruler.getReignStart()));
//		holder.reignEnd.setText(simpleDateFormat.format(ruler.getReignEnd()));
//		holder.title.setText(ruler.getTitle().getTitleType().toString());
//		holder.name.setText(ruler.getName().toString());
        String title = WordUtils.capitalizeFully(ruler.getTitle().getTitleType().toString());

        holder.reignView.setText(resources.getString(R.string.reign_range, simpleDateFormat.format(ruler.getReignStart()), simpleDateFormat.format(ruler.getReignEnd())));
        holder.titleAndNameView.setText(resources.getString(R.string.two_strings, title, ruler.getName().toString()));
        holder.info.setText(ruler.getInformation());
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return rulers.size();
	}

	// View Holder Class
	public static class RulerViewHolder extends RecyclerView.ViewHolder {
		// TODO - can also make one CardView (the holder)
		// and then in onBindViewHolder - set everything
		protected TextView reignStart;
		protected TextView reignEnd;
		protected TextView title;
		protected TextView name;

        protected TextView reignView;
        protected TextView titleAndNameView;
        protected TextView info;

		// Provide a reference to the views for each data item
		// Complex data items may need more than one view per item, and
		// you provide access to all the views for a data item in a view holder
		public RulerViewHolder(View itemView) {
			super(itemView);
//			reignStart = (TextView) itemView.findViewById(R.id.ruler_card_reign_start);
//			reignEnd = (TextView) itemView.findViewById(R.id.ruler_card_reign_end);
//			title = (TextView) itemView.findViewById(R.id.ruler_card_title);
//			name = (TextView) itemView.findViewById(R.id.ruler_card_name);

            reignView = (TextView) itemView.findViewById(R.id.card_ruler_reign_range);
            titleAndNameView = (TextView) itemView.findViewById(R.id.card_ruler_title_and_name);
            info = (TextView) itemView.findViewById(R.id.card_ruler_info);
		}
	}
}