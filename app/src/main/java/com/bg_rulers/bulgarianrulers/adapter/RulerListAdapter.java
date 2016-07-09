package com.bg_rulers.bulgarianrulers.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bg_rulers.bulgarianrulers.R;
import com.bg_rulers.bulgarianrulers.model.Ruler;

import org.apache.commons.lang3.text.WordUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class RulerListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Ruler> rulers;
//	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	private TextView reignStart;
	private TextView reignEnd;
	private TextView title;
	private TextView name;
	
	public RulerListAdapter(Activity activity, List<Ruler> rulers) {
		this.activity = activity;
		this.rulers = rulers;
	}
	
	@Override
	public int getCount() {
		return rulers.size();
	}
	
	@Override
	public Object getItem(int location) {
		return rulers.get(location);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View itemView, ViewGroup parent) {
		
		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (itemView == null)
			itemView = inflater.inflate(R.layout.ruler_list_item, null);
		
//		if (imageLoader == null)
//			imageLoader = AppController.getInstance().getImageLoader();
//		NetworkImageView thumbNail = (NetworkImageView) itemView
//																.findViewById(R.id.thumbnail);

		setUpViewFields(itemView);
		
		// getting movie data for the row
		Ruler ruler = rulers.get(position);

		DateFormat simpleDateFormat = new SimpleDateFormat("y");
		reignStart.setText(simpleDateFormat.format(ruler.getReignStart()));
		reignEnd.setText(" - " + simpleDateFormat.format(ruler.getReignEnd()));
		title.setText(WordUtils.capitalizeFully(ruler.getTitle().getTitleType().toString()));
		name.setText(ruler.getName().toString());
		
		// thumbnail image
//		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

		return itemView;
	}

	private void setUpViewFields(View convertView) {
		reignStart = (TextView) convertView.findViewById(R.id.ruler_list_item_reign_start);
		reignEnd = (TextView) convertView.findViewById(R.id.ruler_list_item_reign_end);
		title = (TextView) convertView.findViewById(R.id.ruler_list_item_title);
		name = (TextView) convertView.findViewById(R.id.ruler_list_item_name);
	}
	
}