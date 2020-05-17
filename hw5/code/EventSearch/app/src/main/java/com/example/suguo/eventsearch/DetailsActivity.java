package com.example.suguo.eventsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    private DetailsActivityPagerAdapter mDetailsActivityPagerAdapter;
    private ViewPager mViewPager;
    private Menu menu;

    // 1 event fragment data
    private String eventString;
    // 2 photos fragment data
    private String artistTeam1String;
    private String artistTeam2String;
    private String segmentString;
    // 3 map fragment data
    private String venueString;
    // 4 reviews fragment data
    private String upcomingString;

    // data for favorites
    private String eventId;
    private String segment;
    private String eventName;
    private String venueName;
    private String dateTime;

    // data for twitter
    private String venue;
    private String ticketMaster;

    // shared preferences
    private SharedPreferences favoritesSharedPreferences;
    private SharedPreferences.Editor favoritesEditor;
    private JSONArray favoritesJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // get data for favorites
        Intent receiveDataIntent = getIntent();
        eventId = receiveDataIntent.getStringExtra("eventId");
        segment = receiveDataIntent.getStringExtra("segment");
        eventName = receiveDataIntent.getStringExtra("eventName");
        venueName = receiveDataIntent.getStringExtra("venueName");
        dateTime = receiveDataIntent.getStringExtra("dateTime");
        //Log.d(TAG, "first: dateTime: " +  dateTime);

        setTitle(eventName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String detailsString = receiveDataIntent.getStringExtra("details");
        //Log.d(TAG, "details: " + detailsString);

        try {
            JSONObject detailsJSONObject = new JSONObject(detailsString);
            JSONObject eventJSONObject = detailsJSONObject.getJSONObject("eventDetail");
            JSONObject venueJSONObject = detailsJSONObject.getJSONObject("venue");
            JSONArray upcomingJSONArray = detailsJSONObject.getJSONArray("upcoming");
            // get data for twitter
            venue = eventJSONObject.getString("venue");
            ticketMaster = eventJSONObject.getString("buyTicketAt");
            // 1 get data for event fragment
            eventString = eventJSONObject.toString();
            Log.d(TAG, "event: " + eventString);
            // 2 get data for artist fragment
            artistTeam1String = eventJSONObject.getString("artistTeam1");
            artistTeam2String = eventJSONObject.getString("artistTeam2");
            segmentString = eventJSONObject.getString("segment");
            // 3 get data for venue fragment
            venueString = venueJSONObject.toString();
            // 4 get data for upcoming fragment
            upcomingString = upcomingJSONArray.toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        // Set up the ViewPager with the sections adapter.
        mDetailsActivityPagerAdapter = new DetailsActivityPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.detailsActivity_viewPager);
        mViewPager.setAdapter(mDetailsActivityPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.detailsActivity_tabLayout);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // shared preferences
        favoritesSharedPreferences = getSharedPreferences("favoritesFile", MODE_PRIVATE);
        favoritesEditor = favoritesSharedPreferences.edit();
        try {
            favoritesJSONArray = new JSONArray(favoritesSharedPreferences.getString("favoritesJSONArray", ""));
        } catch (JSONException e) {
            e.printStackTrace();
            favoritesJSONArray = new JSONArray();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.detailsActivity_twitter:
                openTwitterLink();
                break;
            case R.id.detailsActivity_favorite:
                clickFavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_details, menu);
        boolean inFavorites = checkInFavorites(eventId);
        if (inFavorites) {
            menu.findItem(R.id.detailsActivity_favorite).setIcon(R.drawable.heart_fill_red);
        }
        else {
            menu.findItem(R.id.detailsActivity_favorite).setIcon(R.drawable.heart_fill_white);
        }
        return true;
    }


    public void openTwitterLink() {
        String twitterText = "Check out " + eventName + " located at " + venue + ". Website: " + ticketMaster;
        String twitterLink = "https://twitter.com/intent/tweet?text=" + twitterText + "&hashtags=EventSearch";
        Uri uri = Uri.parse(twitterLink);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public class DetailsActivityPagerAdapter extends FragmentPagerAdapter {

        public DetailsActivityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // 1 detail event:
            if (position == 0) {
                EventFragment eventFragment = new EventFragment();
                Bundle eventBundle = new Bundle();
                eventBundle.putString("event", eventString);
                //Log.d(TAG, "event: " + eventString);
                eventFragment.setArguments(eventBundle);
                return eventFragment;
            }
            // 2 detail artist:
            else if (position == 1) {
                ArtistsFragment artistsFragment = new ArtistsFragment();
                Bundle artistsBundle = new Bundle();
                artistsBundle.putString("artistTeam1", artistTeam1String);
                artistsBundle.putString("artistTeam2", artistTeam2String);
                artistsBundle.putString("segment", segmentString);
                artistsFragment.setArguments(artistsBundle);
                return artistsFragment;
            }
            // 3 detail venue
            else if (position == 2) {
                VenueFragment venueFragment = new VenueFragment();
                Bundle venueBundle = new Bundle();
                venueBundle.putString("venue", venueString);
                venueFragment.setArguments(venueBundle);
                return venueFragment;
            }
            // 4 detail upcoming
            else {
                UpcomingFragment upcomingFragment = new UpcomingFragment();
                Bundle upcomingBundle = new Bundle();
                upcomingBundle.putString("upcoming", upcomingString);
                upcomingFragment.setArguments(upcomingBundle);
                return upcomingFragment;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    public void clickFavorite() {
        String toastMessage = "";
        // get favoritesJSONArray in shared preferences
        try {
            favoritesJSONArray = new JSONArray(favoritesSharedPreferences.getString("favoritesJSONArray", ""));
        }
        catch (JSONException e) {
            e.printStackTrace();
            favoritesJSONArray = new JSONArray();
        }

        boolean inFavorites = checkInFavorites(eventId);
        // if selected item is in Favorites, remove this item from favorites(shared preferences)
        if (inFavorites) {
            int indexInFavorites = checkFavoritesIndex(eventId);
            favoritesJSONArray.remove(indexInFavorites);
            // create toast message
            toastMessage = eventName + " was removed from favorites";
            menu.findItem(R.id.detailsActivity_favorite).setIcon(R.drawable.heart_fill_white);
        }
        // if selected item is not in Favorites, add this item to favorites(shared preferences)
        else {
            // create JSONObject newFavoriteJSONObject and add it to favorites(shared preferences)
            JSONObject newFavoriteJSONObject = new JSONObject();
            try {
                newFavoriteJSONObject.put("eventId", eventId);
                newFavoriteJSONObject.put("segment", segment);
                newFavoriteJSONObject.put("eventName", eventName);
                newFavoriteJSONObject.put("venueName", venueName);
                newFavoriteJSONObject.put("dateTime", dateTime);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "newFavoriteJSONObject: " +  newFavoriteJSONObject.toString());
            // add newFavoriteJSONObject into favoritesJSONArray
            favoritesJSONArray.put(newFavoriteJSONObject);
            // create toast message
            toastMessage = eventName + " was added to favorites";
            menu.findItem(R.id.detailsActivity_favorite).setIcon(R.drawable.heart_fill_red);
        }

        // update favoritesJSONArray in shared preferences
        favoritesEditor.putString("favoritesJSONArray", favoritesJSONArray.toString());
        favoritesEditor.apply();
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




