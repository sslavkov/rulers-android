package com.bgrulers.rulerslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bgrulers.activity.RulerDetailScrollingActivity;
import com.bgrulers.adapter.RulerRecycleViewAdapter;
import com.bgrulers.bulgarianrulers.R;
import com.bgrulers.listener.RecyclerItemListener;
import com.bgrulers.model.Ruler;
import org.apache.commons.lang3.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sslavkov on 3/20/17.
 */

public class RulerListFragment extends Fragment implements RulerListContract.View {

	private RulerListContract.Presenter mPresenter;
	private RulerRecycleViewAdapter mRulerRecycleViewAdapter;

	// Bind Views
	@BindView(R.id.progressBar)
	ProgressBar mProgressBar;

	@BindView(R.id.ruler_recyclerview)
	RecyclerView mRulerRecyclerView;

	public RulerListFragment() {
		// requires empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		mPresenter.start();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.ruler_list_fragment, container, false);
		ButterKnife.bind(this, rootView);

		setUpRecyclerView();
		return rootView;
	}

	public static RulerListFragment newInstance() {
		return new RulerListFragment();
	}

	@Override
	public void setPresenter(@NonNull RulerListContract.Presenter presenter) {
		mPresenter = checkNotNull(presenter);
	}

	@Override
	public void showRulers(List<Ruler> rulers) {
		populateRulerCardListView(rulers);
	}

	@Override
	public void showLoading(boolean isLoading) {
		mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
	}

	@Override
	public void startRulerDetail(Ruler ruler) {
		startRulerDetailActivity(ruler, null);
	}

	// Sets up the recyclerview
	private void setUpRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRulerRecyclerView.setLayoutManager(linearLayoutManager);
	}

	// sets up the recyclerviewadatper and populates the cards
	private void populateRulerCardListView(List<Ruler> rulers) {
		mRulerRecycleViewAdapter = new RulerRecycleViewAdapter(rulers);
		mRulerRecyclerView.setAdapter(mRulerRecycleViewAdapter);
		mRulerRecyclerView.addOnItemTouchListener(
				new RecyclerItemListener(this.getContext(), mRulerRecyclerView, new RecyclerItemListener.RecyclerTouchListener() {

					@Override
					public void onClickItem(View v, int position) {
						startRulerDetailActivity(position, v);
					}

					@Override
					public void onLongClickItem(View v, int position) {
						Snackbar.make(v, "Long pressed an item", Snackbar.LENGTH_SHORT).show();
					}
				}));
	}

	public void startRulerDetailActivity(Ruler ruler, View view) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y");

		String rulerTitle = WordUtils.capitalizeFully(ruler.getTitle().getTitleType().toString());
		String rulerName = ruler.getName();
		String reignStart = simpleDateFormat.format(ruler.getReignStart());
		String reignEnd = simpleDateFormat.format(ruler.getReignEnd());

		Intent intent = new Intent(this.getContext(), RulerDetailScrollingActivity.class);
		// setting up intent extras
		intent.putExtra(RulerDetailScrollingActivity.RULER_ID, ruler.getId());
		intent.putExtra(RulerDetailScrollingActivity.RULER_TITLE, rulerTitle);
		intent.putExtra(RulerDetailScrollingActivity.RULER_NAME, rulerName);
		intent.putExtra(RulerDetailScrollingActivity.RULER_TITLE_AND_NAME, rulerTitle + " " + rulerName);
		intent.putExtra(RulerDetailScrollingActivity.RULER_REIGN_START, reignStart);
		intent.putExtra(RulerDetailScrollingActivity.RULER_REIGN_END, reignEnd);
		intent.putExtra(RulerDetailScrollingActivity.RULER_INFO, ruler.getInformation());

		if (view != null) {
			// activity started from listview
			ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.getActivity(), view.findViewById(R.id.card_feed_body_text), "transition_ruler_info");
			startActivity(intent, options.toBundle());
		} else {
			startActivity(intent);
		}
	}

	private void startRulerDetailActivity(int position, View view) {
		startRulerDetailActivity(mPresenter.getRulerFromPosition(position), view);
	}
}
