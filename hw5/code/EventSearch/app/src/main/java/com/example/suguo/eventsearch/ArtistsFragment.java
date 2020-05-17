package com.example.suguo.eventsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ArtistsFragment extends Fragment {

    private static final String TAG = "ArtistsFragment";

    public ArtistsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        // the first artistTeam1:
        final LinearLayout artistTeam1LinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artistTeam1);
        final TextView artistTeam1NameTextView = (TextView)view.findViewById(R.id.artistsFragment_textView_artistTeam1_name);

        // if artistTeam1 is related to Music
        final LinearLayout artist1LinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist1);
        //the first row: name
        final LinearLayout artist1NameLinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist1_name);
        final TextView artist1NameTextView = (TextView)view.findViewById(R.id.artistsFragment_TextView_artist1_name);
        // the second row: followers
        final LinearLayout artist1FollowersLinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist1_followers);
        final TextView artist1FollowersTextView = (TextView)view.findViewById(R.id.artistsFragment_TextView_artist1_followers);
        // the third row: popularity
        final LinearLayout artist1PopularityLinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist1_popularity);
        final TextView artist1PopularityTextView = (TextView)view.findViewById(R.id.artistsFragment_TextView_artist1_popularity);
        // the fourth row: checkAt
        final LinearLayout artist1CheckAtLinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist1_checkAt);
        final TextView artist1CheckAtTextView = (TextView)view.findViewById(R.id.artistsFragment_TextView_artist1_checkAt);

        // the eight photos
        final LinearLayout photos1LinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_photos1);
        final ImageView artistTeam1Photo1ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam1_photo1);
        final ImageView artistTeam1Photo2ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam1_photo2);
        final ImageView artistTeam1Photo3ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam1_photo3);
        final ImageView artistTeam1Photo4ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam1_photo4);
        final ImageView artistTeam1Photo5ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam1_photo5);
        final ImageView artistTeam1Photo6ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam1_photo6);
        final ImageView artistTeam1Photo7ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam1_photo7);
        final ImageView artistTeam1Photo8ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam1_photo8);

        // the second artistTeam2:
        final LinearLayout artistTeam2LinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artistTeam2);
        final TextView artistTeam2NameTextView = (TextView)view.findViewById(R.id.artistsFragment_textView_artistTeam2_name);

        // if artistTeam2 is related to Music
        final LinearLayout artist2LinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist2);
        //the first row: name
        final LinearLayout artist2NameLinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist2_name);
        final TextView artist2NameTextView = (TextView)view.findViewById(R.id.artistsFragment_TextView_artist2_name);
        // the second row: followers
        final LinearLayout artist2FollowersLinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist2_followers);
        final TextView artist2FollowersTextView = (TextView)view.findViewById(R.id.artistsFragment_TextView_artist2_followers);
        // the third row: popularity
        final LinearLayout artist2PopularityLinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist2_popularity);
        final TextView artist2PopularityTextView = (TextView)view.findViewById(R.id.artistsFragment_TextView_artist2_popularity);
        // the fourth row: checkAt
        final LinearLayout artist2CheckAtLinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_artist2_checkAt);
        final TextView artist2CheckAtTextView = (TextView)view.findViewById(R.id.artistsFragment_TextView_artist2_checkAt);

        // the eight photos
        final LinearLayout photos2LinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_linearLayout_photos2);
        final ImageView artistTeam2Photo1ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam2_photo1);
        final ImageView artistTeam2Photo2ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam2_photo2);
        final ImageView artistTeam2Photo3ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam2_photo3);
        final ImageView artistTeam2Photo4ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam2_photo4);
        final ImageView artistTeam2Photo5ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam2_photo5);
        final ImageView artistTeam2Photo6ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam2_photo6);
        final ImageView artistTeam2Photo7ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam2_photo7);
        final ImageView artistTeam2Photo8ImageView = (ImageView)view.findViewById(R.id.artistsFragment_imageView_artistTeam2_photo8);

        // no artistTeam
        final TextView noArtistsTextView = (TextView)view.findViewById(R.id.artistsFragment_textView_noArtists);
        // loadingLinearLayout
        final LinearLayout loadingLinearLayout = (LinearLayout)view.findViewById(R.id.artistsFragment_loading);

        // receive data
        Bundle bundle = this.getArguments();
        // artistTeam1:
        String artistTeam1 = bundle.getString("artistTeam1");
        final String artistTeam1final = artistTeam1;
        Log.d(TAG, "artistTeam1: " + artistTeam1);

        // artistTeam2:
        String artistTeam2 = bundle.getString("artistTeam2");
        final String artistTeam2final = artistTeam2;
        Log.d(TAG, "artistTeam2: " + artistTeam2);

        // segment
        final String segment = bundle.getString("segment");
        Log.d(TAG, "segment: " + segment);

        // send request
        RequestQueue mqueue = Volley.newRequestQueue(getActivity());
        artistTeam1 = artistTeam1.replaceAll(" ", "%20");
        artistTeam2 = artistTeam2.replaceAll(" ", "%20");
        String artistsUrl = "http://csci571-hw9-android.us-east-2.elasticbeanstalk.com/?" + "artistTeam1=" + artistTeam1 + "&artistTeam2=" + artistTeam2 + "&segment=" + segment;
        Log.d(TAG, "url: " + artistsUrl);

        // hide View
        artistTeam1LinearLayout.setVisibility(View.GONE);
        artistTeam2LinearLayout.setVisibility(View.GONE);
        noArtistsTextView.setVisibility(View.GONE);
        // show loading
        loadingLinearLayout.setVisibility(View.VISIBLE);

        // get the request response
        StringRequest eventStringRequest = new StringRequest(Request.Method.GET, artistsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response: " + response);

                // deal with data
                try {
                    JSONObject artistsJSONObject= new JSONObject(response);

                    // artistTeam1 title
                    if(!artistTeam1final.equals("")) {
                        artistTeam1NameTextView.setText(artistTeam1final);
                    }
                    // artistTeam2 title
                    if(!artistTeam2final.equals("")) {
                        artistTeam2NameTextView.setText(artistTeam2final);
                    }

                    // the first part: music1
                    JSONObject music1JSONObject= artistsJSONObject.getJSONObject("music1");
                    Log.d(TAG, "music1: " + music1JSONObject.toString());
                    boolean hasMusic1;
                    if (!artistTeam1final.equals("") && segment.equals("Music") && !music1JSONObject.toString().equals("{}")) {
                        // the first row: name
                        if(artistTeam1final.equals("")) {
                            artist1NameLinearLayout.setVisibility(View.GONE);
                        }
                        else {
                            artist1NameTextView.setText(artistTeam1final);
                        }
                        // the second row: followers
                        String artist1Followers = music1JSONObject.getString("followers");
                        Log.d(TAG, "artist1Followers: " + artist1Followers);
                        if(artist1Followers.equals("")) {
                            artist1FollowersLinearLayout.setVisibility(View.GONE);
                        }
                        else {
                            artist1FollowersTextView.setText(artist1Followers);
                        }
                        // the third row: popularity
                        String artist1Popularity = music1JSONObject.getString("popularity");
                        if(artist1Popularity.equals("")) {
                            artist1PopularityLinearLayout.setVisibility(View.GONE);
                        }
                        else {
                            artist1PopularityTextView.setText(artist1Popularity);
                        }
                        /*
                        // the seventh row: buyTicketAt
                        buyTicketAt = eventJSONObject.getString("buyTicketAt");
                        if (buyTicketAt.equals("")) {
                            buyTicketAtLinearLayout.setVisibility(View.GONE);
                        }
                        else {
                            String ticketHtml = "<a href=\"" + buyTicketAt + "\">Ticketmaster</a>";
                            buyTicketAtTextView.setText(Html.fromHtml(ticketHtml));
                            buyTicketAtTextView.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                        */
                        // the fourth row: checkAt
                        String artist1CheckAt = music1JSONObject.getString("checkAt");
                        if(artist1CheckAt.equals("")) {
                            artist1CheckAtLinearLayout.setVisibility(View.GONE);
                        }
                        else {
                            String CheckAtHtml = "<a href=\"" + artist1CheckAt + "\">Spotify</a>";
                            artist1CheckAtTextView.setText(Html.fromHtml(CheckAtHtml));
                            artist1CheckAtTextView.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                        hasMusic1 = true;
                    }
                    else {
                        artist1LinearLayout.setVisibility(View.GONE);
                        hasMusic1 = false;
                    }

                    // the second part: photos1
                    JSONObject photos1JSONObject= artistsJSONObject.getJSONObject("photos1");
                    boolean hasPhotos1;
                    if (!artistTeam1final.equals("") && photos1JSONObject != null) {
                        // artistTeam1 photo1:
                        String artistTeam1Photo1 = photos1JSONObject.getString("photo1");
                        if (artistTeam1Photo1.equals("")) {
                            artistTeam1Photo1ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam1Photo1).into(artistTeam1Photo1ImageView);
                        }
                        // artistTeam1 photo2:
                        String artistTeam1Photo2 = photos1JSONObject.getString("photo2");
                        if (artistTeam1Photo2.equals("")) {
                            artistTeam1Photo2ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam1Photo2).into(artistTeam1Photo2ImageView);
                        }
                        // artistTeam1 photo3:
                        String artistTeam1Photo3 = photos1JSONObject.getString("photo3");
                        if (artistTeam1Photo3.equals("")) {
                            artistTeam1Photo3ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam1Photo3).into(artistTeam1Photo3ImageView);
                        }
                        // artistTeam1 photo4:
                        String artistTeam1Photo4 = photos1JSONObject.getString("photo4");
                        if (artistTeam1Photo4.equals("")) {
                            artistTeam1Photo4ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam1Photo4).into(artistTeam1Photo4ImageView);
                        }
                        // artistTeam1 photo5:
                        String artistTeam1Photo5 = photos1JSONObject.getString("photo5");
                        if (artistTeam1Photo5.equals("")) {
                            artistTeam1Photo5ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam1Photo5).into(artistTeam1Photo5ImageView);
                        }
                        // artistTeam1 photo6:
                        String artistTeam1Photo6 = photos1JSONObject.getString("photo6");
                        if (artistTeam1Photo6.equals("")) {
                            artistTeam1Photo6ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam1Photo6).into(artistTeam1Photo6ImageView);
                        }
                        // artistTeam1 photo7:
                        String artistTeam1Photo7 = photos1JSONObject.getString("photo7");
                        if (artistTeam1Photo7.equals("")) {
                            artistTeam1Photo7ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam1Photo7).into(artistTeam1Photo7ImageView);
                        }
                        // artistTeam1 photo8:
                        String artistTeam1Photo8 = photos1JSONObject.getString("photo8");
                        if (artistTeam1Photo8.equals("")) {
                            artistTeam1Photo8ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam1Photo8).into(artistTeam1Photo8ImageView);
                        }
                        hasPhotos1 = true;
                    }
                    else {
                        photos1LinearLayout.setVisibility(View.GONE);
                        hasPhotos1 = false;
                    }

                    // the third part: music2
                    JSONObject music2JSONObject= artistsJSONObject.getJSONObject("music2");
                    Log.d(TAG, "music2: " + music2JSONObject.toString());
                    boolean hasMusic2;
                    if (!artistTeam2final.equals("") && segment.equals("Music") && !music2JSONObject.toString().equals("{}")) {
                        // the first row: name
                        if(artistTeam2final.equals("")) {
                            artist2NameLinearLayout.setVisibility(View.GONE);
                        }
                        else {
                            artist2NameTextView.setText(artistTeam2final);
                        }
                        // the second row: followers
                        String artist2Followers = music2JSONObject.getString("followers");
                        if(artist2Followers.equals("")) {
                            artist2FollowersLinearLayout.setVisibility(View.GONE);
                        }
                        else {
                            artist2FollowersTextView.setText(artist2Followers);
                        }
                        // the third row: popularity
                        String artist2Popularity = music2JSONObject.getString("popularity");
                        if(artist2Popularity.equals("")) {
                            artist2PopularityLinearLayout.setVisibility(View.GONE);
                        }
                        else {
                            artist2PopularityTextView.setText(artist2Popularity);
                        }
                        // the fourth row: checkAt
                        String artist2CheckAt = music2JSONObject.getString("checkAt");
                        if(artist2CheckAt.equals("")) {
                            artist2CheckAtLinearLayout.setVisibility(View.GONE);
                        }
                        else {
                            String CheckAtHtml = "<a href=\"" + artist2CheckAt + "\">Spotify</a>";
                            artist2CheckAtTextView.setText(Html.fromHtml(CheckAtHtml));
                            artist2CheckAtTextView.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                        hasMusic2 = true;
                    }
                    else {
                        artist2LinearLayout.setVisibility(View.GONE);
                        hasMusic2 = false;
                    }

                    // the fourth part: photos2
                    JSONObject photos2JSONObject= artistsJSONObject.getJSONObject("photos2");
                    boolean hasPhotos2;
                    if (!artistTeam2final.equals("") && photos2JSONObject != null) {
                        // artistTeam2 photo1:
                        String artistTeam2Photo1 = photos2JSONObject.getString("photo1");
                        if (artistTeam2Photo1.equals("")) {
                            artistTeam2Photo1ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam2Photo1).into(artistTeam2Photo1ImageView);
                        }
                        // artistTeam2 photo2:
                        String artistTeam2Photo2 = photos2JSONObject.getString("photo2");
                        if (artistTeam2Photo2.equals("")) {
                            artistTeam2Photo2ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam2Photo2).into(artistTeam2Photo2ImageView);
                        }
                        // artistTeam2 photo3:
                        String artistTeam2Photo3 = photos2JSONObject.getString("photo3");
                        if (artistTeam2Photo3.equals("")) {
                            artistTeam2Photo3ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam2Photo3).into(artistTeam2Photo3ImageView);
                        }
                        // artistTeam2 photo4:
                        String artistTeam2Photo4 = photos2JSONObject.getString("photo4");
                        if (artistTeam2Photo4.equals("")) {
                            artistTeam2Photo4ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam2Photo4).into(artistTeam2Photo4ImageView);
                        }
                        // artistTeam2 photo5:
                        String artistTeam2Photo5 = photos2JSONObject.getString("photo5");
                        if (artistTeam2Photo5.equals("")) {
                            artistTeam2Photo5ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam2Photo5).into(artistTeam2Photo5ImageView);
                        }
                        // artistTeam2 photo6:
                        String artistTeam2Photo6 = photos2JSONObject.getString("photo6");
                        if (artistTeam2Photo6.equals("")) {
                            artistTeam2Photo6ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam2Photo6).into(artistTeam2Photo6ImageView);
                        }
                        // artistTeam2 photo7:
                        String artistTeam2Photo7 = photos2JSONObject.getString("photo7");
                        if (artistTeam2Photo7.equals("")) {
                            artistTeam2Photo7ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam2Photo7).into(artistTeam2Photo7ImageView);
                        }
                        // artistTeam2 photo8:
                        String artistTeam2Photo8 = photos2JSONObject.getString("photo8");
                        if (artistTeam2Photo8.equals("")) {
                            artistTeam2Photo8ImageView.setVisibility(View.GONE);
                        }
                        else {
                            Picasso.get().load(artistTeam2Photo8).into(artistTeam2Photo8ImageView);
                        }
                        hasPhotos2 = true;
                    }
                    else {
                        photos2LinearLayout.setVisibility(View.GONE);
                        hasPhotos2 = false;
                    }

                    // hide loading
                    loadingLinearLayout.setVisibility(View.GONE);

                    boolean hasArtistTeam1;
                    if (hasMusic1 || hasPhotos1) {
                        artistTeam1LinearLayout.setVisibility(View.VISIBLE);
                        hasArtistTeam1 = true;
                    }
                    else {
                        artistTeam1LinearLayout.setVisibility(View.GONE);
                        hasArtistTeam1 = false;
                    }
                    boolean hasArtistTeam2;
                    if (hasMusic2 || hasPhotos2) {
                        artistTeam2LinearLayout.setVisibility(View.VISIBLE);
                        hasArtistTeam2 = true;
                    }
                    else {
                        artistTeam2LinearLayout.setVisibility(View.GONE);
                        hasArtistTeam2 = false;
                    }
                    if ( !hasArtistTeam1 && !hasArtistTeam2) {
                        noArtistsTextView.setVisibility(View.VISIBLE);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Fail to request backend for artists detail.", Toast.LENGTH_LONG).show();
                // hide loading
                loadingLinearLayout.setVisibility(View.GONE);
            }
        });
        mqueue.add(eventStringRequest);

        return view;
    }

}

