package com.bgrulers.rulerslist;

import android.support.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bgrulers.model.Ruler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sslavkov on 3/20/17.
 */

public class RulerListPresenter implements RulerListContract.Presenter {

	private List<Ruler> rulers = null;
	private Map<Long, Ruler> rulerMap = null;

	private final RulerListContract.View mRulerListView;

	public RulerListPresenter(@NonNull RulerListContract.View rulerListView) {
		mRulerListView = checkNotNull(rulerListView);
		mRulerListView.setPresenter(this);
	}

	@Override
	public void start() {
		if (CollectionUtils.isEmpty(rulers)) {
			loadRulers();
		} else {
			mRulerListView.showRulers(rulers);
		}
	}

	@Override
	public void loadRulers() {
		mRulerListView.showLoading(true);
		fetchRulersAndPopulateView();
	}

	@Override
	public void startDetailsActivity(Long rulerId) {
		mRulerListView.startRulerDetail(getRulerFromMap(rulerId));
	}

	@Override
	public Ruler getRulerFromPosition(int position) {
		return rulers.get(position);
	}


	// TODO - this should be moved to its own data source class
	private void fetchRulersAndPopulateView() {
		// TODO - make environment specific urls
		String url = "https://rulers-production.herokuapp.com/api/rulers?projection=rulerList&size=1000";

		JsonObjectRequest jsonRequest = new JsonObjectRequest
				(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						rulers = getRulersListFromJson(response);
						mRulerListView.showRulers(rulers); // using cards
//                      populateRulerListView(rulers); // using list
						mRulerListView.showLoading(false);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				});

		// TODO getContext is a workaround - should be in a separate DataSourceRemote file
		Volley.newRequestQueue(((RulerListFragment) mRulerListView).getContext()).add(jsonRequest);
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

}
