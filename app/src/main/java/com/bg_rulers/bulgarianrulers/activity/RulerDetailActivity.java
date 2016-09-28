package com.bg_rulers.bulgarianrulers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class RulerDetailActivity extends AppCompatActivity {

    public static final String RULER_ID = "RULER_ID";
    public static final String RULER_NAME = "RULER_NAME";
    public static final String RULER_TITLE_AND_NAME = "RULER_TITLE_AND_NAME";

    private Ruler ruler;
    private String activityTitle;
    private String rulerName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ruler_detail);

        Intent intent = getIntent();
        if (intent == null) {
            // set back to Main Activity
        }

        activityTitle = intent.getStringExtra(RULER_TITLE_AND_NAME);
        rulerName = intent.getStringExtra(RULER_NAME);
        if (!StringUtils.isEmpty(activityTitle)) {
            setTitle(activityTitle);
        }
        if (!StringUtils.isEmpty(rulerName)) {
            TextView rulerNameView = (TextView) findViewById(R.id.ruler_detail_name);
            rulerNameView.setText(rulerName);
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
        String url = "https://rulers-production.herokuapp.com/api/rulers/" + rulerId + "?projection=detail";

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
            TextView nameView = (TextView) findViewById(R.id.ruler_detail_name);
            nameView.setText(ruler.getName());
        }
        TextView titleView = (TextView) findViewById(R.id.ruler_detail_title);

        String title = ruler.getTitle().getTitleType().toString();
        titleView.setText(WordUtils.capitalizeFully(title));
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
