package com.bg_rulers.bulgarianrulers.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import org.json.JSONObject;

import java.io.IOException;

public class RulerDetailScrollingActivity extends AppCompatActivity {

    // constants
    public static final String RULER_ID = "RULER_ID";
    public static final String RULER_NAME = "RULER_NAME";
    public static final String RULER_TITLE = "RULER_NAME";
    public static final String RULER_TITLE_AND_NAME = "RULER_TITLE_AND_NAME";
    public static final String RULER_REIGN_START = "RULER_REIGN_START";
    public static final String RULER_REIGN_END = "RULER_REIGN_END";

    private Ruler ruler;
    private String activityTitle;

    // Ruler fields
    private Long rulerId;
    private String rulerName;
    private String rulerTitle;
    private String rulerTitleAndName;
    private String rulerReignStart;
    private String rulerReignEnd;

    // Views
    TextView extraTitleView;
    TextView dynastyView;
    TextView reignView;
    TextView infoView;

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
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareContent = "Info about " + rulerTitleAndName + ":\n" +
                        (!StringUtils.isEmpty(extraTitleView.getText()) ? extraTitleView.getText() + "\n" : "") +
                        infoView.getText();
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, rulerTitleAndName);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            // set back to Main Activity
        }

        populateFieldsFromIntent();
        setTitle(rulerTitleAndName);

        if (ruler == null) {
            // fetch ruler
            fetchRulerAndPopulateView(rulerId);
        } else {
            populateRulerDetails(ruler);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // on pressing UP button - trigger back functionality
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void populateFieldsFromIntent() {
        Intent intent = getIntent();
        Resources resources = getResources();

        // set fields from intent
        rulerId             = intent.getLongExtra(RULER_ID, 1);
        rulerName           = intent.getStringExtra(RULER_NAME);
        rulerTitle          = intent.getStringExtra(RULER_TITLE);
        rulerTitleAndName   = intent.getStringExtra(RULER_TITLE_AND_NAME);
        rulerReignStart     = intent.getStringExtra(RULER_REIGN_START);
        rulerReignEnd       = intent.getStringExtra(RULER_REIGN_END);

        // Reign
        reignView = (TextView) findViewById(R.id.ruler_detail_scrolling_reign);
        reignView.setText(resources.getString(R.string.ruled_during_reign_range, rulerReignStart, rulerReignEnd));

        // Extra Title
        extraTitleView = (TextView) findViewById(R.id.ruler_detail_scrolling_extra_title);
        extraTitleView.setVisibility(View.GONE); // gone unless there's extra title
    }

    private void fetchRulerAndPopulateView(Long rulerId) {
        // TODO - make environment specific urls
        String url = "https://rulers-production.herokuapp.com/api/rulers/" + rulerId + "?projection=rulerDetail";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        ruler = getRulerFromJson(response);
//                       populateRulerCardListView(rulers); // using cards
                        populateRulerDetails(ruler); // using list

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private void populateRulerDetails(Ruler ruler) {
        Resources resources = getResources();

        if (!StringUtils.isEmpty(ruler.getExtraTitle())) {
            // Extra Title
            extraTitleView.setVisibility(View.VISIBLE);
            extraTitleView.setText(resources.getString(R.string.extra_title_header, ruler.getExtraTitle()));
        }

        // Dynasty
        dynastyView = (TextView) findViewById(R.id.ruler_detail_scrolling_dynasty);
        dynastyView.setText(ruler.getDynasty().getName());

        // Info
        infoView = (TextView) findViewById(R.id.ruler_detail_scrolling_info);
        infoView.setText(ruler.getInformation());
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
