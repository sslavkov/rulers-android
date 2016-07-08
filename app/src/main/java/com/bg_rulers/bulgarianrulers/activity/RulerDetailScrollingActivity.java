package com.bg_rulers.bulgarianrulers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bg_rulers.bulgarianrulers.R;
import com.bg_rulers.bulgarianrulers.model.Ruler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.io.IOException;

public class RulerDetailScrollingActivity extends AppCompatActivity {

    public static final String RULER_ID = "RULER_ID";
    public static final String RULER_NAME = "RULER_NAME";
    public static final String RULER_TITLE_AND_NAME = "RULER_TITLE_AND_NAME";
    public static final String RULER_REIGN_RANGE = "RULER_REIGN_RANGE";

    private Ruler ruler;
    private String activityTitle;
    private String rulerTitleAndName;
    private String rulerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler_detail_scrolling);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            // set back to Main Activity
        } else {
            activityTitle =     intent.getStringExtra(RULER_TITLE_AND_NAME);
            rulerTitleAndName = intent.getStringExtra(RULER_TITLE_AND_NAME);
            rulerName =         intent.getStringExtra(RULER_NAME);

            setTitle(activityTitle);
        }


        if (ruler == null) {
            long rulerId = intent.getLongExtra(RULER_ID, 1);
            // fetch ruler
            fetchRulerAndPopulateView(rulerId);
        } else {
            populateDetails(ruler);
        }

    }

    private void fetchRulerAndPopulateView(Long rulerId) {
        // TODO - make environment specific urls
        String url = "https://rulers-production.herokuapp.com/api/rulers/" + rulerId + "?projection=rulerDetail";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        System.out.println("Retrieved a response");
                        ruler = getRulerFromJson(response);
//                       populateRulerCardListView(rulers); // using cards
                        populateDetails(ruler); // using list

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private void populateDetails(Ruler ruler) {
        if (StringUtils.isEmpty(rulerName)) {
            TextView nameView = (TextView) findViewById(R.id.ruler_detail_scrolling_name);
            nameView.setText(ruler.getName());
        }

        TextView titleView = (TextView) findViewById(R.id.ruler_detail_scrolling_title);

        String title = WordUtils.capitalizeFully(ruler.getTitle().getTitleType().toString());
        titleView.setText(title);
    }

    private Ruler getRulerFromJson(JSONObject jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ruler = mapper.readValue(jsonObject.toString(), Ruler.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ruler;
    }
}
