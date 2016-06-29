package com.bg_rulers.bulgarianrulers.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bg_rulers.bulgarianrulers.R;
import com.bg_rulers.bulgarianrulers.adapter.RulerRecycleViewAdapter;
import com.bg_rulers.bulgarianrulers.model.Ruler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Ruler> rulers = null;
    RecyclerView  rulerRecyclerView;
    RulerRecycleViewAdapter rulerRecycleViewAdapter;

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

        rulerRecyclerView = (RecyclerView) findViewById(R.id.ruler_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rulerRecyclerView.setLayoutManager(linearLayoutManager);

        // get rulers here and show list
        if (rulers == null) {
            fetchRulersAndPopulateView();
        } else {
            populateRulerListView(rulers);
        }
    }

    private void fetchRulersAndPopulateView() {
        // TODO - make environment specific urls
        String url = "https://rulers-production.herokuapp.com/api/rulers?projection=list&size=1000";

        System.out.println("About to make a call:");
        JsonObjectRequest jsonRequest = new JsonObjectRequest
               (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       // the response is already constructed as a JSONObject!
                       System.out.println("Retrieved a response");
                       rulers = getRulersListFromJson(response);
                       populateRulerListView(rulers);

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
            System.out.println(rulersJson);

            rulers = mapper.readValue(rulersJson.toString(), TypeFactory.defaultInstance().constructCollectionType(List.class, Ruler.class));
            System.out.println(rulers != null ? rulers.size() : "rulers is null");
            return rulers;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private void populateRulerListView(List<Ruler> rulers) {
        rulerRecyclerView = (RecyclerView) findViewById(R.id.ruler_list);
        rulerRecycleViewAdapter = new RulerRecycleViewAdapter(rulers);
        rulerRecyclerView.setAdapter(rulerRecycleViewAdapter);
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
}
