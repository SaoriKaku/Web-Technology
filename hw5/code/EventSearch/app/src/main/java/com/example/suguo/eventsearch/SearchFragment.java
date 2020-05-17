package com.example.suguo.eventsearch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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

// autocomplete import
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SearchFragment";
    private Intent intentToResultsActivity;

    private LinearLayout containerLinearLayout;
    private LinearLayout progressBarLinearLayout;
    private TextView keywordErrorTextView;
    //private EditText keywordEditText;
    private Spinner categorySpinner;
    private EditText distanceEditText;
    private Spinner unitSpinner;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private TextView locationErrorTextView;
    private EditText locationEditText;
    private Button searchButton;
    private Button clearButton;

    private String keyword;
    private String category;
    private String distance;
    private String unit;
    private String latitude;
    private String longitude;
    private String location;

    // autocomplete variable
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private AppCompatAutoCompleteTextView autoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        intentToResultsActivity = new Intent(this.getActivity(), ResultsActivity.class);

        containerLinearLayout = (LinearLayout) view.findViewById(R.id.searchFragment_container);
        progressBarLinearLayout = (LinearLayout) view.findViewById(R.id.searchFragment_progressBar);
        keywordErrorTextView = (TextView) view.findViewById(R.id.searchFragment_error_keyword);
        //keywordEditText = (EditText)view.findViewById(R.id.searchFragment_editText_keyword);
        categorySpinner = (Spinner) view.findViewById(R.id.searchFragment_spinner_category);
        distanceEditText = (EditText) view.findViewById(R.id.searchFragment_editText_distance);
        unitSpinner = (Spinner) view.findViewById(R.id.searchFragment_spinner_unit);
        radioButton1 = (RadioButton) view.findViewById(R.id.searchFragment_radio1);
        // add event listeners to radiobutton1
        radioButton1.setOnClickListener(this);
        radioButton2 = (RadioButton) view.findViewById(R.id.searchFragment_radio2);
        // add event listeners to radiobutton2
        radioButton2.setOnClickListener(this);
        locationErrorTextView = (TextView) view.findViewById(R.id.searchFragment_error_location);
        locationEditText = (EditText) view.findViewById(R.id.searchFragment_editText_location);
        searchButton = (Button) view.findViewById(R.id.searchFragment_button_search);
        // add event listeners to button search
        searchButton.setOnClickListener(this);
        clearButton = (Button) view.findViewById(R.id.searchFragment_button_clear);
        // add event listeners to button clear
        clearButton.setOnClickListener(this);

        // autocomplete part:
        autoCompleteTextView = (AppCompatAutoCompleteTextView) view.findViewById(R.id.auto_complete_edit_text);
        //final TextView selectedText = (TextView)view.findViewById(R.id.selected_item);
        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedText.setText(autoSuggestAdapter.getObject(position));
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        getLocationPermission();
        return view;
    }

    // autocomplete: request method
    private void makeApiCall(String text) {
        ApiCall.make(getActivity(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                ArrayList<String> keywordArrayList = new ArrayList<>();
                String[] array = response.split(",");
                for (int i = 0; i < array.length; i++) {
                    keywordArrayList.add(array[i]);
                }
                Log.d(TAG, "keywordArrayList: " + keywordArrayList.toString());
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(keywordArrayList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Fail to request backend for autocomplete.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        containerLinearLayout.setVisibility(View.VISIBLE);
    }


    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getContext(), "You refuse to access this device's location", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an app-defined int constant. The callback method gets the result of the request.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            if (locationManager != null) {
                Location geoLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (geoLocation != null) {
                    latitude = String.valueOf(geoLocation.getLatitude());
                    longitude = String.valueOf(geoLocation.getLongitude());
                    //Toast.makeText(getContext(), latitude + "," + longitude, Toast.LENGTH_LONG).show();
                } else {
                    latitude = "0.0";
                    longitude = "0.0";
                    Toast.makeText(getContext(), "Errors1: Cannot get current location", Toast.LENGTH_SHORT).show();
                }
            } else {
                latitude = "0.0";
                longitude = "0.0";
                Toast.makeText(getContext(), "Errors2: Cannot get current location", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                    Location geoLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    latitude = String.valueOf(geoLocation.getLatitude());
                    longitude = String.valueOf(geoLocation.getLongitude());
                }
                // permission denied, boo! Disable the functionality that depends on this permission.
                else {
                    Toast.makeText(getActivity(),"Location Permission Denied. Please allow location permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchFragment_radio1:
                validateRadio();
                break;
            case R.id.searchFragment_radio2:
                validateRadio();
                break;
            case R.id.searchFragment_button_search:
                validateAndSearch();
                break;
            case R.id.searchFragment_button_clear:
                clearInput();
                break;
        }
    }


    public void validateRadio() {
        if(radioButton1.isChecked()) {
            locationEditText.setText("");
            locationEditText.setEnabled(false);
        }
        else {
            locationEditText.setEnabled(true);
        }
    }


    public void validateAndSearch() {
        getInput();
        keywordErrorTextView.setVisibility(View.GONE);
        locationErrorTextView.setVisibility(View.GONE);

        if(radioButton1.isChecked()) {
            if(keyword.isEmpty()) {
                keywordErrorTextView.setVisibility(View.VISIBLE);
                Toast.makeText(this.getActivity(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
            }
            else {
                search();
            }
        } // end if
        else {
            location = locationEditText.getText().toString();
            keywordErrorTextView.setVisibility(View.GONE);
            locationErrorTextView.setVisibility(View.GONE);
            if(keyword.isEmpty() || location.isEmpty()) {
                if (keyword.isEmpty()) {
                    keywordErrorTextView.setVisibility(View.VISIBLE);
                }
                if (location.isEmpty()) {
                    locationErrorTextView.setVisibility(View.VISIBLE);
                }
                Toast.makeText(this.getActivity(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
            }
            else {
                search();
            }
        } // end else
    }


    public void getInput() {
        //keyword = keywordEditText.getText().toString();
        keyword = autoCompleteTextView.getText().toString();
        category = categorySpinner.getSelectedItem().toString();
        distance = distanceEditText.getText().toString();
        if(distance.isEmpty()) {
            distance = 10 + "";
        }
        unit = unitSpinner.getSelectedItem().toString();
    }


    public void search() {
        // show progressbar hide container
        containerLinearLayout.setVisibility(View.GONE);
        progressBarLinearLayout.setVisibility(View.VISIBLE);

        keyword = keyword.replaceAll(" ", "%20");
        category = category.toLowerCase();
        unit = unit.toLowerCase();
        String mUrl = "http://csci571-hw9-android.us-east-2.elasticbeanstalk.com/?";
        mUrl += "keyword=" + keyword + "&category=" + category + "&distance=" + distance + "&unit=" + unit;

        if(radioButton1.isChecked()) {
            mUrl += "&latitude=" + latitude + "&longitude=" + longitude;
        }
        else {
            location = location.replaceAll(" ", "%20");
            mUrl += "&location=" + location;
        }
        Log.d(TAG, "url: " + mUrl);
        RequestQueue mqueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG, "response: " + response);
                    intentToResultsActivity.putExtra("results", response);
                    // show container hide ProgressBar
                    //containerLinearLayout.setVisibility(View.VISIBLE);
                    progressBarLinearLayout.setVisibility(View.INVISIBLE);
                    // turn to ResultsActivity
                    getActivity().startActivity(intentToResultsActivity);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Fail to request backend for search results.", Toast.LENGTH_LONG).show();
                // show container hide ProgressBar
                //containerLinearLayout.setVisibility(View.VISIBLE);
                progressBarLinearLayout.setVisibility(View.INVISIBLE);
            }
        });
        mqueue.add(stringRequest);
    }


    public void clearInput() {
        keywordErrorTextView.setVisibility(View.GONE);
        //keywordEditText.setText("");
        autoCompleteTextView.setText("");
        categorySpinner.setSelection(0);
        distanceEditText.setText("");
        unitSpinner.setSelection(0);
        radioButton1.setChecked(true);
        locationErrorTextView.setVisibility(View.GONE);
        locationEditText.setText("");
        locationEditText.setEnabled(false);
    }

}