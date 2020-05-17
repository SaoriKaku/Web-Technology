
(function(angular) {

    var searchApp = angular.module('searchMvc', ['ngAnimate', 'angular-svg-round-progressbar']);

    searchApp.controller('searchController', ['$scope', '$http', '$q', 'orderByFilter', function($scope, $http, $q, orderBy) {
        if (screen.width > 1125) {
            $scope.artistTeams = "Artist/Teams";
            $scope.upcomingEvents = "UpcomingEvents";
        }
        else {
            $scope.artistTeams = "Artist";
            $scope.upcomingEvents = "Upcoming";
        }
        // form info
        $scope.input_keyword;
        $scope.input_category = 'all';
        $scope.input_distance;
        $scope.input_unit = 'miles';
        $scope.input_option;
        $scope.input_location;
        // click button
        $scope.click_search = false;
        $scope.click_results = true;
        $scope.click_favorites = false;
        $scope.results_details_disabled = true;
        $scope.favorites_details_disabled = true;
        // show animation
        $scope.show_slide = false;
        $scope.show_progress = false;
        // show results
        $scope.no_response_search = false;
        $scope.no_records_result = false;
        $scope.show_results = false;
        $scope.show_favorites = false;
        $scope.show_details = false;
        // show event
        $scope.no_response_event = false;
        // history location
        $scope.array_history = [];

        $scope.checkOption = function() {
            if (document.getElementById("input_option1").checked) {
                document.getElementById("input_location").disabled = true;
                $scope.input_location = "";
            }
            
            if (document.getElementById("input_option2").checked) {
                document.getElementById("input_location").disabled = false;
            }
        };

        $scope.checkSearch = function() {
            if ($scope.input_option == 1) {
                $scope.form.location.$setPristine();
                $scope.form.location.$setUntouched();
                if ($scope.form.keyword.$invalid) {
                    return true;
                }
            }
            else {
                if ($scope.form.keyword.$invalid || $scope.form.location.$invalid) {
                    return true;
                }
            }
        };

        $scope.clearAll = function() {
            // form info
            $scope.form.$setPristine();
            $scope.form.$setUntouched();
            $scope.form.$submitted = false;
            $scope.input_keyword = '';
            $scope.input_category = 'all';
            $scope.input_distance = '10';
            $scope.input_unit = 'miles';
            $scope.input_option = 1;
            $scope.input_location = '';
            // click button
            $scope.click_results = true;
            $scope.click_favorites = false;
            // show div
            $scope.show_slide = false;
            $scope.show_results = false;
            $scope.show_favorites = false;
            $scope.show_details = false;
            $scope.show_progress = false;
            $scope.no_response_search = false;
            $scope.no_records_result = false;
            // existed data
            $scope.array_results = [];
            $scope.array_history = [];
            // disable Details button
            $scope.results_details_disabled = true;
            $scope.favorites_details_disabled = true;
            document.getElementById("input_location").disabled = true;
        };

        // when click button Results
        $scope.clickResults = function() {
            $scope.click_favorites = false;
            $scope.click_results = true;
            $scope.show_results = true;
            $scope.show_favorites = false;
            $scope.show_details = false;
            if ($scope.click_search == false) {
                $scope.show_results = false;
                alert("Please click button Search before clicking button Resluts.");
            }
            // history location
            $scope.array_history.push("results");
        };

        $scope.clickDetails = function() {
            $scope.show_slide = true;
            $scope.slideToRight = true;
            $scope.show_results = false;
            $scope.show_favorites = false;
            $scope.show_details = true;
            // history location
            $scope.array_history.push("details");
        };

        $scope.deleteAnimation = function() {
            $scope.show_slide = false;
        };

        $scope.sendForm = function() {
            $scope.click_search = true;
            $scope.click_results = true;
            $scope.click_favorites = false;
            // show div
            $scope.no_response_search = false;
            $scope.no_records_result = false;
            $scope.show_results = false;
            $scope.show_slide = false;
            $scope.show_progress = true;

            var defer = $q.defer();

            if ($scope.input_distance == "" || typeof ($scope.input_distance) == 'undefined') {
                $scope.input_distance = 10;
            }
            if ($scope.input_option == 1) {
                $http ({method: 'GET', url: 'http://ip-api.com/json'})
                .then (function successCallback1 (response) {
                    $scope.latitude_current = parseFloat(response.data.lat);
                    $scope.longitude_current = parseFloat(response.data.lon);
                    // alert($scope.latitude_current);
                    // alert($scope.longitude_current);
                    // var url_option1 = "http://csci571-hw7-nodejs.us-east-2.elasticbeanstalk.com/?";
                    var url_option1 = "/?";
                    url_option1 =  url_option1 + "keyword=" + $scope.input_keyword + "&category=" + $scope.input_category + "&distance=" + $scope.input_distance + "&unit=" + $scope.input_unit + "&latitude=" + $scope.latitude_current + "&longitude=" + $scope.longitude_current;
                    // alert(url_option1);

                    $http({method: 'GET', url: url_option1})
                    .then (function successCallback2 (response) {
                        $scope.jsonObj_results = response.data;
                        // alert(JSON.stringify($scope.jsonObj_results));
                        defer.resolve(response.data);  
                        $scope.show_progress = false;
                        // alert( $scope.test);
                    }, function errorCallback2 (response) {
                        alert("Failed to request nodejs using " + url_option1 + "!");
                        $scope.show_progress = false;
                        $scope.no_response_search = true;
                    });
                }, function errorCallback1 (response) {
                    console.log("Failed to request 'http://ip-api.com/json'!");
                    $scope.show_progress = false;
                    $scope.no_response_search = true;
                });
            } // end if
            else {
                // var url_option2 = "http://csci571-hw7-nodejs.us-east-2.elasticbeanstalk.com/?";
                var url_option2 = "/?";
                url_option2 =  url_option2 + "keyword=" + $scope.input_keyword + "&category=" + $scope.input_category + "&distance=" + $scope.input_distance + "&unit=" + $scope.input_unit + "&location=" + $scope.input_location;
                // alert(url_option2);

                $http ({method: 'GET', url: url_option2})
                .then (function successCallback3 (response) {
                    $scope.jsonObj_results = response.data;
                    // alert(JSON.stringify($scope.jsonObj_results));
                    $scope.show_progress = false;
                    defer.resolve(response.data);  
                    // $rootScope.slideAnimation = true;
                    // $location.path(myPath);
                }, function errorCallback3 (response) {
                    alert("Failed to request nodejs using " + url_option2 + "!");
                    $scope.show_progress = false;
                    $scope.no_response_search = true;
                });
            }
            return defer.promise;
        };

        $scope.generateResultsTable = function () {
            var promise = $scope.sendForm();
            promise.then(function() { 
                if (!$scope.no_response_search) {
                    // array_results to store needed data from jsonObj_results
                    if ($scope.jsonObj_results._embedded && $scope.jsonObj_results._embedded.events) {
                        $scope.events = $scope.jsonObj_results._embedded.events;
                        // structure of $scope.array_results
                        // [{1 eventId: int, 2 date: format, 3 event: string, 4 event_short: string, 5 category: string, 6 venue_info: string, 7 highlight: bool; 8 show_starBorder: bool}, ...]
                        $scope.array_results = [];
                        for (var i = 0; i < $scope.events.length; i++) {
                            $scope.array_results[i] = {};
                            // get the event ID
                            $scope.array_results[i].eventId = $scope.events[i].id;
                            // highlight: true if its event/event_short is clicked, otherwise false
                            $scope.array_results[i].highlight = false;
                            // show_starBorder: true when the row is not in the favorites table
                            $scope.array_results[i].show_starBorder = true;
                            // the first column: date
                            if ($scope.events[i].dates && $scope.events[i].dates.start && $scope.events[i].dates.start.localDate) {
                                $scope.array_results[i].date = $scope.events[i].dates.start.localDate;
                            }
                            else {
                                $scope.array_results[i].date = "";
                            }
                            // the second column: event
                            if ($scope.events[i].name) {
                                $scope.array_results[i].event = $scope.events[i].name;
                                if ($scope.array_results[i].event.length > 35) {
                                    var event_short = $scope.array_results[i].event.substr(0, 35);
                                    var space = event_short.lastIndexOf(" ");
                                    event_short = event_short.substr(0, space-1) + "...";
                                    $scope.array_results[i].event_short = event_short;
                                }
                                else {
                                    $scope.array_results[i].event_short = $scope.array_results[i].event;
                                }
                            }
                            else {
                                $scope.array_results[i].event = "";
                            }
                            // the third column: category
                            if ($scope.events[i].classifications && $scope.events[i].classifications[0].genre && $scope.events[i].classifications[0].genre.name) {
                                $scope.array_results[i].genre = $scope.events[i].classifications[0].genre.name;
                            }
                            else {
                                $scope.array_results[i].genre = "";
                            }
                            if ($scope.events[i].classifications && $scope.events[i].classifications[0].segment && $scope.events[i].classifications[0].segment.name) {
                                $scope.array_results[i].segment = $scope.events[i].classifications[0].segment.name;
                            }
                            else {
                                $scope.array_results[i].segment = "";
                            }
                            if ($scope.array_results[i].genre != "" && $scope.array_results[i].segment != "") {
                                $scope.array_results[i].category = $scope.array_results[i].genre + "-" + $scope.array_results[i].segment;
                            }
                            else {
                                $scope.array_results[i].category = $scope.array_results[i].genre + $scope.array_results[i].segment;
                            }
                            // the fourth column: venue info
                            if ($scope.events[i]._embedded && $scope.events[i]._embedded.venues && $scope.events[i]._embedded.venues[0].name) {
                                $scope.array_results[i].venue_info = $scope.events[i]._embedded.venues[0].name;
                            }
                            else {
                                $scope.array_results[i].venue_info = "";
                            } //end else
                        }// end for
                    }// end if
                    else {
                    $scope.no_records_result = true;
                    $scope.show_results = false;
                    } // end else
                } // end if
                // show_starBorder: true when the row is not in the favorites table
                $scope.myStorage1 = window.localStorage;
                for (var j = 0; j < $scope.myStorage1.length; j++) {
                    var key = $scope.myStorage1.key(j);
                    for (var k = 0; k < $scope.array_results.length; k++) {
                        if ($scope.array_results[k].eventId == key) {
                            // alert($scope.array_results[k].eventId);
                            // alert(key);
                            $scope.array_results[k].show_starBorder = false;
                        }
                    } // end for
                } // end for
            }); // end then
            $scope.show_favorites = false;
            $scope.show_details = false;
            $scope.show_results = true;
        }; // end function
        
        $scope.sendEventTab = function (param_eventId) {
            $scope.show_progress = true;
            $scope.no_records_eventTab = false;
            var defer = $q.defer();
            // var eventId = $scope.array_results[$index].eventId;
            var eventId = param_eventId;
            var url_event = "/?";
            url_event =  url_event + "eventId=" + eventId;
            // alert(url_event);

            $http ({method: 'GET', url: url_event})
            .then (function successCallback (response) {
                $scope.jsonObj_event = response.data;
                // alert(JSON.stringify($scope.jsonObj_event));
                $scope.show_progress = false;
                defer.resolve(response.data);  
            }, function errorCallback (response) {
                alert("Failed to request nodejs using " + url_event + "!");
                $scope.show_progress = false;
                $scope.no_records_eventTab = true;
            });
            return defer.promise;
        };

        $scope.generateEventContent = function (param_eventId, param_eventName) {
            // $scope.event_name = $scope.array_results[$index].event;
            $scope.event_name = param_eventName;
            var promise = $scope.sendEventTab(param_eventId);
            $scope.array_artistTeam_name = [];
            // the structure of $scope.array_artistTeams:
            // [{1 name: string, 2 followers: int, 3 popularity: int, 4 checkAt: value4, 5 show_artist_musicRelated: bool, 6 show_artistTeam_photos: bool, 7 photos: array}, ...]
            $scope.array_artistTeams = [];
            promise.then(function() {
                $scope.show_event_artistTeams = true;
                $scope.show_event_venue = true;
                $scope.show_event_time = true;
                $scope.show_event_category = true;
                $scope.show_event_priceRange = true;
                $scope.show_event_ticketStatus = true;
                $scope.show_event_buyTicketAt = true;
                $scope.show_event_seatMap = true;
                var jsonObj_event = $scope.jsonObj_event;

                if (!$scope.no_response_event) {
                    // the first row: Artist/Teams
                    if (jsonObj_event._embedded && jsonObj_event._embedded.attractions) {
                        var attractions = jsonObj_event._embedded.attractions;
                        // $scope.array_artistTeam_name = [];
                        // $scope.array_artistTeams = [];
                        for (var i = 0; i < attractions.length; i++) {
                            if (attractions[i].name && attractions[i].name != "undefined" && attractions[i].name != "Undefined" && attractions[i].name != "") {
                                // for tab event
                                var artistTeam_name = attractions[i].name;
                                // alert(artistTeam_name);
                                $scope.array_artistTeam_name.push(artistTeam_name);
                                // for tab Artist/Team(s)
                                var artistTeam = {};
                                artistTeam.name = attractions[i].name;
                                $scope.array_artistTeams.push(artistTeam);
                            }
                        } // end for
                        // alert($scope.array_artistTeam_name);
                        // alert(JSON.stringify($scope.array_artistTeams));
                        $scope.event_artistTeams = $scope.array_artistTeam_name.join(" | ");
                        if ($scope.array_artistTeam_name.length == 0) {
                            $scope.show_event_artistTeams = false;
                        }
                    } // if
                    else {
                        $scope.show_event_artistTeams = false;
                    }
                    // the second row: Venue
                    if (jsonObj_event._embedded && jsonObj_event._embedded.venues && jsonObj_event._embedded.venues[0].name) {
                        $scope.event_venue = jsonObj_event._embedded.venues[0].name;
                    }
                    else {
                        $scope.show_event_venue = false;
                    }
                    // the third row: Time
                    if (jsonObj_event.dates && jsonObj_event.dates.start && jsonObj_event.dates.start.localDate) {
                        var time_localDate = jsonObj_event.dates.start.localDate;
                        time_localDate = moment(time_localDate, "YYYY-MM-DD").format("MMM DD, YYYY");
                    }
                    else {
                        time_localDate = "";
                    }
                    if (jsonObj_event.dates && jsonObj_event.dates.start && jsonObj_event.dates.start.localTime) {
                        var time_localTime = jsonObj_event.dates.start.localTime;
                    }
                    else {
                        time_localTime = "";
                    }
                    if (time_localDate != "" || time_localTime != "") {
                        $scope.event_time = time_localDate + " " + time_localTime;
                    }
                    else {
                        $scope.show_event_time = false;
                    }
                    // the fourth row: Genres
                    if (jsonObj_event._embedded && jsonObj_event._embedded.attractions && jsonObj_event._embedded.attractions[0].classifications) {
                        var genres = jsonObj_event._embedded.attractions[0].classifications[0];
                        if (genres.genre && genres.genre.name) {
                            var genre = genres.genre.name;
                        }
                        if (genres.segment && genres.segment.name) {
                            var segment = genres.segment.name;
                            $scope.event_segment = genres.segment.name;
                        }
                        if (segment != "" && segment != "undefined" && segment != "Undefined") {
                            $scope.event_category = segment;
                        }
                        if (genre != "" && genre != "undefined" && genre != "Undefined") {
                            $scope.event_category = $scope.event_category + " | " + genre;
                        }
                        if ($scope.event_category == "") {
                            $scope.show_event_category = false;
                        }
                    }
                    else {
                        $scope.show_event_category = false;
                    }
                    // the fifth row: Price Ranges
                    if (jsonObj_event.priceRanges) {
                        if(jsonObj_event.priceRanges[0].min) {
                            var min = "$" + jsonObj_event.priceRanges[0].min;
                        }
                        else {
                            min = "";
                        }
                        if(jsonObj_event.priceRanges[0].max) {
                            var max =  "$" + jsonObj_event.priceRanges[0].max;
                        }
                        else {
                            max = "";
                        }
                        if(max != "" && min != "") {
                            $scope.event_priceRange = min + " - " + max;
                        }
                        else if(max == "" && min != "") {
                            $scope.event_priceRange = min;
                        }
                        else if(max != "" && min == "") {
                            $scope.event_priceRange = max;
                        }
                        else {
                            $scope.show_event_priceRange = false;
                        }
                    }
                    else {
                        $scope.show_event_priceRange = false;
                    }
                    // the sixth row: Ticket Status
                    if (jsonObj_event.dates && jsonObj_event.dates.status && jsonObj_event.dates.status.code) {
                        $scope.event_ticketStatus = jsonObj_event.dates.status.code;
                    }
                    else {
                        $scope.show_event_ticketStatus = false;
                    }
                    // the seventh row: Buy Ticket At
                    if (jsonObj_event.url) {
                         $scope.event_buyTicketAt = jsonObj_event.url;
                    }
                    else {
                        $scope.show_event_buyTicketAt = false;
                    }
                    // the eighth row: seat map, <img src='' />;
                    if (jsonObj_event.seatmap && jsonObj_event.seatmap.staticUrl) {
                        $scope.event_seatMap = jsonObj_event.seatmap.staticUrl;
                    }
                    else {
                        $scope.show_event_seatMap = false;
                    }
                } // end if (!$scope.no_response_event)
                else {
                    $scope.no_records_eventTab = true;
                }
                // Artist/Team(s)Tab&Content
                $scope.show_progress = true;
                $scope.show_artistTeams_photos = true;
                $scope.show_artists_musicRelated = true;
                $scope.no_records_artistTeamsTab = false;
                // alert($scope.array_artistTeam_name);
                var array_promise_photos = [];
                var array_promise_music = [];
                for (var j = 0; j < $scope.array_artistTeam_name.length; j++) {
                    var artistTeams_name = $scope.array_artistTeam_name[j];
                    var promise_photos = $scope.sendArtistTeamPhotoTab(artistTeams_name);
                    array_promise_photos.push(promise_photos);
                    // music part
                    if ($scope.event_segment == "Music") {
                        var promise_music = $scope.sendArtistTeamMusicTab(artistTeams_name);
                        array_promise_music.push(promise_music);
                    } // music part end
                }
                $q.all(array_promise_photos)
                .then (function (response) {
                    $scope.show_progress = false;
                    var array_artistTeams_photes = response;
                    if (array_artistTeams_photes.length == 0) {
                        $scope.show_artistTeams_photos = false;
                    }
                    else {
                        for (var k = 0; k < array_artistTeams_photes.length; k++) {
                            var artistTeam_photes = $scope.generateArtistTeamPhotos(array_artistTeams_photes[k]);
                            if (artistTeam_photes.length == 0) {
                                $scope.array_artistTeams[k].show_artistTeam_photos = false;
                            }
                            else {
                                $scope.array_artistTeams[k].photos = artistTeam_photes;
                                $scope.array_artistTeams[k].show_artistTeam_photos = true;
                            }
                        } // end for
                    } // end else
                }); // end promise all
                // music part
                if ($scope.event_segment == "Music") {
                    $q.all(array_promise_music)
                    .then (function (response) {
                        $scope.show_progress = false;
                        $scope.show_artists_musicRelated = true;
                        var array_artistTeams_music = response;
                        if (array_artistTeams_music.length == 0) {
                            $scope.show_artists_musicRelated = false;
                        }
                        else {
                            for (var l = 0; l < array_artistTeams_music.length; l++) {
                                // alert(JSON.stringify(array_artistTeams_music.length));
                                if (array_artistTeams_music[l].artists && array_artistTeams_music[l].artists.items && array_artistTeams_music[l].artists.items[0]) {
                                    var artist_music = array_artistTeams_music[l].artists.items[0];
                                    $scope.array_artistTeams[l].show_artist_musicRelated = true;
                                    // the first row: name
                                    if (artist_music.name) {
                                        $scope.array_artistTeams[l].name = artist_music.name;
                                    }
                                    // the second row: followers $scope.array_artistTeams[l].followers
                                    if (artist_music.followers && artist_music.followers.total) {
                                        var str = "" + artist_music.followers.total;
                                        // alert(str.length);
                                        if (str.length < 4) {
                                            $scope.array_artistTeams[l].followers = str;
                                        }
                                        else if (str.length >= 4 && str.length <=6) {
                                            $scope.array_artistTeams[l].followers = str.substr(0, str.length - 3) + "," + str.substr(-3, 3);
                                        }
                                        else if (str.length >= 7 && str.length <=9) {
                                            $scope.array_artistTeams[l].followers = str.substr(0, str.length - 6) + "," + str.substr(-6, 3) + "," + str.substr(-3, 3);
                                        }
                                        else {
                                            $scope.array_artistTeams[l].followers = str.substr(0, str.length - 9) + "," + str.substr(-9, 3) + "," + str.substr(-6, 3) + "," + str.substr(-3, 3);
                                        }
                                    }
                                    // the third row: popularity
                                    if (artist_music.popularity) {
                                        $scope.array_artistTeams[l].popularity = artist_music.popularity;
                                    }
                                    // the fourth row: checkAt
                                    if (artist_music.external_urls && artist_music.external_urls.spotify) {
                                        $scope.array_artistTeams[l].checkAt = artist_music.external_urls.spotify;
                                    }
                                }
                                else {
                                    $scope.array_artistTeams[l].show_artist_musicRelated = false;
                                }
                            } // end for
                        } // end else
                    }); // end promise all
                } 
                else {
                    $scope.show_artists_musicRelated = false;
                } // music part end
                // check $scope.show_artistTeams_photos = false;
                for (var m = 0; m < $scope.array_artistTeams.length; m++) {
                    if ($scope.array_artistTeams[m].show_artistTeam_photos && $scope.array_artistTeams[m].show_artistTeam_photos == true) {
                        $scope.show_artistTeams_photos = true;
                        break;
                    }
                }
                // check $scope.show_artists_musicRelated = false;
                for (var n = 0; n < $scope.array_artistTeams.length; n++) {
                    if ($scope.array_artistTeams[n].show_artist_musicRelated && $scope.array_artistTeams[n].show_artist_musicRelated == true) {
                        $scope.show_artist_musicRelated = true;
                        break;
                    }
                }
                // check $scope.no_records_artistTeamsTab = true;
                if ($scope.show_artistTeams_photos == false && $scope.show_artists_musicRelated == false) {
                    $scope.no_records_artistTeamsTab = true;
                }
            }); // end then
        };

        $scope.sendArtistTeamMusicTab = function (artistName) {
            var defer = $q.defer();
            var url_music = "/?";
            url_music =  url_music + "artistName=" + artistName;
            // alert(url_music);
            $http ({method: 'GET', url: url_music})
            .then (function successCallback (response) {
                $scope.jsonObj_music = response.data;
                // alert(JSON.stringify($scope.jsonObj_music));
                defer.resolve(response.data);  
            }, function errorCallback (response) {
                alert("Failed to request nodejs using " + url_music + " !");
            });
            return defer.promise;
        };

        $scope.sendArtistTeamPhotoTab = function (artistTeam) {
            var defer = $q.defer();
            var url_artistTeams = "/?";
            url_artistTeams =  url_artistTeams + "artistTeam=" + artistTeam;
            // alert(url_artistTeams);
            $http ({method: 'GET', url: url_artistTeams})
            .then (function successCallback (response) {
                $scope.jsonObj_artistTeam = response.data;
                // alert(JSON.stringify($scope.jsonObj_artistTeam));
                defer.resolve(response.data);  
            }, function errorCallback (response) {
                alert("Failed to request nodejs using " + url_artistTeams + " !");
            });
            return defer.promise;
        };

        $scope.generateArtistTeamPhotos = function (jsonObj_photos) {
            var array_photos = [];
            // alert(JSON.stringify(jsonObj_photos));
            if(jsonObj_photos.items && jsonObj_photos.items.length != 0) {
                for (var i = 0; i < jsonObj_photos.items.length; i++) {
                    if (jsonObj_photos.items[i].link) {
                        var url_photo = jsonObj_photos.items[i].link;
                        array_photos.push(url_photo);
                    }
                }
            }
            return array_photos;
        };

        $scope.sendVenueTab = function (param_venueInfo) {
            $scope.show_progress = true;
            // $scope.no_records_venueTab = false;
            var defer = $q.defer();
            // var venueName = $scope.array_results[$index].venue_info;
            var venueName = param_venueInfo;
            var url_venue = "/?";
            url_venue =  url_venue + "venue=yes" + "&venueName=" + venueName;
            // alert(url_venue);
            $http ({method: 'GET', url: url_venue})
            .then (function successCallback (response) {
                $scope.jsonObj_venue = response.data;
                // alert(JSON.stringify($scope.jsonObj_event));
                $scope.show_progress = false;
                defer.resolve(response.data);  
            }, function errorCallback (response) {
                alert("Failed to request nodejs using " + url_venue + "!");
                $scope.show_progress = false;
                // $scope.no_records_venueTab = true;
            });
            return defer.promise;
        };

        $scope.generateVenueContent = function (param_venueInfo) {
            var promise = $scope.sendVenueTab(param_venueInfo);
            promise.then(function() {
                $scope.show_venue_address = true;
                $scope.show_venue_city = true;
                $scope.show_venue_phoneNumber = true;
                $scope.show_venue_openHours = true;
                $scope.show_venue_generalRule = true;
                $scope.show_venue_childRule = true;
                $scope.show_venue_googleMap = true;
                // $scope.no_records_venueTab = false;
                $scope.venue_name = param_venueInfo;
                var jsonObj_venue = $scope.jsonObj_venue;
                if (jsonObj_venue._embedded && jsonObj_venue._embedded.venues) {
                    var venue = jsonObj_venue._embedded.venues[0];
                    // the first row: address
                    if (venue.address && venue.address.line1) {
                        $scope.venue_address = venue.address.line1;
                    }
                    else {
                        $scope.show_venue_address = false;
                    }
                    // the second row: city
                    if (venue.city && venue.city.name) {
                        $scope.venue_city = venue.city.name;
                    }
                    else {
                        $scope.show_venue_city = false;
                    }
                    // the third row: phone number
                    if (venue.boxOfficeInfo && venue.boxOfficeInfo.phoneNumberDetail && (venue.boxOfficeInfo.phoneNumberDetail != "")) {
                        $scope.venue_phoneNumber = venue.boxOfficeInfo.phoneNumberDetail;
                    }
                    else {
                        $scope.show_venue_phoneNumber = false;
                    }
                    // the fourth row: open hours
                    if (venue.boxOfficeInfo && venue.boxOfficeInfo.openHoursDetail) {
                        $scope.venue_openHours = venue.boxOfficeInfo.openHoursDetail;
                    }
                    else {
                        $scope.show_venue_openHours = false;
                    }
                    // the fifth row: general rule
                    if (venue.generalInfo && venue.generalInfo.generalRule) {
                        $scope.venue_generalRule = venue.generalInfo.generalRule;
                    }
                    else {
                        $scope.show_venue_generalRule = false;
                    }
                    // the sixth row: child rule
                    if (venue.generalInfo && venue.generalInfo.childRule) {
                        $scope.venue_childRule = venue.generalInfo.childRule;
                    }
                    else {
                        $scope.show_venue_childRule = false;
                    }
                    // the seventh row: google map
                    if (venue.location && venue.location.latitude && venue.location.longitude) {
                        $scope.googleMap_latitude = parseFloat(venue.location.latitude);
                        $scope.googleMap_longitude = parseFloat(venue.location.longitude);
                        // alert($scope.googleMap_latitude + " " + typeof($scope.googleMap_latitude));
                        // alert($scope.googleMap_longitude + " " + typeof($scope.googleMap_longitude));
                    }
                    else {
                        $scope.show_venue_googleMap = false;
                    }
                } // end if
                // else {
                //     $scope.no_records_venueTab = true;
                // }
            }); // end then
        };

        $scope.initMap = function() {
            var location = {lat: $scope.googleMap_latitude, lng: $scope.googleMap_longitude};
            var map = new google.maps.Map(document.getElementById('googleMap'), {zoom: 16, center: location});
            var marker = new google.maps.Marker({position: location, map: map});
        };

        $scope.sendUpcomingEventsTab = function (param_venueInfo) {
            $scope.show_progress = true;
            $scope.no_records_upcomingEventsTab = false;
            var defer = $q.defer();
            // var venueName = $scope.array_results[$index].venue_info;
            var venueName = param_venueInfo;
            var url_upcomingEvents = "/?";
            url_upcomingEvents =  url_upcomingEvents + "upcomingEvents=yes" + "&venueName=" + venueName;
            // alert(url_upcomingEvents);
            $http ({method: 'GET', url: url_upcomingEvents})
            .then (function successCallback (response) {
                $scope.jsonObj_upcomingEvents = response.data;
                // alert(JSON.stringify($scope.jsonObj_event));
                $scope.show_progress = false;
                defer.resolve(response.data);  
            }, function errorCallback (response) {
                alert("Failed to request nodejs using " + url_upcomingEvents + "!");
                $scope.show_progress = false;
                $scope.no_records_upcomingEventsTab = true;
            });
            return defer.promise;
        };

        $scope.generateUpcomingEventsContent = function (param_venueInfo) {
            var promise = $scope.sendUpcomingEventsTab(param_venueInfo);
            promise.then(function() {
                $scope.no_records_upcomingEventsTab = false;
                $scope.button_show_more = true;
                $scope.input_sortType = "Default";
                $scope.input_sortOrder = "Ascending";

                var jsonObj_upcomingEvents = $scope.jsonObj_upcomingEvents;
                // structure of $scope.array_upcomingEvents:
                // [{1 displayName: string, 2 url: link, 3 artist: string, 4 dateTime: format, 5 timeStamp: int, 6 type: string}]
                $scope.array_upcomingEvents5 = [];
                $scope.array_upcomingEvents = [];
                if (jsonObj_upcomingEvents.resultsPage && jsonObj_upcomingEvents.resultsPage.results && jsonObj_upcomingEvents.resultsPage.results.event) {
                    var event = jsonObj_upcomingEvents.resultsPage.results.event;
                    for (var i = 0; i < event.length; i++) {
                        var upcomingEvent = {};
                        // the first row: displayName
                        if (event[i].displayName) {
                            upcomingEvent.displayName = event[i].displayName;
                        }
                        if (event[i].uri) {
                            upcomingEvent.uri = event[i].uri;
                        }
                        // the second row: artist
                        if (event[i].performance[0] && event[i].performance[0].displayName) {
                            upcomingEvent.artist = event[i].performance[0].displayName;
                        }
                        // the third row: dateTime
                        if (event[i].start && event[i].start.date) {
                            var date = event[i].start.date;
                            var date_moment = moment(date, "YYYY-MM-DD").format("MMM DD, YYYY");
                        }
                        else {
                            date = "";
                        }
                        if (event[i].start && event[i].start.time) {
                            var time = event[i].start.time;
                            var time_sorted = event[i].start.time;
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
                        if (event[i].type) {
                            upcomingEvent.type = event[i].type;
                        }
                        $scope.array_upcomingEvents.push(upcomingEvent);
                    } // end for
                    if ($scope.array_upcomingEvents.length == 0) {
                        $scope.no_records_upcomingEventsTab = true;
                    }
                    else {
                        $scope.array_upcomingEvents5 = $scope.getArrayUpcomingEvents5($scope.array_upcomingEvents);
                        $scope.array_upcomingEvents_sorted = $scope.array_upcomingEvents;
                        $scope.array_upcomingEvents5_sorted = $scope.array_upcomingEvents5;
                        $scope.array_upcomingEvents_show = $scope.array_upcomingEvents5_sorted;
                    }
                } // end if
                else {
                    $scope.no_records_upcomingEventsTab = true;
                }
            }); // end then
            $scope.show_results = false;
            $scope.show_favorites = false;
            $scope.show_details = true;
        };

        $scope.getArrayUpcomingEvents5 = function (array_upcomingEvents) {
            if (array_upcomingEvents.length <= 5) {
                array_upcomingEvents5 = array_upcomingEvents;
            }
            else {
                array_upcomingEvents5 = [array_upcomingEvents[0], array_upcomingEvents[1], array_upcomingEvents[2], array_upcomingEvents[3], array_upcomingEvents[4]];
            }
            return array_upcomingEvents5;
        };

        $scope.clickShowMore = function () {
            $scope.array_upcomingEvents_show = $scope.array_upcomingEvents_sorted;
            $scope.button_show_more = false;
        };

        $scope.clickShowLess = function () {
            $scope.array_upcomingEvents_show = $scope.array_upcomingEvents5_sorted;
            $scope.button_show_more = true;
        };

        $scope.sortBy = function() {
            if ($scope.input_sortType == 'Default') {
                // alert("test1: " + $scope.input_sortType + " " + $scope.input_sortOrder);
                $scope.array_upcomingEvents_sorted = $scope.array_upcomingEvents;
                $scope.array_upcomingEvents5_sorted = $scope.array_upcomingEvents5;
            }
            else {
                // alert("test2: " + $scope.input_sortType + " " + $scope.input_sortOrder);
                if ($scope.input_sortOrder == 'Ascending') {
                    $scope.reverse = false;
                }
                else {
                    $scope.reverse = true;
                }
                if ($scope.input_sortType == 'EventName') {
                    $scope.sortType = "displayName";
                }
                else if ($scope.input_sortType == 'Time') {
                    $scope.sortType = "timeStamp";
                }
                else if ($scope.input_sortType == 'Artist') {
                    $scope.sortType = "artist";
                }
                else if ($scope.input_sortType == 'Type') {
                    $scope.sortType = "type";
                }
                $scope.array_upcomingEvents_sorted = orderBy($scope.array_upcomingEvents, $scope.sortType, $scope.reverse);
                $scope.array_upcomingEvents5_sorted = $scope.getArrayUpcomingEvents5($scope.array_upcomingEvents_sorted);;
            }
            if ($scope.button_show_more == true) {
                $scope.array_upcomingEvents_show = $scope.array_upcomingEvents5_sorted;
            }
            else {
                $scope.array_upcomingEvents_show = $scope.array_upcomingEvents_sorted;
            }
        };

        $scope.checkHighlight = function (param_array, $index) {
            for (var i = 0; i < param_array.length; i++) {
                if ( i == $index) {
                    param_array[i].highlight = true;
                }
                else {
                    param_array[i].highlight = false;
                }
            }
        }

        // when click button Favorites
        $scope.clickFavorites = function() {
            // download data from localStorage to array_favorites
            $scope.myStorage2 = window.localStorage;
            // structure of $scope.array_favorites
            // [{1 eventId: int, 2 date: format, 3 event: string, 4 event_short: string, 5 category: string, 6 venue_info: string}, ...]
            $scope.array_favorites = [];
            for (var i = 0; i < $scope.myStorage2.length; i++) {
                var key = $scope.myStorage2.key(i);
                var value = JSON.parse(localStorage.getItem(key));
                $scope.array_favorites.push(value);
            }
            $scope.no_records_favorites = false;
            // check empty
            if ($scope.array_favorites.length == 0) {
                $scope.no_records_favorites = true;
            }
            // history location
            $scope.array_history.push("favorites");
            $scope.click_favorites = true;
            $scope.click_results = false;
            $scope.show_results = false;
            $scope.show_details = false;
            $scope.show_favorites = true;
        };

        // for favorite button in results table
        $scope.addToFavoritesFromResults = function (param_index) {
            $scope.no_records_favorites = false;
            var index = param_index;
            if ($scope.array_results[index].show_starBorder == true) {
                var value = {};
                value.eventId = $scope.array_results[index].eventId;
                value.highlight = false;
                // value.show_starBorder = false;
                value.date = $scope.array_results[index].date;
                value.event = $scope.array_results[index].event;
                value.event_short = $scope.array_results[index].event_short;
                value.category = $scope.array_results[index].category;
                value.venue_info = $scope.array_results[index].venue_info;
                $scope.array_results[index].show_starBorder = false;
                // push the row in array_results into localStorage
                localStorage.setItem(value.eventId, JSON.stringify(value));
                if ($scope.result_eventId == $scope.array_results[index].eventId) {
                    $scope.result_show_starBorder = false;
                }
            }
            else {
                $scope.array_results[index].show_starBorder = true;
                localStorage.removeItem($scope.array_results[index].eventId);
                if ($scope.result_eventId == $scope.array_results[index].eventId) {
                    $scope.result_show_starBorder = true;
                }
            }
        };

        $scope.getResultForFavorites = function(param_index) {
            var index = param_index;
            $scope.result_eventId = $scope.array_results[index].eventId;
            $scope.result_date = $scope.array_results[index].date;
            $scope.result_event = $scope.array_results[index].event;
            $scope.result_event_short = $scope.array_results[index].event_short;
            $scope.result_category = $scope.array_results[index].category;
            $scope.result_venue_info = $scope.array_results[index].venue_info;
            $scope.result_show_starBorder = $scope.array_results[index].show_starBorder;
            $scope.result_highlight = $scope.array_results[index].highlight;
        };

        $scope.addToFavoritesFromDetails = function() {
            $scope.no_records_favorites = false;
            if ($scope.result_show_starBorder == true) {
                var result = {};
                result.eventId = $scope.result_eventId;
                result.date = $scope.result_date;
                result.event = $scope.result_event;
                result.event_short = $scope.result_event_short;
                result.category = $scope.result_category;
                result.venue_info = $scope.result_venue_info;
                $scope.result_show_starBorder = false;
                result.highlight = $scope.result_highlight;
                // alert("expected:false " + $scope.result_show_starBorder);
                // push the row in array_results into localStorage
                localStorage.setItem(result.eventId, JSON.stringify(result));
                for (var i = 0; i < $scope.array_results.length; i++) {
                    if ($scope.array_results[i].eventId == $scope.result_eventId) {
                        $scope.array_results[i].show_starBorder = false;
                    }
                } // end for
            }
            else {
                $scope.result_show_starBorder = true;
                // alert("expected:true " + $scope.result_show_starBorder);
                localStorage.removeItem($scope.result_eventId);
                for (var j = 0; j < $scope.array_results.length; j++) {
                    if ($scope.array_results[j].eventId == $scope.result_eventId) {
                        $scope.array_results[j].show_starBorder = true;
                    }
                } // end for
            } // end else
        };

        $scope.deleteFromFavorites = function(param_eventId, param_index) {
            var key = param_eventId;
            var index = param_index;
            // $scope.array_results[index].show_starBorder = true;
            // var key = $scope.array_favorites[index].eventId;
            $scope.array_favorites.splice(index, 1);
            localStorage.removeItem(key);
            // check empty
            if ($scope.array_favorites.length == 0) {
                $scope.no_records_favorites = true;
            }
            if ($scope.result_eventId == key) {
                $scope.result_show_starBorder = true;
            }
            // make array_results[i].show_starBorder = true
            for (var i = 0; i < $scope.array_results.length; i++) {
                // alert($scope.array_results[i].eventId);
                if ($scope.array_results[i].eventId == key) {
                    $scope.array_results[i].show_starBorder = true;
                }
            }
        };

        $scope.clickTwitter = function() {
            var tweetText = "Check out " + $scope.result_event + " located at " + $scope.result_venue_info + ". Website:&url=" + $scope.event_buyTicketAt + "&hashtags=" + "CSCI571EventSearch";
            var tweetUrl = "https://twitter.com/intent/tweet?text=" + tweetText;
            $scope.tweetWindow = window.open(tweetUrl, "Share a link on Twitter");
        };

        $scope.clickList = function() {
            // previous location is results
            if ($scope.array_history[$scope.array_history.length - 2] == "results") {
                $scope.array_history.push("results");
                $scope.show_favorites = false;
                $scope.show_details = false;
                $scope.show_results = true;
            }
            // previous location is favorites
            else {
                $scope.array_history.push("favorites");
                $scope.show_results = false;
                $scope.show_details = false;
                $scope.show_favorites = true;
            }
        };

        $scope.addLocationDetails = function() {
            // history location
            $scope.array_history.push("details");
        }

        $scope.addLocationResults = function() {
            // history location
            $scope.array_history.push("results");
        }

        $scope.enableResultsDetails = function() {
            $scope.results_details_disabled = false;
        }

        $scope.enableFavoritesDetails = function() {
            $scope.favorites_details_disabled = false;
        }

        $scope.animationToLeft = function() {
            $scope.show_slide = true;
            $scope.slideToRight = false;
        };

         $scope.animationToRight = function() {
            $scope.show_slide = true;
            $scope.slideToRight = true;
        };

        $scope.getMatches = function (param_searchText) {
            $scope.array_searchResults = [];
            var searchText = param_searchText;
            var url_autocomplete = "/?";
            url_autocomplete =  url_autocomplete + "searchText=" + searchText;
            $http ({method: 'GET', url: url_autocomplete})
            .then (function successCallback (response) {
                $scope.array_searchResults = response.data;
                // alert(typeof($scope.array_searchResults[0]) + $scope.array_searchResults[0]);
            }, function errorCallback (response) {
                alert("Failed to request nodejs using " + url_autocomplete + "!");
            });
        };

        $scope.showValue = function(param_value) {
            alert(param_value);
        }

    }]);

})(angular);
