package com.example.suguo.eventsearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class UpcomingFragment extends Fragment implements UpcomingAdapter.ListItemClickListener {

    private ArrayList<UpcomingEvent> upcomingEventArrayList;
    private ArrayList<UpcomingEvent> defaultUpcomingEventArrayList;
    private UpcomingAdapter upcomingAdapter;
    private Spinner sortSpinner;
    private Spinner orderSpinner;

    public UpcomingFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        String upcomingString = getArguments().getString("upcoming");
        upcomingEventArrayList = new ArrayList<>();
        try {
            if (upcomingString != null && !upcomingString.equals("")) {
                JSONArray upcomingJSONArray = new JSONArray(upcomingString);
                for (int i = 0; i < upcomingJSONArray.length(); i++) {
                    JSONObject upcomingJSONObject = upcomingJSONArray.getJSONObject(i);
                    String displayName = upcomingJSONObject.getString("displayName");
                    String uri = upcomingJSONObject.getString("uri");
                    String artist = upcomingJSONObject.getString("artist");
                    String dateTime = upcomingJSONObject.getString("dateTime");
                    Long timeStamp = Long.valueOf(upcomingJSONObject.getString("timeStamp"));
                    String type = upcomingJSONObject.getString("type");
                    upcomingEventArrayList.add(new UpcomingEvent(displayName, uri, artist, dateTime, timeStamp, type));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        defaultUpcomingEventArrayList = upcomingEventArrayList;

        // set view
        RecyclerView recyclerView = view.findViewById(R.id.upcomingFragment_recyclerView_upcoming);
        TextView noRecordTextView = view.findViewById(R.id.upcomingFragment_textView_noRecord);
        sortSpinner = view.findViewById(R.id.upcomingFragment_spinner_sort);
        orderSpinner = view.findViewById(R.id.upcomingFragment_spinner_order);

        // set adapter
        upcomingAdapter = new UpcomingAdapter(upcomingEventArrayList, this);
        recyclerView.setAdapter(upcomingAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // sortSpinner click
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortAndOrder(sortSpinner.getSelectedItem().toString(), orderSpinner.getSelectedItem().toString());
                upcomingAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // orderSpinner click
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortAndOrder(sortSpinner.getSelectedItem().toString(), orderSpinner.getSelectedItem().toString());
                upcomingAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (upcomingEventArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noRecordTextView.setVisibility(View.VISIBLE);
            sortSpinner.setEnabled(false);
            orderSpinner.setEnabled(false);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noRecordTextView.setVisibility(View.GONE);
        }
        return view;
    }


    // Webpage for the SongKick event
    @Override
    public void onListItemClick(int clickItemIndex) {
        Uri songKickPage = Uri.parse(upcomingEventArrayList.get(clickItemIndex).getUri());
        Intent mIntent = new Intent(Intent.ACTION_VIEW, songKickPage);
        if (mIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mIntent);
        }
    }


    public void sortAndOrder(String sort, String order) {
        // sort spinner: Default
        if (sort.equals("Default")) {
            upcomingEventArrayList = defaultUpcomingEventArrayList;
            orderSpinner.setEnabled(false);
        }
        // sort spinner: Event Name
        else if (sort.equals("Event Name")) {
            orderSpinner.setEnabled(true);
            // order spinner: Ascending
            if (order.equals("Ascending")) {
                sortByEventName(1);
            }
            // order spinner: Descending
            else {
                sortByEventName(-1);
            }
        }
        // order spinner: Time
        else if (sort.equals("Time")) {
            orderSpinner.setEnabled(true);
            // order spinner: Ascending
            if (order.equals("Ascending")) {
                sortByTime(1);
            }
            // order spinner: Descending
            else {
                sortByTime(-1);
            }
        }
        // order spinner: Artist
        else if (sort.equals("Artist")) {
            orderSpinner.setEnabled(true);
            // order spinner: Ascending
            if (order.equals("Ascending")) {
                sortByArtist(1);
            }
            // order spinner: Descending
            else {
                sortByArtist(-1);
            }
        }
        // order spinner: Type
        else {
            orderSpinner.setEnabled(true);
            // order spinner: Ascending
            if (order.equals("Type")) {
                sortByType(1);
            }
            // order spinner: Descending
            else {
                sortByType(-1);
            }
        }
    }


    public void sortByEventName(final int order) {
        Collections.sort(upcomingEventArrayList, new Comparator<UpcomingEvent>() {
            @Override
            public int compare(UpcomingEvent upcoming1, UpcomingEvent upcoming2) {
                String eventName1 = upcoming1.getDisplayName();
                String eventName2 = upcoming2.getDisplayName();
                if (order == 1) {
                    return eventName1.compareTo(eventName2);
                }
                else {
                    return eventName2.compareTo(eventName1);
                }
            }
        });
    }


    public void sortByTime(final int order) {
        Collections.sort(upcomingEventArrayList, new Comparator<UpcomingEvent>() {
            @Override
            public int compare(UpcomingEvent upcoming1, UpcomingEvent upcoming2) {
                Long time1 = upcoming1.getTimeStamp();
                Long time2 = upcoming2.getTimeStamp();
                if (order == 1) {
                    return time1.compareTo(time2);
                }
                else {
                    return time2.compareTo(time1);
                }
            }
        });
    }


    public void sortByArtist(final int order) {
        Collections.sort(upcomingEventArrayList, new Comparator<UpcomingEvent>() {
            @Override
            public int compare(UpcomingEvent upcoming1, UpcomingEvent upcoming2) {
                String artist1 = upcoming1.getArtist();
                String artist2 = upcoming2.getArtist();
                if (order == 1) {
                    return artist1.compareTo(artist2);
                }
                else {
                    return artist2.compareTo(artist1);
                }
            }
        });
    }


    public void sortByType(final int order) {
        Collections.sort(upcomingEventArrayList, new Comparator<UpcomingEvent>() {
            @Override
            public int compare(UpcomingEvent upcoming1, UpcomingEvent upcoming2) {
                String type1 = upcoming1.getType();
                String type2 = upcoming2.getType();
                if (order == 1) {
                    return type1.compareTo(type2);
                }
                else {
                    return type2.compareTo(type1);
                }
            }
        });
    }

}
