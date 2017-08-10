package com.bgrulers.rulerslist;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bgrulers.activity.MainActivity;
import com.bgrulers.bulgarianrulers.R;
import com.bgrulers.util.ActivityUtils;

/**
 * Created by sslavkov on 3/20/17.
 */

public class RulerListActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener  {

	private SearchView searchView;
	private MenuItem searchMenuItem;

	private RulerListPresenter mRulerListPresenter;

	// bind views
	@BindView(R.id.toolbar)
	Toolbar mToolbar;

	@BindView(R.id.fab)
	FloatingActionButton mFab;

	@BindView(R.id.drawer_layout)
	DrawerLayout mDrawer;

	@BindView(R.id.nav_view)
	NavigationView mNavigationView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ruler_list_activity);
		ButterKnife.bind(this);

		setSupportActionBar(mToolbar);

		mFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawer.setDrawerListener(toggle);
		toggle.syncState();

		mNavigationView.setNavigationItemSelectedListener(this);

		// Activity responsible for creating View and Presenter
		// RulerListFragment is the View from MVP
		RulerListFragment rulerListFragment =
				(RulerListFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
		if (rulerListFragment == null) {
			// Create the fragment which is the VIEW
			rulerListFragment = RulerListFragment.newInstance();
			ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), rulerListFragment, R.id.content_frame);
		}

		// Creating the Presenter
		mRulerListPresenter = new RulerListPresenter(rulerListFragment);

	}

	// Needed for Search
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		switch (intent.getAction()) {
			case Intent.ACTION_SEARCH:
				String query = intent.getStringExtra(SearchManager.QUERY);
				Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();

				break;
			case Intent.ACTION_VIEW:
				Uri detailUri = intent.getData();
				String rulerId = detailUri.getLastPathSegment();

				// TODO - handle starting of RulerDetailActivity from within Search
				mRulerListPresenter.startDetailsActivity(Long.valueOf(rulerId));
				searchMenuItem.collapseActionView();
				break;
		}
	}

	@Override
	public void onBackPressed() {
		if (mDrawer.isDrawerOpen(GravityCompat.START)) {
			mDrawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	// SEARCH
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		searchMenuItem = menu.findItem(R.id.search);

		searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
		searchView.setOnQueryTextListener(this);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, MainActivity.class)));
		searchView.setIconifiedByDefault(false);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		switch (id) {
			case R.id.nav_camera:
				// Handle the camera action
				break;
			case R.id.nav_gallery:

				break;
			case R.id.nav_slideshow:

				break;
			case R.id.nav_manage:

				break;
			case R.id.nav_share:

				break;
			case R.id.nav_send:

				break;
		}

		mDrawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
}
