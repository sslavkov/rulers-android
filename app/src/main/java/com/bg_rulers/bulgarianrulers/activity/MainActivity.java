package com.bg_rulers.bulgarianrulers.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bg_rulers.bulgarianrulers.R;
import com.bg_rulers.bulgarianrulers.adapter.RulerListAdapter;
import com.bg_rulers.bulgarianrulers.adapter.RulerRecycleViewAdapter;
import com.bg_rulers.bulgarianrulers.model.Ruler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private List<Ruler> rulers = null;
    private Map<Long, Ruler> rulerMap = null;
    private RecyclerView  rulerRecyclerView;
    private RulerRecycleViewAdapter rulerRecycleViewAdapter;
    private SearchView searchView;
    private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        setUpRecyclerView(); // using recyclerview

        // get rulers here and show list
        if (rulers == null) {
            fetchRulersAndPopulateView();
        } else {
//            populateRulerCardListView(rulers); // using cards
            populateRulerListView(rulers);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (Intent.ACTION_SEARCH.equals(action)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(action)) {
            Uri detailUri = intent.getData();
            String rulerId = detailUri.getLastPathSegment();

            startRulerDetailActivity(getRulerFromMap(Long.valueOf(rulerId)), null);
            searchMenuItem.collapseActionView();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // ====== Begin Private Methods ======

    private void populateRulerListView(List<Ruler> rulers) {
        final ListView rulersListView = (ListView) findViewById(R.id.ruler_list);
        RulerListAdapter arrayAdapter = new RulerListAdapter(this, rulers);
        rulersListView.setAdapter(arrayAdapter);
        rulersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Ruler ruler = (Ruler) rulersListView.getItemAtPosition(position);
                startRulerDetailActivity(ruler, view);
            }
        });
    }

    public void startRulerDetailActivity(Ruler ruler, View view) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y");

        String rulerTitle = WordUtils.capitalizeFully(ruler.getTitle().getTitleType().toString());
        String rulerName = ruler.getName();
        String reignStart = simpleDateFormat.format(ruler.getReignStart());
        String reignEnd = simpleDateFormat.format(ruler.getReignEnd());

        Intent intent = new Intent(MainActivity.this, RulerDetailScrollingActivity.class);
        // setting up intent extras
        intent.putExtra(RulerDetailScrollingActivity.RULER_ID, ruler.getId());
        intent.putExtra(RulerDetailScrollingActivity.RULER_TITLE, rulerTitle);
        intent.putExtra(RulerDetailScrollingActivity.RULER_NAME, rulerName);
        intent.putExtra(RulerDetailScrollingActivity.RULER_TITLE_AND_NAME, rulerTitle + " " + rulerName);
        intent.putExtra(RulerDetailScrollingActivity.RULER_REIGN_START, reignStart);
        intent.putExtra(RulerDetailScrollingActivity.RULER_REIGN_END, reignEnd);

        if (view != null) {
            // activity started from listview
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, view.findViewById(R.id.ruler_list_item_reign), "transition_ruler_reign");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    private void fetchRulersAndPopulateView() {
        // TODO - make environment specific urls
        String url = "https://rulers-production.herokuapp.com/api/rulers?projection=rulerList&size=1000";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
               (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       rulers = getRulersListFromJson(response);
//                       populateRulerCardListView(rulers); // using cards
                       populateRulerListView(rulers); // using list

                   }
               }, new Response.ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError error) {
                       error.printStackTrace();
                   }
               });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private List<Ruler> getRulersListFromJson(JSONObject response) {
        ObjectMapper mapper = new ObjectMapper();
        List<Ruler> rulers;
        try {
            JSONObject o = (JSONObject) response.get("_embedded");
            JSONArray rulersJson = o.getJSONArray("rulers");
            rulers = mapper.readValue(rulersJson.toString(), TypeFactory.defaultInstance().constructCollectionType(List.class, Ruler.class));
            return rulers;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private Ruler getRulerFromMap(Long rulerId) {
        if (rulerMap == null) {
            rulerMap = initRulerMap();
        }

        return rulerMap.get(rulerId);
    }

    private Map<Long, Ruler> initRulerMap() {
        rulerMap = new HashedMap<>();
        for (Ruler ruler : rulers) {
            rulerMap.put(ruler.getId(), ruler);
        }
        return rulerMap;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // Sets up the recyclerview
//    private void setUpRecyclerView() {
//        rulerRecyclerView = (RecyclerView) findViewById(R.id.ruler_recyclerview);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rulerRecyclerView.setLayoutManager(linearLayoutManager);
//    }

    // sets up the recyclerviewadatper and populates the cards
//    private void populateRulerCardListView(List<Ruler> rulers) {
//        rulerRecyclerView = (RecyclerView) findViewById(R.id.ruler_recyclerview);
//        rulerRecycleViewAdapter = new RulerRecycleViewAdapter(rulers);
//        rulerRecyclerView.setAdapter(rulerRecycleViewAdapter);
//        rulerRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//				@Override public void onItemClick(View view, int position) {
//                    Snackbar.make(view, "I've been clicked", Snackbar.LENGTH_LONG).show();
//                }
//			}));
//    }
}
