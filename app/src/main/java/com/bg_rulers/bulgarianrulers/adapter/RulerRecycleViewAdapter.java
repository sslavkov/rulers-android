package com.bg_rulers.bulgarianrulers.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bg_rulers.bulgarianrulers.R;
import com.bg_rulers.bulgarianrulers.model.Ruler;

import org.apache.commons.lang3.text.WordUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RulerRecycleViewAdapter extends RecyclerView.Adapter<RulerRecycleViewAdapter.RulerViewHolder> {

	private List<Ruler> rulers;

	// Provide a suitable constructor (depends on the kind of dataset)
	public RulerRecycleViewAdapter(List<Ruler> rulers) {
		this.rulers = rulers;
	}


	@Override
	public RulerRecycleViewAdapter.RulerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_feed, parent, false);

		return new RulerViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RulerRecycleViewAdapter.RulerViewHolder holder, int position) {
		Ruler ruler = rulers.get(position);
		DateFormat simpleDateFormat = new SimpleDateFormat("y");
        Resources resources = holder.itemView.getResources();

        String title = WordUtils.capitalizeFully(ruler.getTitle().getTitleType().toString());

        holder.reignView.setText(resources.getString(R.string.reign_range, simpleDateFormat.format(ruler.getReignStart()), simpleDateFormat.format(ruler.getReignEnd())));
		String rulerName = ruler.getName();
		holder.titleAndNameView.setText(resources.getString(R.string.two_strings, title, rulerName));
        holder.infoView.setText(ruler.getInformation());

		ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
		// generate random color
		int randomColor = generator.getRandomColor();
		// generate color based on a key (same key returns the same color), useful for list/grid views
//		int color2 = generator.getColor("user@gmail.com");

		// declare the builder object once.
		TextDrawable drawable = TextDrawable.builder().beginConfig().toUpperCase().endConfig().buildRound(WordUtils.initials(String.valueOf(rulerName.charAt(0))), holder.colorPrimary);
		holder.imageView.setImageDrawable(drawable);
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return rulers.size();
	}

	// View Holder Class
	public static class RulerViewHolder extends RecyclerView.ViewHolder {
		// and then in onBindViewHolder - set everything

        @BindView(R.id.card_feed_header_info) TextView reignView;
        @BindView(R.id.card_feed_title) TextView titleAndNameView;
        @BindView(R.id.card_feed_body_text) TextView infoView;
		@BindView(R.id.card_feed_image) ImageView imageView;

		@BindColor(R.color.colorAccent) int colorAccent;
		@BindColor(R.color.colorPrimary) int colorPrimary;

		// Provide a reference to the views for each data item
		// Complex data items may need more than one view per item, and
		// you provide access to all the views for a data item in a view holder
		public RulerViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}