var geohash = require("ngeohash");
var body_parser = require("body-parser");
// var query_string = require("querystring");
var request = require("request");
var url = require("url");
var promise = require("promise");
var SpotifyWebApi = require("spotify-web-api-node");
var moment = require("moment");
var express = require("express");
var server = express();

 // the first parameter: apikey
var KEY_TICKETMASTER_API = "sWpPtmciRAZg1Ac7LUuAN9JVOU8QY5Q2";
var KEY_TICKETMASTER_API2 = "waFCpGCZUJ8m2cdxiEYsrMNwaO2ep3AB";
var KEY_GOOGLE_API = "AIzaSyDBxDr0CHnmIPk7dl8NfTe7XOUarxwKiBw";
var SEARCH_ENGINE_ID = "003954211756892606404:hcdjplzqvaw";
var KEY_SONGKICK_API = "vVnlPq96znyWSzU6";

// server.use('/public', express.static('public'));
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
            var url_Ticketmaster_API = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + KEY_TICKETMASTER_API2 + "&keyword=" + keyword + "&segmentId=" + segmentId + "&radius=" + distance + "&unit=" + unit + "&geoPoint=" + geoPoint + "&sort=" + sort;
            request.get(url_Ticketmaster_API, function(apiError, apiResponse, apiBody){
                var jsonObj_results = JSON.parse(apiBody);
                // array_results to store needed data from jsonObj_results
                if (jsonObj_results._embedded && jsonObj_results._embedded.events) {
                    var events = jsonObj_results._embedded.events;
                    // structure of array_results:
                    // [{1 eventId: string, 2 segment: string, 3 eventName: string, 4 venueName: string, 5 dateTime: string}, ...]
                    var array_results = [];
                    for (var i = 0; i < events.length; i++) {
                        array_results[i] = {};
                        // the first element: eventId
                        array_results[i].eventId = events[i].id;
                        // the second element: segment
                        if (events[i].classifications && events[i].classifications[0].segment && events[i].classifications[0].segment.name) {
                            array_results[i].segment = events[i].classifications[0].segment.name;
                        }
                        else {
                            array_results[i].segment = "";
                        }
                        // the third element: eventName
                        if (events[i].name) {
                            array_results[i].eventName = events[i].name;
                        }
                        else {
                            array_results[i].eventName = "";
                        }
                         // the fourth element: venueName
                        if (events[i]._embedded && events[i]._embedded.venues && events[i]._embedded.venues[0].name) {
                            array_results[i].venueName = events[i]._embedded.venues[0].name;
                        }
                        else {
                            array_results[i].venueName = "";
                        }
                        // the five column: dateTime
                        if (events[i].dates && events[i].dates.start && events[i].dates.start.localDate) {
                            var localDate = events[i].dates.start.localDate;
                        }
                        else {
                            var localDate = "";
                        }
                        if (events[i].dates && events[i].dates.start && events[i].dates.start.localTime) {
                            var localTime = events[i].dates.start.localTime;
                        }
                        else {
                            var localTime = "";
                        }
                        array_results[i].dateTime = localDate + " " + localTime;
                    }// end for
                }// end if
                else {
                    array_results = [];
                }// end else
                res.send(JSON.stringify(array_results));
            });
        }
        // 2. if location is specified
        else {
            var location = input_form.location;
            var url_googleMap_API = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=" + KEY_GOOGLE_API;
            request.get(url_googleMap_API, function(locationError, locationResponse, locationBody) {
                var json_googleMap_API = JSON.parse(locationBody);
                //var latitude = json_googleMap_API['results'][0]['geometry']['location']['lat'];
                //var longitude = json_googleMap_API['results'][0]['geometry']['location']['lng'];
                if (json_googleMap_API.results && json_googleMap_API.results[0] && json_googleMap_API.results[0].geometry && json_googleMap_API.results[0].geometry.location && json_googleMap_API.results[0].geometry.location.lat) {
                    var latitude = json_googleMap_API.results[0].geometry.location.lat;
                }
                else {
                    var latitude = "";
                }
                if (json_googleMap_API.results && json_googleMap_API.results[0] && json_googleMap_API.results[0].geometry && json_googleMap_API.results[0].geometry.location && json_googleMap_API.results[0].geometry.location.lng) {
                    var longitude = json_googleMap_API.results[0].geometry.location.lng;
                }
                else {
                     var longitude = "";
                }
                // the sixth parameter: geoPoint, geohash.encode(37.8324, 112.5584)
                var geoPoint = geohash.encode(latitude, longitude);
                // the seventh parameter: sort
                var sort = "date,asc";
                var url_Ticketmaster_API = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + KEY_TICKETMASTER_API2 + "&keyword=" + keyword + "&segmentId=" + segmentId + "&radius=" + distance + "&unit=" + unit + "&geoPoint=" + geoPoint + "&sort=" + sort;
                request.get(url_Ticketmaster_API, function(apiError, apiResponse, apiBody){
                    var jsonObj_results = JSON.parse(apiBody);
                    // array_results to store needed data from jsonObj_results
                    if (jsonObj_results._embedded && jsonObj_results._embedded.events) {
                        var events = jsonObj_results._embedded.events;
                        // structure of array_results:
                        // [{1 eventId: string, 2 segment: string, 3 eventName: string, 4 venueName: string, 5 dateTime: string}, ...]
                        var array_results = [];
                        for (var i = 0; i < events.length; i++) {
                            array_results[i] = {};
                            // the first element: eventId
                            array_results[i].eventId = events[i].id;
                            // the second element: segment
                            if (events[i].classifications && events[i].classifications[0].segment && events[i].classifications[0].segment.name) {
                                array_results[i].segment = events[i].classifications[0].segment.name;
                            }
                            else {
                                array_results[i].segment = "";
                            }
                            // the third element: eventName
                            if (events[i].name) {
                                array_results[i].eventName = events[i].name;
                            }
                            else {
                                array_results[i].eventName = "";
                            }
                             // the fourth element: venueName
                            if (events[i]._embedded && events[i]._embedded.venues && events[i]._embedded.venues[0].name) {
                                array_results[i].venueName = events[i]._embedded.venues[0].name;
                            }
                            else {
                                array_results[i].venueName = "";
                            }
                            // the five column: dateTime
                            if (events[i].dates && events[i].dates.start && events[i].dates.start.localDate) {
                                var localDate = events[i].dates.start.localDate;
                            }
                            else {
                                var localDate = "";
                            }
                            if (events[i].dates && events[i].dates.start && events[i].dates.start.localTime) {
                                var localTime = events[i].dates.start.localTime;
                            }
                            else {
                                var localTime = "";
                            }
                            array_results[i].dateTime = localDate + " " + localTime;
                        }// end for
                    }// end if
                    else {
                        array_results = [];
                    }// end else
                    res.send(JSON.stringify(array_results));
                }); // end second request
            }); // end first request
        } // end else
    } // end if


    else if (typeof input_client.eventId !== 'undefined' && typeof input_client.venueName !== 'undefined') {
        var eventId = input_client.eventId;
        var venueName = input_client.venueName;

        // the first part for event tab
        var eventPromise = new Promise (function func1(resolve) {
            var url_Ticketmaster_API_event = "https://app.ticketmaster.com/discovery/v2/events/" + eventId + ".json?apikey=" + KEY_TICKETMASTER_API;
            //console.log("url_Ticketmaster_API_event: " + url_Ticketmaster_API_event);
            request.get(url_Ticketmaster_API_event, function(apiError, apiResponse, apiBody) {
                var jsonObj_event = JSON.parse(apiBody);
                var event = {};
                // structure of event: {1 artistTeam1: string, 2 artistTeam2: string, 3 artistTeams: string, 4 venue: string, 5 time: string, 6 segment: string, 7 category: string, 8 priceRange: string, 9 ticketStatus: string, 10 buyTicketAt: string, 11 seatMap: string}
                // the first row: artistTeams
                if (jsonObj_event._embedded && jsonObj_event._embedded.attractions) {
                    var attractions = jsonObj_event._embedded.attractions;
                    // !!! artistTeam1 for artistTeams & artists tab
                    if (attractions.length >= 1 && attractions[0].name && attractions[0].name != "undefined" && attractions[0].name != "Undefined" && attractions[0].name != "") {
                        var artistTeam1 = attractions[0].name;
                        event.artistTeam1 = attractions[0].name;
                    }
                    else {
                        var artistTeam1 = "";
                        event.artistTeam1 = "";
                    }
                    // !!! artistTeam2 for artistTeams & artists tab
                    if (attractions.length >= 2 && attractions[1].name && attractions[1].name != "undefined" && attractions[1].name != "Undefined" && attractions[1].name != "") {
                        var artistTeam2 = attractions[1].name;
                        event.artistTeam2 = attractions[1].name;
                    }
                    else {
                        var artistTeam2 = "";
                        event.artistTeam2 = "";
                    }
                    if (artistTeam2 != "") {
                        var artistTeams = artistTeam1 + " | " + artistTeam2;
                    }
                    else {
                        var artistTeams = artistTeam1;
                    }
                    event.artistTeams = artistTeams;
                } // if
                else {
                    event.artistTeam1 = "";
                    event.artistTeam2 = "";
                    event.artistTeams = "";
                }
                // the second row: venue
                if (jsonObj_event._embedded && jsonObj_event._embedded.venues && jsonObj_event._embedded.venues[0].name) {
                    event.venue = jsonObj_event._embedded.venues[0].name;
                }
                else {
                    event.venue = "";
                }
                // the third row: time
                if (jsonObj_event.dates && jsonObj_event.dates.start && jsonObj_event.dates.start.localDate) {
                    var time_localDate = jsonObj_event.dates.start.localDate;
                    time_localDate = moment(time_localDate, "YYYY-MM-DD").format("MMM DD, YYYY");
                }
                else {
                    var time_localDate = "";
                }
                if (jsonObj_event.dates && jsonObj_event.dates.start && jsonObj_event.dates.start.localTime) {
                    var time_localTime = jsonObj_event.dates.start.localTime;
                }
                else {
                    var time_localTime = "";
                }
                if (time_localDate != "" || time_localTime != "") {
                    event.time = time_localDate + " " + time_localTime;
                }
                else {
                    event.time = "";
                }
                // the fourth row: category
                if (jsonObj_event._embedded && jsonObj_event._embedded.attractions && jsonObj_event._embedded.attractions[0].classifications) {
                    var genres = jsonObj_event._embedded.attractions[0].classifications[0];
                    if (genres.genre && genres.genre.name) {
                        var genre = genres.genre.name;
                    }
                    else {
                        var genre = "";
                    }
                    // !!! segment for artists tab
                    if (genres.segment && genres.segment.name) {
                        var segment = genres.segment.name;
                        event.segment = genres.segment.name;
                    }
                    else {
                        var segment = "";
                        event.segment = "";
                    }
                    event.category = "";
                    if (segment != "" && segment != "undefined" && segment != "Undefined") {
                        event.category = segment;
                    }
                    if (genre != "" && genre != "undefined" && genre != "Undefined") {
                        event.category = event.category + " | " + genre;
                    }
                }
                else {
                    event.segment = "";
                    event.category = "";
                }
                // the fifth row: priceRange
                if (jsonObj_event.priceRanges) {
                    if(jsonObj_event.priceRanges[0].min) {
                    	var min = jsonObj_event.priceRanges[0].min;
                    	min = min.toFixed(2);
                        min = "$" + min;
                    }
                    else {
                        var min = "";
                    }
                    if(jsonObj_event.priceRanges[0].max) {
                    	var max = jsonObj_event.priceRanges[0].max;
                    	max = max.toFixed(2);
                        max =  "$" + max;
                    }
                    else {
                        var max = "";
                    }
                    if(max != "" && min != "") {
                        event.priceRange = min + " ~ " + max;
                    }
                    else if(max == "" && min != "") {
                        event.priceRange = min;
                    }
                    else if(max != "" && min == "") {
                        event.priceRange = max;
                    }
                    else {
                        event.priceRange = "";
                    }
                }
                else {
                    event.priceRange = "";
                }
                // the sixth row: ticketStatus
                if (jsonObj_event.dates && jsonObj_event.dates.status && jsonObj_event.dates.status.code) {
                    event.ticketStatus = jsonObj_event.dates.status.code;
                }
                else {
                    event.ticketStatus = "";
                }
                // the seventh row: buyTicketAt
                if (jsonObj_event.url) {
                    event.buyTicketAt = jsonObj_event.url;
                }
                else {
                    event.buyTicketAt = "";
                }
                // the eighth row: seatMap
                if (jsonObj_event.seatmap && jsonObj_event.seatmap.staticUrl) {
                    event.seatMap = jsonObj_event.seatmap.staticUrl;
                }
                else {
                    event.seatMap = "";
                }
            resolve(event);
            }); // end request
        }); // end promise
        

        // the third part for venue tab
        // structure of venue: {1 name: string, 2 address: string, 3 city: string, 4 phoneNumber: string, 5 openHours: string, 6 generalRule: string, 7 childRule: string, 8 latitude: string, 9 longitude: string}
        var venuePromise = new Promise (function func6(resolve) {
            // the first row: name
            var venueInfo = {};
            venueInfo.name = venueName;
            // Ticketmaster API: venue search
            var url_Ticketmaster_API_venue = "https://app.ticketmaster.com/discovery/v2/venues.json?keyword=" + venueName + "&apikey=" + KEY_TICKETMASTER_API2;

            request.get(url_Ticketmaster_API_venue, function(apiError, apiResponse, apiBody) {
                var jsonObj_venue = JSON.parse(apiBody);
                if (jsonObj_venue._embedded && jsonObj_venue._embedded.venues) {
                    var venue = jsonObj_venue._embedded.venues[0];
                    // the second row: address
                    if (venue.address && venue.address.line1) {
                        venueInfo.address  = venue.address.line1;
                    }
                    else {
                        venueInfo.address = "";
                    }
                    // the third row: city
                    if (venue.city && venue.city.name) {
                        venueInfo.city = venue.city.name;
                    }
                    else {
                        venueInfo.city = "";
                    }
                    // the fourth row: phoneNumber
                    if (venue.boxOfficeInfo && venue.boxOfficeInfo.phoneNumberDetail && (venue.boxOfficeInfo.phoneNumberDetail != "")) {
                        venueInfo.phoneNumber = venue.boxOfficeInfo.phoneNumberDetail;
                    }
                    else {
                        venueInfo.phoneNumber = "";
                    }
                    // the fifth row: openHours
                    if (venue.boxOfficeInfo && venue.boxOfficeInfo.openHoursDetail) {
                        venueInfo.openHours = venue.boxOfficeInfo.openHoursDetail;
                    }
                    else {
                        venueInfo.openHours = "";
                    }
                    // the sixth row: generalRule
                    if (venue.generalInfo && venue.generalInfo.generalRule) {
                        venueInfo.generalRule = venue.generalInfo.generalRule;
                    }
                    else {
                        venueInfo.generalRule = "";
                    }
                    // the seventh row: childRule
                    if (venue.generalInfo && venue.generalInfo.childRule) {
                        venueInfo.childRule = venue.generalInfo.childRule;
                    }
                    else {
                        venueInfo.childRule = "";
                    }
                    // the eighth row: google map
                    if (venue.location && venue.location.latitude && venue.location.longitude) {
                        venueInfo.latitude = venue.location.latitude;
                        venueInfo.longitude = venue.location.longitude;
                    }
                    else {
                        venueInfo.latitude = "";
                        venueInfo.longitude = "";
                    }
                } // end if
                else {
                    venueInfo.address = "";
                    venueInfo.city = "";
                    venueInfo.phoneNumber = "";
                    venueInfo.openHours = "";
                    venueInfo.generalRule = "";
                    venueInfo.childRule = "";
                    venueInfo.latitude = "";
                    venueInfo.longitude = "";
                }
                resolve(venueInfo);
            }); // end request
        }); // end promise

        
        // the fourth part for upcoming tab
        var upcomingPromise = new Promise (function func6(resolve) {
            var upcomingEvents = [];
            // songkick search: for id
            var url_Songkick_API_ID = "https://api.songkick.com/api/3.0/search/venues.json?query=" + venueName + "&apikey=" + KEY_SONGKICK_API;
            request.get(url_Songkick_API_ID, function(apiError, apiResponse, apiBody) {
                var json_Songkick_API_ID = JSON.parse(apiBody);
                if (json_Songkick_API_ID.resultsPage && json_Songkick_API_ID.resultsPage.results && json_Songkick_API_ID.resultsPage.results.venue && json_Songkick_API_ID.resultsPage.results.venue[0].id) {
                    var venueId = json_Songkick_API_ID.resultsPage.results.venue[0].id;
                    // https://api.songkick.com/api/3.0/venues/598/calendar.json?apikey=vVnlPq96znyWSzU6
                    var url_Songkick_API_events = "https://api.songkick.com/api/3.0/venues/" + venueId + "/calendar.json?apikey=" + KEY_SONGKICK_API;
                    request.get(url_Songkick_API_events, function(apiError, apiResponse, apiBody) {
                        var jsonObj_upcomingEvents = JSON.parse(apiBody);
                        // structure of upcoming:
                        // [{1 displayName: string, 2 url: link, 3 artist: string, 4 dateTime: string, 5 timeStamp: string, 6 type: string}]
                        if (jsonObj_upcomingEvents.resultsPage && jsonObj_upcomingEvents.resultsPage.results && jsonObj_upcomingEvents.resultsPage.results.event) {
                            var events = jsonObj_upcomingEvents.resultsPage.results.event;
                            if (events.length >= 5) {
                                var arrayLength = 5;
                            }
                            else {
                                var arrayLength = events.length;
                            }
                            for (var i = 0; i < arrayLength; i++) {
                                var upcomingEvent = {};
                                // the first row: displayName
                                if (events[i].displayName) {
                                    upcomingEvent.displayName = events[i].displayName;
                                }
                                if (events[i].uri) {
                                    upcomingEvent.uri = events[i].uri;
                                }
                                // the second row: artist
                                if (events[i].performance[0] && events[i].performance[0].displayName) {
                                    upcomingEvent.artist = events[i].performance[0].displayName;
                                }
                                // the third row: dateTime
                                if (events[i].start && events[i].start.date) {
                                    var date = events[i].start.date;
                                    var date_moment = moment(date, "YYYY-MM-DD").format("MMM DD, YYYY");
                                }
                                else {
                                    date = "";
                                }
                                if (events[i].start && events[i].start.time) {
                                    var time = events[i].start.time;
                                    var time_sorted = events[i].start.time;
                                }
                                else {
                                    time = "";
                                    time_sorted = "00:00:00";
                                }
                                if (date_moment != "") {
                                    upcomingEvent.dateTime = date_moment + " " + time;
                                    var dateTimeSorted = date + " " + time_sorted;
                                    dateTimeSorted = dateTimeSorted.replace(/-/g, '/');
                                    upcomingEvent.timeStamp = new Date(dateTimeSorted).getTime();
                                }
                                // the fourth row: type
                                if (events[i].type) {
                                    upcomingEvent.type = events[i].type;
                                }
                                upcomingEvents.push(upcomingEvent);
                            } // end for
                        } // end if
                        resolve(upcomingEvents);
                    }); // end request
                }
                else {
                    resolve(upcomingEvents);
                }
            }); // end function(3)
        }); // end promise
        
        var promise = Promise.all([eventPromise, venuePromise, upcomingPromise]);
        promise.then(function(value){
            console.log("test11: " + value);
            console.log("test12: " + JSON.stringify(value[0]));
            console.log("test13: " + JSON.stringify(value[1]));
            console.log("test14: " + JSON.stringify(value[2]));
            var details = {};
            details.eventDetail = value[0];
            details.venue = value[1];
            details.upcoming = value[2];
            res.send(details);
        });
        
    } // end else if


    else if (typeof input_client.segment !== 'undefined') {
        var artistTeam1 =  input_client.artistTeam1;
        var artistTeam2 =  input_client.artistTeam2;
        var segment =  input_client.segment;
        var music1 = {};
        var music2 = {};
        var photos1 = {};
        var photos2 = {};
        /*
        the second part for artists tab
        artists structure: { 1 music1: {}, 2 photos1: {}, 3 music2: {}, 4 photos2: {} }
        music1 music2 structure: 
        { 1 follower: string, 2 popularity: string, 3 checkAt: string }
        photos1 photos2 structure: 
        { photo1: url, photo2: url, photo3: url, photo4: url, photo5: url, photo6: url, photo7: url, photo8: url }
        */
        // artists tab: artist1: part1: request music
        var artistTeam1MusicPromise = new Promise (function func3(resolve) {
            if (segment == "Music" && artistTeam1 != "") {
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
                    spotifyApi.searchArtists(artistTeam1).then(function(data) {
                        //console.log("test3: spotify: " + JSON.stringify(data.body));
                        //res.send(data.body);
                        var artist1_music_data = data.body;
                        if (artist1_music_data.artists && artist1_music_data.artists.items && artist1_music_data.artists.items[0]) {
                            var artist1_music = artist1_music_data.artists.items[0];
                            // the second row: followers, music1.followers
                            if (artist1_music.followers && artist1_music.followers.total) {
                                var artist1_str = "" + artist1_music.followers.total;
                                if (artist1_str.length < 4) {
                                    music1.followers = artist1_str;
                                }
                                else if (artist1_str.length >= 4 && artist1_str.length <=6) {
                                    music1.followers = artist1_str.substr(0, artist1_str.length - 3) + "," + artist1_str.substr(-3, 3);
                                }
                                else if (artist1_str.length >= 7 && artist1_str.length <=9) {
                                    music1.followers = artist1_str.substr(0, artist1_str.length - 6) + "," + artist1_str.substr(-6, 3) + "," + artist1_str.substr(-3, 3);
                                }
                                else {
                                    music1.followers = artist1_str.substr(0, str.length - 9) + "," + artist1_str.substr(-9, 3) + "," + artist1_str.substr(-6, 3) + "," + artist1_str.substr(-3, 3);
                                }
                            }
                            else {
                                music1.followers = "";
                            }
                            // the third row: popularity, music1.popularity
                            if (artist1_music.popularity) {
                                music1.popularity = artist1_music.popularity;
                            }
                            else {
                                music1.popularity = "";
                            }
                            // the fourth row: checkAt, music1.checkAt
                            if (artist1_music.external_urls && artist1_music.external_urls.spotify) {
                                music1.checkAt = artist1_music.external_urls.spotify;
                            }
                            else {
                                music1.checkAt = "";
                            }
                            resolve(music1);
                        } // end if has artist1_music
                        else {
                            resolve(music1);
                        }
                    });
                },function(err) {
                    console.log('Something went wrong when retrieving an access token', err.message);
                });
            }
            else {                   
                resolve(music1);
            }
        });

        // artists tab: artist1: part2: request photos
        var artistTeam1PhotosPromise = new Promise (function func3(resolve) {
            if (artistTeam1 != "") {
                var url_Google_API_artistTeam1 = "https://www.googleapis.com/customsearch/v1?q=" + artistTeam1 + "&cx=" + SEARCH_ENGINE_ID + "&imgSize=small&num=8&searchType=image&key=" + KEY_GOOGLE_API;
                //console.log("url_Google_API_artistTeam1: " + url_Google_API_artistTeam1);
                request.get(url_Google_API_artistTeam1, function(apiError, apiResponse, apiBody) {
                    var jsonObj_photos1 = JSON.parse(apiBody);
                    // photo1
                    if (jsonObj_photos1.items && jsonObj_photos1.items[0] && jsonObj_photos1.items[0].link) {
                        photos1.photo1 = jsonObj_photos1.items[0].link;
                    }
                    else {
                        photos1.photo1 = "";
                    }
                    // photo2
                    if (jsonObj_photos1.items && jsonObj_photos1.items[1] && jsonObj_photos1.items[1].link) {
                        photos1.photo2 = jsonObj_photos1.items[1].link;
                    }
                    else {
                        photos1.photo2 = "";
                    }
                    // photo3
                    if (jsonObj_photos1.items && jsonObj_photos1.items[2] && jsonObj_photos1.items[2].link) {
                        photos1.photo3 = jsonObj_photos1.items[2].link;
                    }
                    else {
                        photos1.photo3 = "";
                    }
                    // photo4
                    if (jsonObj_photos1.items && jsonObj_photos1.items[3] && jsonObj_photos1.items[3].link) {
                        photos1.photo4 = jsonObj_photos1.items[3].link;
                    }
                    else {
                        photos1.photo4 = "";
                    }
                    // photo5
                    if (jsonObj_photos1.items && jsonObj_photos1.items[4] && jsonObj_photos1.items[4].link) {
                        photos1.photo5 = jsonObj_photos1.items[4].link;
                    }
                    else {
                        photos1.photo5 = "";
                    }
                    // photo6
                    if (jsonObj_photos1.items && jsonObj_photos1.items[5] && jsonObj_photos1.items[5].link) {
                        photos1.photo6 = jsonObj_photos1.items[5].link;
                    }
                    else {
                        photos1.photo6 = "";
                    }
                    // photo7
                    if (jsonObj_photos1.items && jsonObj_photos1.items[6] && jsonObj_photos1.items[6].link) {
                        photos1.photo7 = jsonObj_photos1.items[6].link;
                    }
                    else {
                        photos1.photo7 = "";
                    }
                    // photo8
                    if (jsonObj_photos1.items && jsonObj_photos1.items[7] && jsonObj_photos1.items[7].link) {
                        photos1.photo8 = jsonObj_photos1.items[7].link;
                    }
                    else {
                        photos1.photo8 = "";
                    }
                    resolve(photos1);
                }); // end request
            } // end if
            else {
                resolve(photos1);
            }
        });

        // artists tab: artist2: part1: request music
        var artistTeam2MusicPromise = new Promise (function func4(resolve) {
            if (segment == "Music" && artistTeam2 != "" ) {
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
                    spotifyApi.searchArtists(artistTeam2).then(function(data) {
                        var artist2_music_data = data.body;
                        if (artist2_music_data.artists && artist2_music_data.artists.items && artist2_music_data.artists.items[0]) {
                            var artist2_music = artist2_music_data.artists.items[0];
                            // the second row: followers, music2.followers
                            if (artist2_music.followers && artist2_music.followers.total) {
                                var artist2_str = "" + artist2_music.followers.total;
                                if (artist2_str.length < 4) {
                                    music2.followers = artist2_str;
                                }
                                else if (artist2_str.length >= 4 && artist2_str.length <=6) {
                                    music2.followers = artist2_str.substr(0, artist2_str.length - 3) + "," + artist2_str.substr(-3, 3);
                                }
                                else if (artist2_str.length >= 7 && artist2_str.length <=9) {
                                    music2.followers = artist2_str.substr(0, artist2_str.length - 6) + "," + artist2_str.substr(-6, 3) + "," + artist2_str.substr(-3, 3);
                                }
                                else {
                                    music2.followers = artist2_str.substr(0, str.length - 9) + "," + artist2_str.substr(-9, 3) + "," + artist2_str.substr(-6, 3) + "," + artist2_str.substr(-3, 3);
                                }
                            }
                            else {
                                music2.followers = "";
                            }
                            // the third row: popularity, music2.popularity
                            if (artist2_music.popularity) {
                                music2.popularity = artist2_music.popularity;
                            }
                            else {
                                music2.popularity = "";
                            }
                            // the fourth row: checkAt, music2.checkAt
                            if (artist2_music.external_urls && artist2_music.external_urls.spotify) {
                                music2.checkAt = artist2_music.external_urls.spotify;
                            }
                            else {
                                music2.checkAt = "";
                            }
                            resolve(music2);
                        } // end if has artist2_music
                        else {
                            resolve(music2);
                        }
                    });
                },function(err) {
                    console.log('Something went wrong when retrieving an access token', err.message);
                });
            } // end if 
            else {                   
                resolve(music2);
            }
        });

        // artists tab: artist2: part2: request photos
        var artistTeam2PhotosPromise = new Promise (function func5(resolve) {
            if (artistTeam2 != "") {
                var url_Google_API_artistTeam2 = "https://www.googleapis.com/customsearch/v1?q=" + artistTeam2 + "&cx=" + SEARCH_ENGINE_ID + "&imgSize=small&num=8&searchType=image&key=" + KEY_GOOGLE_API;
                //console.log("url_Google_API_artistTeam2: " + url_Google_API_artistTeam2);
                request.get(url_Google_API_artistTeam2, function(apiError, apiResponse, apiBody) {
                    var jsonObj_photos2 = JSON.parse(apiBody);
                    // photo1
                    if (jsonObj_photos2.items && jsonObj_photos2.items[0] && jsonObj_photos2.items[0].link) {
                        photos2.photo1 = jsonObj_photos2.items[0].link;
                    }
                    else {
                        photos2.photo1 = "";
                    }
                    // photo2
                    if (jsonObj_photos2.items && jsonObj_photos2.items[1] && jsonObj_photos2.items[1].link) {
                        photos2.photo2 = jsonObj_photos2.items[1].link;
                    }
                    else {
                        photos2.photo2 = "";
                    }
                    // photo3
                    if (jsonObj_photos2.items && jsonObj_photos2.items[2] && jsonObj_photos2.items[2].link) {
                        photos2.photo3 = jsonObj_photos2.items[2].link;
                    }
                    else {
                        photos2.photo3 = "";
                    }
                    // photo4
                    if (jsonObj_photos2.items && jsonObj_photos2.items[3] && jsonObj_photos2.items[3].link) {
                        photos2.photo4 = jsonObj_photos2.items[3].link;
                    }
                    else {
                        photos2.photo4 = "";
                    }
                    // photo5
                    if (jsonObj_photos2.items && jsonObj_photos2.items[4] && jsonObj_photos2.items[4].link) {
                        photos2.photo5 = jsonObj_photos2.items[4].link;
                    }
                    else {
                        photos2.photo5 = "";
                    }
                    // photo6
                    if (jsonObj_photos2.items && jsonObj_photos2.items[5] && jsonObj_photos2.items[5].link) {
                        photos2.photo6 = jsonObj_photos2.items[5].link;
                    }
                    else {
                        photos2.photo6 = "";
                    }
                    // photo7
                    if (jsonObj_photos2.items && jsonObj_photos2.items[6] && jsonObj_photos2.items[6].link) {
                        photos2.photo7 = jsonObj_photos2.items[6].link;
                    }
                    else {
                        photos2.photo7 = "";
                    }
                    // photo8
                    if (jsonObj_photos2.items && jsonObj_photos2.items[7] && jsonObj_photos2.items[7].link) {
                        photos2.photo8 = jsonObj_photos2.items[7].link;
                    }
                    else {
                        photos2.photo8 = "";
                    }
                    resolve(photos2);
                }); // end request
            } // end if
            else {
                resolve(photos2);
            }
        });

        artistsPromise = Promise.all([artistTeam1MusicPromise, artistTeam1PhotosPromise, artistTeam2MusicPromise, artistTeam2PhotosPromise]);
        artistsPromise.then(function(value){
            console.log("artistsPromise: " + value);
            console.log("artistsPromise: " + JSON.stringify(value[0]));
            console.log("artistsPromise: " + JSON.stringify(value[1]));
            console.log("artistsPromise: " + JSON.stringify(value[2]));
            console.log("artistsPromise: " + JSON.stringify(value[3]));
            var artists = {};
            artists.music1 = value[0];
            artists.photos1 = value[1];
            artists.music2 = value[2];
            artists.photos2 = value[3];
            res.send(artists);
        });
    }


    // autocomplete
    else if (typeof input_client.searchText !== 'undefined') {
        var searchText =  input_client.searchText;
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
            var autocomplete = "";
            if (array_searchResults.length >0) {
                autocomplete = array_searchResults.join(",")
            }
            res.send(autocomplete);
        });
    }
    
});
server.listen(8081, function(){
    console.log("Start successfully，please visit http://127.0.0.1:8081/public/index.html");
});
