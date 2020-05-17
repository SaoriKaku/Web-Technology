package com.example.suguo.eventsearch;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class EventFragment extends Fragment {
    private static final String TAG = "EventFragment";

    public String eventString;
    public JSONObject eventJSONObject;

    private LinearLayout hasEventLinearLayout;
    private LinearLayout artistTeamsLinearLayout;
    private LinearLayout venueLinearLayout;
    private LinearLayout timeLinearLayout;
    private LinearLayout categoryLinearLayout;
    private LinearLayout priceRangeLinearLayout;
    private LinearLayout ticketStatusLinearLayout;
    private LinearLayout buyTicketAtLinearLayout;
    private LinearLayout seatMapLinearLayout;

    private TextView artistTeamsTextView;
    private TextView venueTextView;
    private TextView timeTextView;
    private TextView categoryTextView;
    private TextView priceRangeTextView;
    private TextView ticketStatusTextView;
    private TextView buyTicketAtTextView;
    private TextView seatMapTextView;
    private TextView noEventTextView;

    private String artistTeams;
    private String venue;
    private String time;
    private String category;
    private String priceRange;
    private String ticketStatus;
    private String buyTicketAt;
    private String seatMap;

    public EventFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        hasEventLinearLayout = (LinearLayout)view.findViewById(R.id.eventFragment_linearLayout_hasEvent);
        artistTeamsLinearLayout = (LinearLayout)view.findViewById(R.id.eventFragment_linearLayout_artistTeams);
        venueLinearLayout = (LinearLayout)view.findViewById(R.id.eventFragment_linearLayout_venue);
        timeLinearLayout = (LinearLayout)view.findViewById(R.id.eventFragment_linearLayout_time);
        categoryLinearLayout = (LinearLayout)view.findViewById(R.id.eventFragment_linearLayout_category);
        priceRangeLinearLayout = (LinearLayout)view.findViewById(R.id.eventFragment_linearLayout_priceRange);
        ticketStatusLinearLayout = (LinearLayout)view.findViewById(R.id.eventFragment_linearLayout_ticketStatus);
        buyTicketAtLinearLayout = (LinearLayout)view.findViewById(R.id.eventFragment_linearLayout_buyTicketAt);
        seatMapLinearLayout = (LinearLayout)view.findViewById(R.id.eventFragment_linearLayout_seatMap);

        artistTeamsTextView = (TextView)view.findViewById(R.id.eventFragment_textView_artistTeams);
        venueTextView = (TextView)view.findViewById(R.id.eventFragment_textView_venue);
        timeTextView = (TextView)view.findViewById(R.id.eventFragment_textView_time);
        categoryTextView = (TextView)view.findViewById(R.id.eventFragment_textView_category);
        priceRangeTextView = (TextView)view.findViewById(R.id.eventFragment_textView_priceRange);
        ticketStatusTextView = (TextView)view.findViewById(R.id.eventFragment_textView_ticketStatus);
        buyTicketAtTextView = (TextView)view.findViewById(R.id.eventFragment_textView_buyTicketAt);
        seatMapTextView = (TextView)view.findViewById(R.id.eventFragment_textView_seatMap);
        noEventTextView = (TextView)view.findViewById(R.id.eventFragment_textView_noEvent);

        Bundle bundle = this.getArguments();
        eventString = bundle.getString("event");
        //Log.d(TAG, "event: " + eventString);
        try {
            eventJSONObject = new JSONObject(eventString);
            generateEvent();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if ((artistTeams.equals("")) && (venue.equals("")) && (time.equals("")) && (category.equals("")) && (priceRange.equals("")) && (ticketStatus.equals("")) && (buyTicketAt.equals("")) && (seatMap.equals(""))) {
            hasEventLinearLayout.setVisibility(View.GONE);
            noEventTextView.setVisibility(View.VISIBLE);
        }
        else {
            hasEventLinearLayout.setVisibility(View.VISIBLE);
            noEventTextView.setVisibility(View.GONE);
        }

        return view;
    }

    public void generateEvent() throws JSONException {
        // the first row: artistTeams
        artistTeams = eventJSONObject.getString("artistTeams");
        if (artistTeams.equals("")) {
            artistTeamsLinearLayout.setVisibility(View.GONE);
        }
        else {
            artistTeamsTextView.setText(artistTeams);
        }
        // the second row: venue
        venue = eventJSONObject.getString("venue");
        if (venue.equals("")) {
            venueLinearLayout.setVisibility(View.GONE);
        }
        else {
            venueTextView.setText(venue);
        }
        // the third row: time
        time = eventJSONObject.getString("time");
        if (time.equals("")) {
            timeLinearLayout.setVisibility(View.GONE);
        }
        else {
            timeTextView.setText(time);
        }
        // the fourth row: category
        category = eventJSONObject.getString("category");
        if (category.equals("")) {
            categoryLinearLayout.setVisibility(View.GONE);
        }
        else {
            categoryTextView.setText(category);
        }
        // the fifth row: category
        priceRange = eventJSONObject.getString("priceRange");
        if (priceRange.equals("")) {
            priceRangeLinearLayout.setVisibility(View.GONE);
        }
        else {
            priceRangeTextView.setText(priceRange);
        }
        // the sixth row: ticketStatus
        ticketStatus = eventJSONObject.getString("ticketStatus");
        if (ticketStatus.equals("")) {
            ticketStatusLinearLayout.setVisibility(View.GONE);
        }
        else {
            ticketStatusTextView.setText(ticketStatus);
        }
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
        // the eigthth row: seatMap
        seatMap = eventJSONObject.getString("seatMap");
        if (seatMap.equals("")) {
            seatMapLinearLayout.setVisibility(View.GONE);
        }
        else {
            String seatMapHtml = "<a href=\"" + seatMap + "\">View Here</a>";
            seatMapTextView.setText(Html.fromHtml(seatMapHtml));
            seatMapTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}