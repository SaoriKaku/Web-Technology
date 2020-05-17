package com.example.suguo.eventsearch;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class VenueFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "VenueFragment";

    public String venueString;
    public JSONObject venueJSONObject;

    private LinearLayout hasVenueLinearLayout;
    private LinearLayout nameLinearLayout;
    private LinearLayout addressLinearLayout;
    private LinearLayout cityLinearLayout;
    private LinearLayout phoneNumberLinearLayout;
    private LinearLayout openHoursLinearLayout;
    private LinearLayout generalRuleLinearLayout;
    private LinearLayout childRuleLinearLayout;

    private TextView nameTextView;
    private TextView addressTextView;
    private TextView cityTextView;
    private TextView phoneNumberTextView;
    private TextView openHoursTextView;
    private TextView generalRuleTextView;
    private TextView childRuleTextView;
    private TextView noVenueTextView;

    private String name;
    private String address;
    private String city;
    private String phoneNumber;
    private String openHours;
    private String generalRule;
    private String childRule;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private GoogleMap mMap;

    public VenueFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        hasVenueLinearLayout = (LinearLayout)view.findViewById(R.id.venueFragment_linearLayout_hasVenue);
        nameLinearLayout = (LinearLayout)view.findViewById(R.id.venueFragment_linearLayout_name);
        addressLinearLayout = (LinearLayout)view.findViewById(R.id.venueFragment_linearLayout_address);
        cityLinearLayout = (LinearLayout)view.findViewById(R.id.venueFragment_linearLayout_city);
        phoneNumberLinearLayout = (LinearLayout)view.findViewById(R.id.venueFragment_linearLayout_phoneNumber);
        openHoursLinearLayout = (LinearLayout)view.findViewById(R.id.venueFragment_linearLayout_openHours);
        generalRuleLinearLayout = (LinearLayout)view.findViewById(R.id.venueFragment_linearLayout_generalRule);
        childRuleLinearLayout = (LinearLayout)view.findViewById(R.id.venueFragment_linearLayout_childRule);
        //googleMapFrameLayout = (FrameLayout)view.findViewById(R.id.venueFragment_frameLayout_googleMap);

        nameTextView = (TextView)view.findViewById(R.id.venueFragment_textView_name);
        addressTextView = (TextView)view.findViewById(R.id.venueFragment_textView_address);
        cityTextView = (TextView)view.findViewById(R.id.venueFragment_textView_city);
        phoneNumberTextView = (TextView)view.findViewById(R.id.venueFragment_textView_phoneNumber);
        openHoursTextView = (TextView)view.findViewById(R.id.venueFragment_textView_openHours);
        generalRuleTextView = (TextView)view.findViewById(R.id.venueFragment_textView_generalRule);
        childRuleTextView = (TextView)view.findViewById(R.id.venueFragment_textView_childRule);
        noVenueTextView = (TextView)view.findViewById(R.id.venueFragment_textView_noVenue);

        Bundle bundle = this.getArguments();
        venueString = bundle.getString("venue");
        //Log.d(TAG, "venue: " + venueString);

        try {
            venueJSONObject = new JSONObject(venueString);
            generateVenue();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        // google map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.venueFragment_fragment_googleMap);
        //SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.venueFragment_fragment_googleMap, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        String latitude = null;
        String longitude = null;
        try {
            latitude = venueJSONObject.getString("latitude");
            longitude = venueJSONObject.getString("longitude");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ((name.equals("")) && (address.equals("")) && (city.equals("")) && (phoneNumber.equals("")) && (openHours.equals("")) && (generalRule.equals("")) && (childRule.equals("")) && (latitude.equals("")) && (longitude.equals(""))) {
            hasVenueLinearLayout.setVisibility(View.GONE);
            noVenueTextView.setVisibility(View.VISIBLE);
        }
        else {
            hasVenueLinearLayout.setVisibility(View.VISIBLE);
            noVenueTextView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Log.d(TAG, "onMapReady: map ready");
        try {
            destinationLatitude = Double.valueOf(venueJSONObject.getString("latitude"));
            destinationLongitude = Double.valueOf(venueJSONObject.getString("longitude"));
            String destinationName = venueJSONObject.getString("name");
            LatLng destinationLocation = new LatLng(destinationLatitude, destinationLongitude);
            mMap.addMarker(new MarkerOptions().position(destinationLocation).title(destinationName));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLocation));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void generateVenue() throws JSONException {
        // the first row: name
        name = venueJSONObject.getString("name");
        if (name.equals("")) {
            nameLinearLayout.setVisibility(View.GONE);
        }
        else {
            nameTextView.setText(name);
        }
        // the second row: address
        address = venueJSONObject.getString("address");
        if (address.equals("")) {
            addressLinearLayout.setVisibility(View.GONE);
        }
        else {
            addressTextView.setText(address);
        }
        // the third row: city
        city = venueJSONObject.getString("city");
        if (city.equals("")) {
            cityLinearLayout.setVisibility(View.GONE);
        }
        else {
            cityTextView.setText(city);
        }
        // the fourth row: phoneNumber
        phoneNumber = venueJSONObject.getString("phoneNumber");
        if (phoneNumber.equals("")) {
            phoneNumberLinearLayout.setVisibility(View.GONE);
        }
        else {
            phoneNumberTextView.setText(phoneNumber);
        }
        // the fifth row: openHours
        openHours = venueJSONObject.getString("openHours");
        if (openHours.equals("")) {
            openHoursLinearLayout.setVisibility(View.GONE);
        }
        else {
            openHoursTextView.setText(openHours);
        }
        // the sixth row: generalRule
        generalRule = venueJSONObject.getString("generalRule");
        if (generalRule.equals("")) {
            generalRuleLinearLayout.setVisibility(View.GONE);
        }
        else {
            generalRuleTextView.setText(generalRule);
        }
        // the seventh row: childRule
        childRule = venueJSONObject.getString("childRule");
        if (childRule.equals("")) {
            childRuleLinearLayout.setVisibility(View.GONE);
        }
        else {
            childRuleTextView.setText(childRule);
        }
    }
}
