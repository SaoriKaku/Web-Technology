package com.example.suguo.eventsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements ResultsAdapter.ListItemClickListener {

    private static final String TAG = "ResultsActivity";
    private ArrayList<EventResult> eventResultArrayList;
    private RecyclerView recyclerView;
    private TextView noResultTextView;
    private LinearLayout loadingLinearLayout;
    private ResultsAdapter resultsAdapter;
    private int storeClickItemIndex;
    // shared preferences
    private SharedPreferences favoritesSharedPreferences;
    private SharedPreferences.Editor favoritesEditor;
    private JSONArray favoritesJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setTitle("Search Results");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storeClickItemIndex = -1;
        recyclerView = (RecyclerView)findViewById(R.id.resultsActivity_recyclerView_results);
        noResultTextView = (TextView)findViewById(R.id.resultsActivity_textView_noResults);
        loadingLinearLayout = findViewById(R.id.searchFragment_progressBar);

        // shared preferences
        favoritesSharedPreferences = getSharedPreferences("favoritesFile", MODE_PRIVATE);
        favoritesEditor = favoritesSharedPreferences.edit();
        try {
            favoritesJSONArray = new JSONArray(favoritesSharedPreferences.getString("favoritesJSONArray", ""));
        }
        catch (JSONException e) {
            e.printStackTrace();
            favoritesJSONArray = new JSONArray();
        }

        // get data from MainActivity
        eventResultArrayList = new ArrayList<>();
        Intent mIntent = getIntent();
        String resultsArray = mIntent.getStringExtra("results");
        try {
            JSONArray resultsJSONArray = new JSONArray(resultsArray);
            for (int i = 0; i < resultsJSONArray.length(); i++) {
                JSONObject result = (JSONObject) resultsJSONArray.get(i);
                String eventId = result.getString("eventId");
                String segment = result.getString("segment");
                String eventName = result.getString("eventName");
                String venueName = result.getString("venueName");
                String dateTime = result.getString("dateTime");
                boolean inFavorites = checkInFavorites(eventId);
                eventResultArrayList.add(new EventResult(eventId, segment, eventName, venueName, dateTime, inFavorites));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        // set resultAdapter
        resultsAdapter = new ResultsAdapter(eventResultArrayList, this);
        recyclerView.setAdapter(resultsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (eventResultArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            loadingLinearLayout.setVisibility(View.GONE);
            noResultTextView.setVisibility(View.VISIBLE);
        } else {
            noResultTextView.setVisibility(View.GONE);
            loadingLinearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setVisibility(View.VISIBLE);
        if (storeClickItemIndex != -1) {
            // get the new favoritesJSONArray from Shared Preferences
            try {
                favoritesJSONArray = new JSONArray(favoritesSharedPreferences.getString("favoritesJSONArray", ""));
            }
            catch (JSONException e) {
                e.printStackTrace();
                favoritesJSONArray = new JSONArray();
            }
            // find that item clicked for details
            EventResult currentEventResult = eventResultArrayList.get(storeClickItemIndex);
            String eventId = currentEventResult.getEventId();
            boolean oldInFavorites = currentEventResult.getInFavorites();
            Log.d(TAG, "oldInFavorites: " + oldInFavorites);
            // use eventId to get the new inFavorites of that clicked item
            boolean newInFavorites = checkInFavorites(eventId);
            Log.d(TAG, "newInFavorites: " + newInFavorites);
            if (oldInFavorites != newInFavorites) {
                currentEventResult.setInFavorites(newInFavorites);
                // reset resultAdapter
                resultsAdapter = new ResultsAdapter(eventResultArrayList, this);
                recyclerView.setAdapter(resultsAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemDetailsClick(int clickItemIndex) {
        storeClickItemIndex = clickItemIndex;
        // hide recyclerView, show loading
        recyclerView.setVisibility(View.GONE);
        loadingLinearLayout.setVisibility(View.VISIBLE);

        final RequestQueue mqueue = Volley.newRequestQueue(this);
        final Intent mIntent = new Intent(this, DetailsActivity.class);

        // data for favorites and data for request
        String eventId = eventResultArrayList.get(clickItemIndex).getEventId();
        mIntent.putExtra("eventId", eventId);
        String segment = eventResultArrayList.get(clickItemIndex).getSegment();
        mIntent.putExtra("segment", segment);
        String eventName = eventResultArrayList.get(clickItemIndex).getEventName();
        mIntent.putExtra("eventName", eventName);
        String venueName = eventResultArrayList.get(clickItemIndex).getVenueName();
        mIntent.putExtra("venueName", venueName);
        String dateTime = eventResultArrayList.get(clickItemIndex).getDateTime();
        mIntent.putExtra("dateTime", dateTime);

        // request for 4 tabs
        venueName = venueName.replaceAll(" ", "%20");
        String detailsUrl = "http://csci571-hw9-android.us-east-2.elasticbeanstalk.com/?" + "eventId=" + eventId + "&venueName=" + venueName;
        Log.d(TAG, "url: " + detailsUrl);

        StringRequest eventStringRequest = new StringRequest(Request.Method.GET, detailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response: " + response);
                mIntent.putExtra("details", response);
                // hide loading
                loadingLinearLayout.setVisibility(View.GONE);
                // lead to details activity
                startActivity(mIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fail to request backend for event detail.", Toast.LENGTH_LONG).show();
                // hide loading
                loadingLinearLayout.setVisibility(View.GONE);
            }
        });
        mqueue.add(eventStringRequest);
    }


    @Override
    public void onListItemFavoriteClick(int clickItemIndex) {
        EventResult currentEventResult = eventResultArrayList.get(clickItemIndex);
        String toastMessage = "";
        // get JSONArray in shared preferences
        try {
            favoritesJSONArray = new JSONArray(favoritesSharedPreferences.getString("favoritesJSONArray", ""));
        }
        catch (JSONException e) {
            e.printStackTrace();
            favoritesJSONArray = new JSONArray();
        }

        String eventId = eventResultArrayList.get(clickItemIndex).getEventId();
        boolean inFavorites = checkInFavorites(eventId);

        // if selected item is in Favorites, remove this item from favorites(shared preferences)
        if (inFavorites) {
            currentEventResult.setInFavorites(false);
            int indexInFavorites = checkFavoritesIndex(eventId);
            favoritesJSONArray.remove(indexInFavorites);
            // create toast message
            toastMessage = currentEventResult.getEventName() + " was removed from favorites";
        }
        // if selected item is not in Favorites, add this item to favorites(shared preferences)
        else {
            currentEventResult.setInFavorites(true);
            // create JSONObject newFavoriteJSONObject and add it to favorites(shared preferences)
            JSONObject newFavoriteJSONObject = new JSONObject();
            try {
                newFavoriteJSONObject.put("eventId", currentEventResult.getEventId());
                newFavoriteJSONObject.put("segment", currentEventResult.getSegment());
                newFavoriteJSONObject.put("eventName", currentEventResult.getEventName());
                newFavoriteJSONObject.put("venueName", currentEventResult.getVenueName());
                newFavoriteJSONObject.put("dateTime", currentEventResult.getDateTime());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "newFavoriteJSONObject: " +  newFavoriteJSONObject.toString());
            // add newFavoriteJSONObject into favoritesJSONArray
            favoritesJSONArray.put(newFavoriteJSONObject);
            // create toast message
            toastMessage = currentEventResult.getEventName() + " was added to favorites";
        }
        // update favoritesJSONArray in shared preferences
        favoritesEditor.putString("favoritesJSONArray", favoritesJSONArray.toString());
        Log.d(TAG, "favoritesJSONArray: " +  favoritesJSONArray.toString());
        favoritesEditor.apply();
        // tell adapter to update view
        resultsAdapter.notifyItemChanged(clickItemIndex);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }


    public int checkFavoritesIndex(String targetEventId) {
        int indexInFavorites = -1;
        for (int i = 0; i < favoritesJSONArray.length(); i++) {
            try {
                JSONObject favoriteJSONObject = favoritesJSONArray.getJSONObject(i);
                if (favoriteJSONObject.getString("eventId").equals(targetEventId)) {
                    indexInFavorites = i;
                    break;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return indexInFavorites;
    }


    public boolean checkInFavorites(String targetEventId) {
        boolean inFavorites = false;
        for (int i = 0; i < favoritesJSONArray.length(); i++) {
            try {
                JSONObject favoriteJSONObject = favoritesJSONArray.getJSONObject(i);
                if (favoriteJSONObject.getString("eventId").equals(targetEventId)) {
                    inFavorites = true;
                    break;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return inFavorites;
    }

}
