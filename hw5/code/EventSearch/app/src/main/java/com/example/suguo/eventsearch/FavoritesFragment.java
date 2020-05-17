package com.example.suguo.eventsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FavoritesFragment extends Fragment implements ResultsAdapter.ListItemClickListener {

    private static final String TAG = "FavoritesFragment";
    private RecyclerView favoritesRecyclerView;
    private TextView noFavoritesTextView;
    private LinearLayout loadingLinearLayout;
    private SharedPreferences favoritesSharedPreferences;
    private SharedPreferences.Editor favoritesEditor;
    private JSONArray favoritesJSONArray;
    private ArrayList<EventResult> favoritesArrayList;
    private ResultsAdapter resultsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        favoritesRecyclerView = view.findViewById(R.id.favoritesFragment_recyclerView_favorites);
        noFavoritesTextView = view.findViewById(R.id.favoritesFragment_textView_noFavorites);
        loadingLinearLayout = view.findViewById(R.id.favoritesFragment_linearLayout_loading);

        favoritesSharedPreferences = view.getContext().getSharedPreferences("favoritesFile", view.getContext().MODE_PRIVATE);
        favoritesEditor = favoritesSharedPreferences.edit();
        // clear favoritesJSONArray when data in favorites has error
        //favoritesEditor.putString("favoritesJSONArray", "");
        //favoritesEditor.apply();
        favoritesArrayList = new ArrayList<>();

        try {
            favoritesJSONArray = new JSONArray(favoritesSharedPreferences.getString("favoritesJSONArray", ""));
            Log.d(TAG, "favoritesJSONArray: " + favoritesJSONArray.toString());
            // convert favoritesJSONArray into favoritesArrayList
            for (int i = 0; i < favoritesJSONArray.length(); i++) {
                JSONObject favorite = (JSONObject)favoritesJSONArray.get(i);
                String eventId = favorite.getString("eventId");
                String segment = favorite.getString("segment");
                String eventName = favorite.getString("eventName");
                String venueName = favorite.getString("venueName");
                String dateTime = favorite.getString("dateTime");
                boolean inFavorites = true;
                favoritesArrayList.add(new EventResult(eventId, segment, eventName, venueName, dateTime, inFavorites));
            }
            //Log.d(TAG, "favoritesArrayList: " + favoritesArrayList.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
            favoritesJSONArray = new JSONArray();
            favoritesArrayList = new ArrayList<>();
        }

        // set adapter
        resultsAdapter = new ResultsAdapter(favoritesArrayList, this);
        favoritesRecyclerView.setAdapter(resultsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        favoritesRecyclerView.setLayoutManager(layoutManager);

        if (favoritesArrayList.isEmpty()) {
            favoritesRecyclerView.setVisibility(View.GONE);
            noFavoritesTextView.setVisibility(View.VISIBLE);
        }
        else {
            favoritesRecyclerView.setVisibility(View.VISIBLE);
            noFavoritesTextView.setVisibility(View.GONE);
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // update data from new shared preferences
        favoritesArrayList = new ArrayList<>();
        try {
            favoritesJSONArray = new JSONArray(favoritesSharedPreferences.getString("favoritesJSONArray", ""));
            Log.d(TAG, "favoritesJSONArray: " + favoritesJSONArray.toString());
            // convert favoritesJSONArray into favoritesArrayList
            for (int i = 0; i < favoritesJSONArray.length(); i++) {
                JSONObject favorite = (JSONObject)favoritesJSONArray.get(i);
                String eventId = favorite.getString("eventId");
                String segment = favorite.getString("segment");
                String eventName = favorite.getString("eventName");
                String venueName = favorite.getString("venueName");
                String dateTime = favorite.getString("dateTime");
                boolean inFavorites = true;
                favoritesArrayList.add(new EventResult(eventId, segment, eventName, venueName, dateTime, inFavorites));
            }
            //Log.d(TAG, "Resume: favoritesArrayList: " + favoritesArrayList.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
            favoritesJSONArray = new JSONArray();
            favoritesArrayList = new ArrayList<>();
        }
        // set adapter
        resultsAdapter = new ResultsAdapter(favoritesArrayList, this);
        favoritesRecyclerView.setAdapter(resultsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        favoritesRecyclerView.setLayoutManager(layoutManager);

        if (favoritesArrayList.isEmpty()) {
            favoritesRecyclerView.setVisibility(View.GONE);
            noFavoritesTextView.setVisibility(View.VISIBLE);
        }
        else {
            favoritesRecyclerView.setVisibility(View.VISIBLE);
            noFavoritesTextView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onListItemDetailsClick(int clickItemIndex) {

        // hide recyclerView, show loading
        favoritesRecyclerView.setVisibility(View.GONE);
        loadingLinearLayout.setVisibility(View.VISIBLE);

        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        final Intent mIntent = new Intent(getActivity(), DetailsActivity.class);
        // get eventId, segment, eventName, venueName, dateTime for DetailsActivity
        String eventId = favoritesArrayList.get(clickItemIndex).getEventId();
        mIntent.putExtra("eventId", eventId);
        String segment = favoritesArrayList.get(clickItemIndex).getSegment();
        mIntent.putExtra("segment", segment);
        String eventName = favoritesArrayList.get(clickItemIndex).getEventName();
        mIntent.putExtra("eventName", eventName);
        String venueName = favoritesArrayList.get(clickItemIndex).getVenueName();
        mIntent.putExtra("venueName", venueName);
        String dateTime = favoritesArrayList.get(clickItemIndex).getDateTime();
        mIntent.putExtra("dateTime", dateTime);
        // request
        venueName = venueName.replaceAll(" ", "%20");
        String detailsUrl = "http://csci571-hw9-android.us-east-2.elasticbeanstalk.com/?" + "eventId=" + eventId + "&venueName=" + venueName;
        Log.d(TAG, "url: " + detailsUrl);

        // request for 4 tabs
        StringRequest stringRequest = new StringRequest(Request.Method.GET, detailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mIntent.putExtra("details", response);
                // hide loading
                loadingLinearLayout.setVisibility(View.GONE);
                startActivity(mIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Fail to request backend for event detail.", Toast.LENGTH_LONG).show();
                // hide loading
                loadingLinearLayout.setVisibility(View.GONE);
            }
        });

        queue.add(stringRequest);
    }


    @Override
    public void onListItemFavoriteClick(int clickItemIndex) {

        EventResult currentEventResult = favoritesArrayList.get(clickItemIndex);
        // set inFavorites false, because we remove it from favorites
        currentEventResult.setInFavorites(false);
        // remove item from favoritesJSONArray
        favoritesJSONArray.remove(clickItemIndex);
        // remove item from recyclerView
        favoritesArrayList.remove(clickItemIndex);
        // tell adapter to update view
        resultsAdapter.notifyDataSetChanged();
        // put JSONArray back into shared preferences
        favoritesEditor.putString("favoritesJSONArray", favoritesJSONArray.toString());
        favoritesEditor.apply();

        String toastMessage = currentEventResult.getEventName() + " was removed from favorites";
        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();

        if (favoritesArrayList.isEmpty()) {
            favoritesRecyclerView.setVisibility(View.GONE);
            noFavoritesTextView.setVisibility(View.VISIBLE);
        }
        else {
            favoritesRecyclerView.setVisibility(View.VISIBLE);
            noFavoritesTextView.setVisibility(View.GONE);
        }
    }
}