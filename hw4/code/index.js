
var geohash = require("ngeohash");
var body_parser = require("body-parser");
// var query_string = require("querystring");
var request = require("request");
var url = require("url");
var promise = require("promise");
var SpotifyWebApi = require("spotify-web-api-node");
var express = require("express");
var server = express();

 // the first parameter: apikey
var KEY_TICKETMASTER_API = "sWpPtmciRAZg1Ac7LUuAN9JVOU8QY5Q2";
var KEY_GOOGLE_API = "AIzaSyDBxDr0CHnmIPk7dl8NfTe7XOUarxwKiBw";
var SEARCH_ENGINE_ID = "003954211756892606404:hcdjplzqvaw";
var KEY_SONGKICK_API = "vVnlPq96znyWSzU6";

server.use('/public', express.static('public'));
server.use(body_parser.json());
server.use(body_parser.urlencoded({ extended: false }));

server.get("/", function(req, res) {
    var input_client = url.parse(req.url, true).query;
    // console.log(input_form);
    if (typeof input_client.keyword !== 'undefined' && typeof input_client.category !== 'undefined') {
        var input_form = input_client;
        // the second parameter: keyword = keyword.replace(' ', '+');
        var keyword = input_form.keyword.replace(' ', '+');
        // the third parameter: segmentId
        var category = input_form.category;
        var segmentId;
            if(category == "music") {
                segmentId = "KZFzniwnSyZfZ7v7nJ";
            }
            if(category == "sports") {
                $segmentId = "KZFzniwnSyZfZ7v7nE";
            }
            if(category == "arts&theatre") {
                segmentId = "KZFzniwnSyZfZ7v7na";
            }
            if(category == "film") {
                segmentId = "KZFzniwnSyZfZ7v7nn";
            }
            if(category == "miscellaneous") {
                segmentId = "KZFzniwnSyZfZ7v7n1";
            }
            if(category == "all") {
                segmentId = "";
            }
        // the fourth parameter: radius
        var distance = input_form.distance;
        // the fifth parameter: unit
        var unit = input_form.unit;
        // Google API request: 1. current location
        if (typeof input_form.location == "undefined") {
            var latitude = input_form.latitude;
            var longitude = input_form.longitude;
            // the sixth parameter: geoPoint, geohash.encode(37.8324, 112.5584)
            var geoPoint = geohash.encode(latitude, longitude);
            // the seventh parameter: sort
            var sort = "date,asc";
            var url_Ticketmaster_API = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + KEY_TICKETMASTER_API + "&keyword=" + keyword + "&segmentId=" + segmentId + "&radius=" + distance + "&unit=" + unit + "&geoPoint=" + geoPoint + "&sort=" + sort;
            request.get(url_Ticketmaster_API, function(apiError, apiResponse, apiBody){
                var json_Ticketmaster_API = JSON.parse(apiBody);
                res.send(json_Ticketmaster_API);
            });
        }
        // 2. if location is specified
        else {
            var location = input_form.location.replace(' ', '+');
            var url_googleMap_API = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=" + KEY_GOOGLE_API;
            request.get(url_googleMap_API, function(locationError, locationResponse, locationBody) {
                var json_googleMap_API = JSON.parse(locationBody);
                var latitude = json_googleMap_API['results'][0]['geometry']['location']['lat'];
                var longitude = json_googleMap_API['results'][0]['geometry']['location']['lng'];
                // the sixth parameter: geoPoint, geohash.encode(37.8324, 112.5584)
                var geoPoint = geohash.encode(latitude, longitude);
                // the seventh parameter: sort
                var sort = "date,asc";
                var url_Ticketmaster_API = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + KEY_TICKETMASTER_API + "&keyword=" + keyword + "&segmentId=" + segmentId + "&radius=" + distance + "&unit=" + unit + "&geoPoint=" + geoPoint + "&sort=" + sort;
                request.get(url_Ticketmaster_API, function(apiError, apiResponse, apiBody){
                    var json_Ticketmaster_API = JSON.parse(apiBody);
                    res.send(json_Ticketmaster_API);
                });
            });
        }
        
    }

    else if (typeof input_client.eventId !== 'undefined') {
        var input_event = input_client;
        var event_ID = input_event.eventId;
        var url_Ticketmaster_API_event = "https://app.ticketmaster.com/discovery/v2/events/" + event_ID + ".json?apikey=" + KEY_TICKETMASTER_API;
        request.get(url_Ticketmaster_API_event, function(apiError, apiResponse, apiBody) {
            var json_Ticketmaster_API_event = JSON.parse(apiBody);
            res.send(json_Ticketmaster_API_event);
        });
    }

    else if (typeof input_client.artistName !== 'undefined') {
        var artistName = input_client.artistName;
        // console.log(artistsName);
        var spotifyApi = new SpotifyWebApi({
            clientId: 'ecdf6c6e2b184c7d9174755396b312a4',
            clientSecret: 'aa8f2d22de474571b74b75d7c09de17a',
        });
        // Retrieve an access token
        spotifyApi.clientCredentialsGrant().then(function(data) {
            console.log('The access token expires in ' + data.body['expires_in']);
            console.log('The access token is ' + data.body['access_token']);
            // Save the access token so that it's used in future calls
            spotifyApi.setAccessToken(data.body['access_token']);
            spotifyApi.searchArtists(artistName).then(function(data) {
                res.send(data.body);
            });
        },function(err) {
            console.log('Something went wrong when retrieving an access token', err.message);
            spotifyApi.clientCredentialsGrant().then(function(data) {
                console.log('The access token expires in ' + data.body['expires_in']);
                console.log('The access token is ' + data.body['access_token']);
                // Save the access token so that it's used in future calls
                spotifyApi.setAccessToken(data.body['access_token']);
                spotifyApi.searchArtists(artistName).then(function(data) {
                // console.log(data.body);
                res.send(data.body);
                });
            });
        });
    } // end else if

    else if (typeof input_client.artistTeam !== 'undefined') {
        var artistTeam = input_client.artistTeam;
        artistTeam = artistTeam.replace(' ', '+');
        var url_Google_API_artistTeam = "https://www.googleapis.com/customsearch/v1?q=" + artistTeam + "&cx=" + SEARCH_ENGINE_ID + "&imgSize=huge&num=8&searchType=image&key=" + KEY_GOOGLE_API;
        request.get(url_Google_API_artistTeam, function(apiError, apiResponse, apiBody) {
            var json_Google_API_artistTeam = JSON.parse(apiBody);
            res.send(json_Google_API_artistTeam);
        });
    }
    //end else if

    else if (typeof input_client.venue !== 'undefined') {
        var venueName = input_client.venueName;
        venueName = venueName.replace(' ', '+');
        // Ticketmaster API: venue search
        var url_Ticketmaster_API_venue = "https://app.ticketmaster.com/discovery/v2/venues.json?keyword=" + venueName + "&apikey=" + KEY_TICKETMASTER_API;
        request.get(url_Ticketmaster_API_venue, function(apiError, apiResponse, apiBody) {
            var json_Ticketmaster_API_venue = JSON.parse(apiBody);
            res.send(json_Ticketmaster_API_venue);
        });
    }
    //end else if

    // https://api.songkick.com/api/3.0/search/venues.json?query=STAPLES+Center&apikey=vVnlPq96znyWSzU6
    else if (typeof input_client.upcomingEvents !== 'undefined') {
        var venueName = input_client.venueName;
        venueName = venueName.replace(' ', '+');
        // songkick search: for id
        var url_Songkick_API_ID = "https://api.songkick.com/api/3.0/search/venues.json?query=" + venueName + "&apikey=" + KEY_SONGKICK_API;
        request.get(url_Songkick_API_ID, function(apiError, apiResponse, apiBody) {
            var json_Songkick_API_ID = JSON.parse(apiBody);
            if (json_Songkick_API_ID.resultsPage && json_Songkick_API_ID.resultsPage.results && json_Songkick_API_ID.resultsPage.results.venue && json_Songkick_API_ID.resultsPage.results.venue[0].id) {
                var venueId = json_Songkick_API_ID.resultsPage.results.venue[0].id;
                // console.log(venueId); https://api.songkick.com/api/3.0/venues/598/calendar.json?apikey=vVnlPq96znyWSzU6
                var url_Songkick_API_events = "https://api.songkick.com/api/3.0/venues/" + venueId + "/calendar.json?apikey=" + KEY_SONGKICK_API;
                request.get(url_Songkick_API_events, function(apiError, apiResponse, apiBody) {
                    var json_Songkick_API_events = JSON.parse(apiBody);
                    res.send(json_Songkick_API_events);
                }); // end request
            }
            else {
                var str_Songkick_API_ID_error = "This venue name doesn't have ID!";
                // var json_Songkick_API_ID_error = JSON.parse(str_Songkick_API_ID_error);
                res.send(str_Songkick_API_ID_error);
            }
        }); // end function(3)
    }

    else if (typeof input_client.searchText !== 'undefined') {
        var searchText =  input_client.searchText;
        searchText = searchText.replace(' ', '+');
        //https://app.ticketmaster.com/discovery/v2/suggest?apikey= sWpPtmciRAZg1Ac7LUuAN9JVOU8QY5Q2&keyword=laker
        var url_Ticketmaster_API_autocomplete = "https://app.ticketmaster.com/discovery/v2/suggest?apikey=" + KEY_TICKETMASTER_API + "&keyword=" + searchText;
        request.get(url_Ticketmaster_API_autocomplete, function(apiError, apiResponse, apiBody) {
            var json_autocomplete = JSON.parse(apiBody);
            var array_searchResults = [];
            if (json_autocomplete._embedded && json_autocomplete._embedded.attractions) {
                var array_attractions = json_autocomplete._embedded.attractions;
                for (var i = 0; i < array_attractions.length; i++) {
                    if(array_attractions[i].name) {
                        var name = array_attractions[i].name;
                        array_searchResults.push(name);
                    }
                }
            }
            res.send(array_searchResults);
        });
    }
    
});
server.listen(8081, function(){
    console.log("Start successfully，please visit http://127.0.0.1:8081/public/index.html");
});
