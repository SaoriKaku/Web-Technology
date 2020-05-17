
<?php 
    /*
    * the server request by user clicking the search button
    */
    if(isset($_GET['keyword'])) {
        // function encode() from external file
        require('geoHash.php');
        // the first parameter: apikey
        $KEY_TICKETMASTER_API = "sWpPtmciRAZg1Ac7LUuAN9JVOU8QY5Q2";
        $KEY_GOOGLE_API = "AIzaSyDBxDr0CHnmIPk7dl8NfTe7XOUarxwKiBw";
        // the second parameter: keyword
        $keyword = $_GET['keyword'];
        $keyword = str_replace(' ', '+', $keyword);
        // the third parameter: segmentId
        $category = $_GET['category'];
        //echo $category;
        if($category == "Music") {
            $segmentId = "KZFzniwnSyZfZ7v7nJ";
        }
        if($category == "Sports") {
            $segmentId = "KZFzniwnSyZfZ7v7nE";
        }
        if($category == "Arts & Theatre") {
            $segmentId = "KZFzniwnSyZfZ7v7na";
        }
        if($category == "Film") {
            $segmentId = "KZFzniwnSyZfZ7v7nn";
        }
        if($category == "Miscellaneous") {
            $segmentId = "KZFzniwnSyZfZ7v7n1";
        }
        if($category == "Default") {
            $segmentId = "";
        }
        // the fourth parameter: radius
        if (!empty($_GET['distance'])) {
            $distance = $_GET['distance'];
        }
        else {
            $distance = 10;
        }
        //echo $distance;
        // the fifth parameter: unite
        $unite = "miles";
        // the sixth parameter: geoPoint
        // get the coordinates from two cases: here or the input location by user
        // the first case: here 
        if (($_GET['location']) != "input_location") {
            $locationToJS = "";
            $location = $_GET['location'];
            $coordinates = explode(',', $location);
            $latitude = $coordinates[0];
            $longitude = $coordinates[1];
            $geoPoint = encode($latitude, $longitude, 5);
            //echo $geoPoint;
        }
        // the second case: input location by user
        else {
            $inputLocation = $_GET['input_location'];
            $locationToJS = $_GET['input_location'];
            $inputLocation = str_replace(' ', '+', $inputLocation);
            //find the coordinates of input location 
            $url_googleMap_API = "https://maps.googleapis.com/maps/api/geocode/json?address=$inputLocation&key=$KEY_GOOGLE_API";
            $str_googleMap_API = file_get_contents($url_googleMap_API);
            // json_decode: turn a string into an array
            $json_googleMap_API = json_decode($str_googleMap_API, true);
            // if nothing is returned 
            $latitude = $json_googleMap_API['results'][0]['geometry']['location']['lat'];
            $longitude = $json_googleMap_API['results'][0]['geometry']['location']['lng'];
            $geoPoint = encode($latitude, $longitude, 5);
        }
        // get the form input 
        $str_form_input = $keyword.",".$category.",".$distance.",".$locationToJS.",".$latitude.",".$longitude;
        // echo $str_form_input;
        //get the string results from Ticketmaster API
        $url_Ticketmaster_API = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=$KEY_TICKETMASTER_API&keyword=$keyword&segmentId=$segmentId&radius=$distance&unit=$unite&geoPoint=$geoPoint";
        $str_Ticketmaster_API = file_get_contents($url_Ticketmaster_API);
        //echo $str_Ticketmaster_API;
    }

    if(isset($_POST['eventID'])) {
        $event_name = $_POST['eventName'];
        $event_ID = $_POST['eventID'];
        // echo $event_ID;
        $url_Ticketmaster_API_event = "https://app.ticketmaster.com/discovery/v2/events/$event_ID.json?apikey=$KEY_TICKETMASTER_API";
        $str_Ticketmaster_API_event = file_get_contents($url_Ticketmaster_API_event);
        // echo $str_Ticketmaster_API_event;

        if (($_POST['venueName']) != "N/A") {
            $venue_name = $_POST['venueName'];
            $venue_name = str_replace(' ', '%20', $venue_name);
            // echo $venue_name;
            $url_Ticketmaster_API_venue = "https://app.ticketmaster.com/discovery/v2/venues.json?keyword=$venue_name&apikey=$KEY_TICKETMASTER_API";
            $str_Ticketmaster_API_venue = file_get_contents($url_Ticketmaster_API_venue);
            // echo $str_Ticketmaster_API_venue;
        }
        else {
            $str_Ticketmaster_API_venue = "";
        }    
    }
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title> Homework6 </title>
    <style type="text/css">
    *{
	   margin: 0;
	   padding: 0;
	   box-sizing: border-box; 
	}
	.page {
	    font-family: Times;
        margin: 0 auto;
        width: 1200px;
	}
	.box {
		margin: 20px auto;
		height: 210px;
		width: 600px;
		border: 3px solid #c1c1c1;
		background-color: #f9f9f9;
		padding: 10px;
		line-height: 26px;
    }
    .box h1 {
    	text-align: center;
    	font-style: italic;
    	margin-bottom: 10px;
    	font-weight: 400;
    }
    .box h4 {
    	display: inline;
    }
   	#radio_location2 {
   		margin-left: 285px;
   	}
   	.box button {
   		width: 55px;
   		height: 20px;
   	}
   	#button_search {
   		margin-left: 60px;
   	}
   	table {
        width: 1200px;
        margin: 0 auto;
   		border-color: #d5d5d5;
        border-collapse: collapse;
   	}
    a {
        text-decoration: none;
        color: black;
    }
    a:hover {
        color: #c1c1c1;
    }
    li {
        list-style:none;
        margin-top: 5px;
        margin-bottom: 5px
    }
    .arrow {
        width: 40px;
        height: 20px;
        margin: 10px auto;
    }
    .title {
        color: #767676;
        margin-top: 10px;
        text-align: center;
    }
    #events {
        z-index: 0;
    }
    .googleMap1 {
        z-index: 1;
        width: 300px;
        height: 200px;
        position: absolute;
        display: none;
    }
    .googleMap2 {
        z-index: 1;
        width: 400px;
        height: 300px;
        display: inline-block; 
    }
    .mode1 {
        z-index: 2;
        position: absolute;
        width: 100px;
        height: 60px;
        overflow: hidden;
        display: none;
    }
    .mode2 {
        z-index: 2;
        width: 100px;
        height: 90px;
        overflow: hidden;
        display: inline-block; 
        margin-left: 50px; 
        margin-right: 20px;
    }
    .chooseMode {
        border: none;
    }
    option {
        width: 100px;
        height: 20px;
        text-align: center;
        font-size: 16px;
        font-family: Times;
        background-color: #f9f9f9;
    }
    </style>

</head>
<body class="page" >
	<div class="box">
		<form id="form" method="get">
    		<h1>Events Search</h1>
    		<hr>
    		<h4> Keyword </h4>
    		<input type="text" name="keyword" id="text_keyword" required="required">
 			<br>
    		<h4> Category </h4>
    		<select name="category" id="select_category" required="required">
    			<option selected="selected"> Default </option>
    			<option> Music </option>
    			<option> Sports </option>
    			<option> Arts & Theatre </option>
    			<option> Film </option>
    			<option> Miscellaneous </option>
    		</select>
    		<br>
    		<h4> Distance (miles) </h4>
    		<input type="text" name="distance" id="text_distance" placeholder="10">
    		<h4> from </h4>
    		<input type="radio" name="location" id="radio_location1" value="current_location" checked="checked" onclick="setRadio()">
    		<span> Here </span>
    		<br>
    		<input type="radio" name="location" id="radio_location2" value="input_location" onclick="setRadio()">
    		<input type="text" name="input_location" id="input_location" placeholder="location">
    		<br>
    		<button type="submit" name="search" id="button_search" onclick="clearGoogleMap()"> Search </button>
    		<button type="button" name="clear" id="button_clear" onclick="clearForm()"> Clear </button>
    	</form>
	</div>
	<div id="events"></div>
	<div id="googleMap1" class="googleMap1"></div>
	<div id="mode1" class="mode1">
		<select size="3" id="chooseMode1" class="chooseMode">
			<option id="walking1" value="WALKING"> Walk there </option>
			<option id="bicycling1" value="BICYCLING"> Bike there </option>
			<option id="driving1" value="DRIVING"> Drive there </option>
		</select>
	</div>

<script type="text/javascript">
        window.onload = function() {
            document.getElementById("button_search").disabled = true;
            setRadio();
            getLocationByIP();
            document.getElementById("button_search").disabled = false;
            getInput()
            generateEventsTable();
            generateEventDetailTable();
        }

       /*
        * set the value of radios 
        */
        function setRadio() {
            if (document.getElementById("radio_location1").checked) {
                document.getElementById("input_location").disabled = true;
                document.getElementById("input_location").value = ""; }
            if (document.getElementById("radio_location2").checked) {
                document.getElementById("input_location").disabled = false;
                document.getElementById("input_location").required = true; }
        }

       /*
        * find the coordinate according to IP address
        * @return array_cordinates: an array that stores latitude and longitude.
        */
        function getLocationByIP() {
            var URL_IP = "http://ip-api.com/json";
            var ipHttp = new XMLHttpRequest();
            ipHttp.open("GET", URL_IP, false);
            ipHttp.send();
            var ipObj = JSON.parse(ipHttp.responseText);
            var latitude = ipObj.lat;
            var longitude = ipObj.lon;
            var array_cordinates = [];
            array_cordinates.push(latitude);
            array_cordinates.push(longitude);
            document.getElementById("radio_location1").value = latitude + "," + longitude;
            // console.log(document.getElementById("radio_location1").value);
            return array_cordinates; 
        }

        var strTicketmasterAPI = <?php echo json_encode($str_Ticketmaster_API); ?>;
        var objTicketmasterAPI = JSON.parse(strTicketmasterAPI);
        var strFromInput = <?php echo json_encode($str_form_input); ?>;
        var latitude_start;
        var longitude_start;
        var latitude_destination1;
        var longitude_destination1;
        var KEY_GOOGLE_API = "AIzaSyDBxDr0CHnmIPk7dl8NfTe7XOUarxwKiBw";

        /*
        * strFromInput has 4 element: keyword, category, distance, location
        * after submitting the form, it should hold these values
        */
        function getInput() {
            var str_from_input = strFromInput.split(",");
            document.getElementById("text_keyword").value = str_from_input[0];
            document.getElementById("select_category").value = str_from_input[1];
            document.getElementById("text_distance").value = str_from_input[2];
            latitude_start = parseFloat(str_from_input[4]);
            longitude_start = parseFloat(str_from_input[5]);

            if(str_from_input[3] == "") {
                document.getElementById("radio_location1").checked = true;
                document.getElementById("radio_location2").checked = false;
                document.getElementById("input_location").disabled = true;
            }
            else {
                document.getElementById("radio_location1").checked = false;
                document.getElementById("radio_location2").checked = true;
                document.getElementById("input_location").disabled = false;
                document.getElementById("input_location").value = str_from_input[3];
            }
        }

       /*
        * parse the json object "objTicketmasterAPI" from PHP and show the Events table with five columns:
        * Date, Icon, Event, Genre, Venue. 
        * the html_text is in the div whose id is events
        */
        function generateEventsTable() {
            var jsonObj = objTicketmasterAPI;
            var html_text = "";
            if (jsonObj._embedded && jsonObj._embedded.events) { //align='center'
             var events = jsonObj._embedded.events;
             html_text = html_text + "<table border='1'><tr>";
             html_text = html_text + "<th> Date </th>";
             html_text = html_text + "<th> Icon </th>";
             html_text = html_text + "<th> Event </th>";
             html_text = html_text + "<th> Genre </th>";
             html_text = html_text + "<th> Venue </th>";
             html_text = html_text + "<th></th></tr>";

             for(var i=0; i<events.length; i++) {
                 html_text = html_text + "<tr>";
                 // the first column: date var date_localDate; var date_localTime;
                 if(events[i].dates && events[i].dates.start && events[i].dates.start.localDate) {
                     var date_localDate = events[i].dates.start.localDate;
                 }
                 else{
                     date_localDate = "";
                 }
                 if(events[i].dates && events[i].dates.start && events[i].dates.start.localTime) {
                     var date_localTime = events[i].dates.start.localTime;
                 }
                 else{
                     date_localTime = "";
                 }
                 html_text = html_text + "<td align='center'>" + date_localDate + "<br>" + date_localTime + "</td>";
                 // the second column: icon <img src='' />
                 if(events[i].images[0] && events[i].images[0].url) {
                     var img_src = events[i].images[0].url;
                 }
                 else{
                     img_src = "N/A";
                 }
                 html_text = html_text + "<td align='center'><img src='" + img_src + "'" + "width=50" + "height=50" + "/></td>";
                 // the third column: event <a href='' target='_blank'></a>
                 if(events[i].name) {
                     var event_name = events[i].name;
                 }
                 else{
                     event_name = "";
                 }
                 html_text = html_text + "<td style='padding-left:10px'><a href='javascript:submitVirtualForm(" + i + ")'>" + event_name + "</a></td>";
                 // the fourth column: genre var genre_name;
                 if(events[i].classifications && events[i].classifications[0].segment && events[i].classifications[0].segment.name) {
                     var genre_name = events[i].classifications[0].segment.name;
                 }
                 else{
                     genre_name = "N/A";
                 }
                 html_text = html_text + "<td style='padding-left:10px'>" + genre_name + "</td>";
                 // the fifth column: venue var venue_name;
                 if(events[i]._embedded && events[i]._embedded.venues && events[i]._embedded.venues[0].name) {
                     var venue_name = events[i]._embedded.venues[0].name;
                 }
                 else{
                     venue_name = "N/A";
                 }
                 // parse the coordinate of events to use in map1
                 if(events[i]._embedded && events[i]._embedded.venues && events[i]._embedded.venues[0].location && events[i]._embedded.venues[0].location.latitude) {
                     var venue_latitude = events[i]._embedded.venues[0].location.latitude;
                 }
                 if(events[i]._embedded && events[i]._embedded.venues && events[i]._embedded.venues[0].location && events[i]._embedded.venues[0].location.longitude) {
                     var venue_longitude = events[i]._embedded.venues[0].location.longitude;
                 }
                 html_text = html_text + "<td style='padding-left:10px'><a href='javascript:generateGoogleMap(" + venue_latitude + "," + venue_longitude + ")'>" + venue_name + "</a></td>";
                 // build a virtual form var event_ID;
                 if(events[i].id) {
                     var event_ID = events[i].id;
                 }
                 else{
                     event_ID = "";
                 }
                 // html_text = html_text + "<td>" + event_ID + "</td>";
                 html_text = html_text + "<td><form id='virtualForm" + i + "' method='post'>";
                 html_text = html_text + "<input type='hidden' name='eventID' value='" + event_ID + "'>";
                 html_text = html_text + "<input type='hidden' name='venueName' value='" + venue_name + "'>";
                 html_text = html_text + "<input type='hidden' name='eventName' value='" + event_name + "'>";
                 html_text = html_text + "</form></td>";
                 html_text = html_text + "</tr>";
             }// end for
             html_text = html_text + "</table>";   
            }// end else
            else {
                html_text = html_text + "<table border='1' style='width:900px; margin-left:150px'><tr><td bgcolor='#ececec'>";
                html_text = html_text + "<center> No Records has been found </center>";
                html_text = html_text + "</td></tr></table></center>";
            }
            document.getElementById("events").innerHTML = html_text;
            document.getElementById("events").style.display = "block";
        } // end function

       /*
        * when user clicks event on the third columns, the virtual form on that row will be submitted
        * @param count: the number of row
        */
        function submitVirtualForm(count) {
            var j = count;
            document.getElementById("virtualForm" + j).submit();
            // alert("test");
        }

        var strEventName = <?php echo json_encode($event_name); ?>;
        var strTicketmasterAPIEvent = <?php echo json_encode($str_Ticketmaster_API_event); ?>;
        // alert(strTicketmasterAPIEvent);
        var objTicketmasterAPIEvent = JSON.parse(strTicketmasterAPIEvent);
        var strTicketmasterAPIVenue = <?php echo json_encode($str_Ticketmaster_API_venue); ?>;
        //alert(strTicketmasterAPIVenue);
        var objTicketmasterAPIVenue = JSON.parse(strTicketmasterAPIVenue);

       /*
        * parse the json object "objTicketmasterAPIEvent" from PHP and show the Event Detail table with seven columns:
        * Date, Artists/Team, Venue, Genres, Price Ranges, Ticket Status, Buy Ticket At and the seat map if json contains
        * the html_text is in the div whose id is events
        */
        function generateEventDetailTable() {
            var jsonObj = objTicketmasterAPIEvent;
            if (jsonObj == null) {
                return;
            }
            var html_text = "";
            html_text = html_text + "<br>";
            html_text = html_text + "<center><h2>" + strEventName + "</h2></center>";
            // html_text = html_text + "<div style='overflow:hidden'><div style='width:400px;float:left;margin-left:150px'><ul>";
            html_text = html_text + "<div style='width:800px; margin-left:200px'><div style='width:300px;display:inline-block'><ul>";
            // the first row: Date
            if (jsonObj.dates && jsonObj.dates.start && jsonObj.dates.start.localDate) {
                var date_localDate = jsonObj.dates.start.localDate;
            }
            else {
                date_localDate = "";
            }
            if (jsonObj.dates && jsonObj.dates.start && jsonObj.dates.start.localTime) {
                var date_localTime = jsonObj.dates.start.localTime;
            }
            else {
                date_localTime = "";
            }
            if (date_localDate != "" || date_localTime != "") {
                html_text = html_text + "<li><h3> Date </h3></li>";
                html_text = html_text + "<li>" +  date_localDate + " " + date_localTime + "</li>";
            }
            // the second row: Artists/Team
            if (jsonObj._embedded && jsonObj._embedded.attractions) {
                var attractions = jsonObj._embedded.attractions;
                if (attractions[0].name && attractions[0].name != "undefined" && attractions[0].name != "Undefined") {
                    var artistOrTeam1 = attractions[0].name;
                }
                else {
                    artistOrTeam1 = "";
                }
                if (attractions[1] && attractions[1].name && attractions[1].name != "undefined" && attractions[1].name != "Undefined") {
                    var artistOrTeam2 = attractions[1].name;
                }
                else {
                    artistOrTeam2 = "";
                }
                html_text = html_text + "<li><h3> Artists/Team </h3></li>";
                if (artistOrTeam1 == "" && artistOrTeam2 == "") {
                     html_text = html_text + "<li></li>";
                }
                else if (artistOrTeam1 != "" && artistOrTeam2 == "") {
                    html_text = html_text + "<li><a href='" + attractions[0].url + "'>" + artistOrTeam1 + "</a></li>";
                }
                else if (artistOrTeam1 != "" && artistOrTeam2 != "") {
                    html_text = html_text + "<li><a href='" + attractions[0].url + "'>" + artistOrTeam1 + "</a> | <a href='" + attractions[1].url + "'>" + artistOrTeam2 + "</a></li>";
                }
                
            }
            // the third row: Venue
            if (jsonObj._embedded && jsonObj._embedded.venues && jsonObj._embedded.venues[0].name) {
                var venue = jsonObj._embedded.venues[0].name;
                html_text = html_text + "<li><h3> Venue </h3></li>";
                html_text = html_text + "<li>" + venue + "</li>";
            }
            // the fourth row: Genres
            if (jsonObj._embedded && jsonObj._embedded.attractions && jsonObj._embedded.attractions[0].classifications) {
                var genres = jsonObj._embedded.attractions[0].classifications[0];
                if (genres.subGenre && genres.subGenre.name) {
                    var subGenre = genres.subGenre.name;
                }
                if (genres.genre && genres.genre.name) {
                    var genre = genres.genre.name;
                }
                if (genres.segment && genres.segment.name) {
                    var segment = genres.segment.name;
                }
                if (genres.subType && genres.subType.name) {
                    var subType = genres.subType.name;
                }
                if (genres.type && genres.type.name) {
                    var type = genres.type.name;
                }
                if (subGenre != "" && subGenre != "undefined" && subGenre != "Undefined") {
                    var str_genre = subGenre;
                }
                if (genre != "" && genre != "undefined" && genre != "Undefined") {
                    str_genre = str_genre + " | " + genre;
                }
                if (segment != "" && segment != "undefined" && segment != "Undefined") {
                    str_genre = str_genre + " | " + segment;
                }
                if (subType != "" && subType != "undefined" && subType != "Undefined") {
                    str_genre = str_genre + " | " + subType;
                }
                if (type != "" && type != "undefined" && type != "Undefined") {
                    str_genre = str_genre + " | " + type;
                }
                html_text = html_text + "<li><h3> Genres </h3></li>";
                html_text = html_text + "<li>" + str_genre + "</li>";
            }
            // the fifth row: Price Ranges
            if (jsonObj.priceRanges) {
                if(jsonObj.priceRanges[0].min) {
                    var min = jsonObj.priceRanges[0].min;
                }
                else {
                    min = "";
                }
                if(jsonObj.priceRanges[0].max) {
                    var max = jsonObj.priceRanges[0].max;
                }
                else {
                    max = "";
                }
                if(max != "" && min != "") {
                    var price_range = min + " - " + max + " USD";
                }
                if(max == "" && min != "") {
                    price_range = min + " USD";
                }
                if(max != "" && min == "") {
                    price_range = max + " USD";
                }
                html_text = html_text + "<li><h3> Price Ranges </h3></li>";
                html_text = html_text + "<li>" + price_range + "</li>";
            }
            // the sixth row: Ticket Status
            if (jsonObj.dates && jsonObj.dates.status && jsonObj.dates.status.code) {
                var ticket_status = jsonObj.dates.status.code;
                html_text = html_text + "<li><h3> Ticket Status </h3></li>";
                html_text = html_text + "<li>" + ticket_status + "</li>";
            }
            // the seventh row: Buy Ticket At
            if (jsonObj.url) {
                var url_buyTicket = jsonObj.url;
                html_text = html_text + "<li><h3> Buy Ticket At: </h3></li>";
                html_text = html_text + "<li><a href='" + url_buyTicket + "'> Ticketmaster </a></li>";
            }
            html_text = html_text + "</ul></div>";
            // the eighth row: seat map, <img src='' />;
            if (jsonObj.seatmap && jsonObj.seatmap.staticUrl) {
                var seat_map = jsonObj.seatmap.staticUrl;
                // html_text = html_text + "<div style='width:500px;float:right;margin-right:150px;margin-top:20px'><img src='" + seat_map + "'" + "style='width:500px'/></div>";
                html_text = html_text + "<div style='width:500px;display:inline-block'><img src='" + seat_map + "'" + "style='width:500px'/></div>";
            }
            html_text = html_text + "</div>";
            // draw the two arrows graph
            var SRC_ARROW_UP = "http://csci571.com/hw/hw6/images/arrow_up.png";
            var SRC_ARROW_DOWN = "http://csci571.com/hw/hw6/images/arrow_down.png";
            html_text = html_text + "<br>";
            // the first arrow to show venue table
            html_text = html_text + "<div style='width:900px;margin-left:150px'>";
            html_text = html_text + "<p id='titleVenue' class='title'> click to show venue info </p>";
            html_text = html_text + "<center><img src='" + SRC_ARROW_DOWN + "' id='arrowVenue' class='arrow'></center>";
            // the divVenue to show venue table, style='display:none'
            html_text = html_text + "<div id='divVenue' style='display:none'>";
            html_text = html_text + generateVenueTable();
            html_text = html_text + "</div>";
            // the second arrow to show photo table
            html_text = html_text + "<p id='titlePhoto' class='title'> click to show venue photos</p>";
            html_text = html_text + "<center><img src='" + SRC_ARROW_DOWN + "' id='arrowPhoto' class='arrow'></center>";
            // the divVenue to show venue table, style='display:none'
            html_text = html_text + "<div id='divPhoto' style='display:none'>";
            html_text = html_text + generateVenuePhoto();
            html_text = html_text + "</div>";
            html_text = html_text + "</div>";
            // add this html_text to the div whose id is "events"
            document.getElementById("events").innerHTML = html_text;
            document.getElementById("events").style.display = "block";
            var showVenue = false;
            var showPhoto = false;
            // add click event for the img whose id is "arrowVenue"
            document.getElementById("arrowVenue").addEventListener("click", function() {
                if (showVenue == false && showPhoto == false) {
                    document.getElementById("divVenue").style.display = "block";
                    document.getElementById("titleVenue").innerHTML = "click to hide venue info";
                    document.getElementById("arrowVenue").src = SRC_ARROW_UP;
                    showVenue = true;
                }
                else if (showVenue == false && showPhoto == true) {
                    document.getElementById("divVenue").style.display = "block";
                    document.getElementById("titleVenue").innerHTML = "click to hide venue info";
                    document.getElementById("arrowVenue").src = SRC_ARROW_UP;
                    showVenue = true;
                    document.getElementById("divPhoto").style.display = "none";
                    document.getElementById("titlePhoto").innerHTML = "click to show venue photo";
                    document.getElementById("arrowPhoto").src = SRC_ARROW_DOWN;
                    showPhoto = false;
                }
                else if (showVenue == true && showPhoto == false) {
                    document.getElementById("divVenue").style.display = "none";
                    document.getElementById("titleVenue").innerHTML = "click to show venue info";
                    document.getElementById("arrowVenue").src = SRC_ARROW_DOWN;
                    showVenue = false;
                }
            });
            // add click event for the img whose id is "arrowPhoto"
            document.getElementById("arrowPhoto").addEventListener("click", function() {
                if (showVenue == false && showPhoto == false) {
                    document.getElementById("divPhoto").style.display = "block";
                    document.getElementById("titlePhoto").innerHTML = "click to hide venue photos";
                    document.getElementById("arrowPhoto").src = SRC_ARROW_UP;
                    showPhoto = true;
                }
                else if (showVenue == false && showPhoto == true) {
                    document.getElementById("divPhoto").style.display = "none";
                    document.getElementById("titlePhoto").innerHTML = "click to show venue photos";
                    document.getElementById("arrowPhoto").src = SRC_ARROW_DOWN;
                    showVenue = false;
                }
                else if (showVenue == true && showPhoto == false) {
                    document.getElementById("divVenue").style.display = "none";
                    document.getElementById("titleVenue").innerHTML = "click to show venue info";
                    document.getElementById("arrowVenue").src = SRC_ARROW_DOWN;
                    showVenue = false;
                    document.getElementById("divPhoto").style.display = "block";
                    document.getElementById("titlePhoto").innerHTML = "click to hide venue photos";
                    document.getElementById("arrowPhoto").src = SRC_ARROW_UP;
                    showPhoto = true;
                }
            });
        }

       /*
        * parse the json object "objTicketmasterAPIEvent" from PHP and show the Venue table with six columns:
        * Name, Map, Address, City, Postal Code, Upcoming Events.
        * @return html_text: a table that will be in the div created in the function "generateVenueInfoPhoto"
        */
        function generateVenueTable() {
            var jsonObj = objTicketmasterAPIVenue;
            if (jsonObj == null) {
                return;
            }
            var html_text = "";
            html_text = html_text + "<table border='1' style='width:900px'>";
            var venue;
            if (jsonObj._embedded && jsonObj._embedded.venues) {
                venue = jsonObj._embedded.venues[0];
                // the first row: Name
                if (venue.name) {
                    var venue_name = venue.name;
                    html_text = html_text + "<tr><td align='right'><h4> Name </h4></td>";
                    html_text = html_text + "<td align='center'>" + venue_name + "</td></tr>";
                }
                // the second row: Map
                if (venue.location) {
                    var latitude_destination2 = venue.location.latitude;
                    var longitude_destination2 = venue.location.longitude;
                    html_text = html_text + "<tr><td align='right'><h4> Map </h4></td>";
                    html_text = html_text + "<td>";
                    html_text = html_text + "<div id='mode2' class='mode2'>";
                    html_text = html_text + "<select size='3' id='chooseMode2' class='chooseMode'>";
                    html_text = html_text + "<option id='warking2' value='WALKING'> Walk there </option>";
                    html_text = html_text + "<option id='bicycling2' value='BICYCLING'> Bike there </option>";
                    html_text = html_text + "<option id='driving2' value='DRIVING'> Drive there </option>";
                    html_text = html_text + "</select></div>";
                    html_text = html_text + "<div id='googleMap2' class='googleMap2'></div>";
                    html_text = html_text + "</td></tr>";
                    var mapScript = document.createElement("script");
                    mapScript.type = "text/javascript";
                    var text_map = "function initMap2(){var directionsDisplay = new google.maps.DirectionsRenderer;";
                    text_map = text_map + "var directionsService = new google.maps.DirectionsService;";
                    text_map = text_map + "var core = {lat: " + latitude_destination2 + ", lng:" + longitude_destination2 + "};";
                    text_map = text_map + "var map2 = new google.maps.Map(document.getElementById('googleMap2'), {zoom: 14, center: core});";
                    text_map = text_map + "var marker2 = new google.maps.Marker({position: core, map: map2});";
                    text_map = text_map + "directionsDisplay.setMap(map2);";
                    text_map = text_map + "calculateAndDisplayRoute(directionsService, directionsDisplay);";
                    text_map = text_map + "document.getElementById('chooseMode2').addEventListener('change', function() { calculateAndDisplayRoute(directionsService, directionsDisplay);});}";
                    text_map = text_map + "function calculateAndDisplayRoute(directionsService, directionsDisplay) {";
                    text_map = text_map + "var selectedMode2 = document.getElementById('chooseMode2').value;";
                    text_map = text_map + "directionsService.route({ origin: {lat: " + latitude_start + ", lng: " + longitude_start + "}, ";
                    text_map = text_map + "destination: {lat: " + latitude_destination2 + ", lng: " + longitude_destination2 + "}, ";
                    text_map = text_map + "travelMode: google.maps.TravelMode[selectedMode2] }, function(response, status) {";
                    text_map = text_map + "if (status == 'OK') { directionsDisplay.setDirections(response); } ";
                    text_map = text_map + "else { window.alert('Directions request failed due to ' + status);}});}";
                    mapScript.innerHTML = text_map;
                    document.body.appendChild(mapScript);
                    // add script src
                    var srcScript = document.createElement("script");
                    srcScript.type = "text/javascript";
                    srcScript.src = "https://maps.googleapis.com/maps/api/js?key=" + KEY_GOOGLE_API + "&callback=initMap2";
                    document.body.appendChild(srcScript);
                }
                // the third row: Address
                if (venue.address) {
                    var venue_address = venue.address.line1;
                    html_text = html_text + "<tr><td align='right'><h4> Address </h4></td>";
                    html_text = html_text + "<td align='center'>" + venue_address + "</td></tr>";
                }
                // the fourth row: City
                if (venue.city) {
                    var venue_city = venue.city.name;
                    // alert("test" + venue_city);
                }
                if (venue.state) {
                    var venue_state = venue.state.stateCode;
                }
                if (venue_city != "" && venue_state != "") {
                    var str_city_state = venue_city + ", " + venue_state;
                }
                if (venue_city != "" && venue_state == "") {
                    str_city_state = venue_city;
                }
                if (venue_city == "" && venue_state != "") {
                    str_city_state = venue_state;
                }
                if (str_city_state != "") {
                    html_text = html_text + "<tr><td align='right'><h4> City </h4></td>";
                    html_text = html_text + "<td align='center'>" + str_city_state + "</td></tr>";
                }
                // the fifth row: Postal Code
                if (venue.postalCode) {
                    var venue_postcode = venue.postalCode;
                    html_text = html_text + "<tr><td align='right'><h4> Postal Code </h4></td>";
                    html_text = html_text + "<td align='center'>" + venue_postcode + "</td></tr>";
                }
                // the sixth row: Upcoming Events
                if (venue.url) {
                    var venue_url = venue.url;
                    html_text = html_text + "<tr><td align='right'><h4> Upcoming Events </h4></td>";
                    html_text = html_text + "<td align='center'>" + "<a href='" + venue.url + "'>" + venue_name + " Tickets </a></td></tr>";
                }
            }
            else {
                html_text = html_text + "<tr><td><center><h4> No Venue Info Found </h4></center></td></tr>";
            }
            html_text = html_text + "</table>";
            return html_text;
        }

       /*
        * parse the json object "objTicketmasterAPIEvent" from PHP and show one Venue photo
        * @return html_text: a table that will be in the div created in the function "generateVenueInfoPhoto"
        */
        function generateVenuePhoto() {
            var jsonObj = objTicketmasterAPIVenue;
            if (jsonObj == null) {
                return;
            }
            var html_text = "";
            html_text = html_text + "<table border='1' style='width:900px'>";
            if (jsonObj._embedded && jsonObj._embedded.venues && jsonObj._embedded.venues[0].images) {
                    var venue_images = jsonObj._embedded.venues[0].images;
                    // alert(venue_images.length);
                    for (var i = 0; i < venue_images.length; i++) {
                        var url_venue_photo = venue_images[i].url;
                        html_text = html_text + "<tr><td><img src='" + url_venue_photo + "'style='width:800px; margin-left:50px'/></td></tr>";
                    }
            }
            else {
                html_text = html_text + "<tr><td><center><h4> No Venue Photo Found </h4></center></td></tr>";
            }
            html_text = html_text + "</table>";
            return html_text;
        }

       /*
        * get the mouse position when clicking
        * @param event
        */
        function getMousePosition(event) {
            mouseX = event.pageX;
            mouseY = event.pageY;
        }
        document.addEventListener("click", getMousePosition);
        var showMap = false; 

       /*
        * given five elements: latitude_start, longitude_start, latitude_destination1, longitude_destination1, var KEY_GOOGLE_API
        * use googleAPI to generate google map
        * @param latitude_destination: the latitude of destination
        * @param longitude_destination: the longitude of destination
        */
        function generateGoogleMap(latitude_destination, longitude_destination) {
            document.getElementById("googleMap1").style.top = mouseY + "px";
            document.getElementById("googleMap1").style.left = mouseX + "px";
            document.getElementById("mode1").style.top = mouseY + "px";
            document.getElementById("mode1").style.left = mouseX + "px";
            latitude_destination1 = parseFloat(latitude_destination);
            longitude_destination1 = parseFloat(longitude_destination);
            // map is closed
            if (!showMap) {
                showMap = true;
                var srcScript = document.createElement("script");
                srcScript.type = "text/javascript";
                srcScript.src = "https://maps.googleapis.com/maps/api/js?key=" + KEY_GOOGLE_API + "&callback=initMap1";
                document.body.appendChild(srcScript);
                document.getElementById("walking1").style.selected = "false";
                document.getElementById("bicycling1").style.selected = "false";
                document.getElementById("driving1").style.selected = "false";
                document.getElementById("googleMap1").style.display = "block";
                document.getElementById("mode1").style.display = "block";
            }
            // map is open
            else {
                showMap = false;
                document.getElementById("walking1").style.selected = "false";
                document.getElementById("bicycling1").style.selected = "false";
                document.getElementById("driving1").style.selected = "false";
                document.getElementById("googleMap1").style.display = "none";
                document.getElementById("mode1").style.display = "none";
            }
        }

       /*
        * initialize google map when clicking the name of venue in the table
        * @param event
        */
        function initMap1() {
            var directionsDisplay = new google.maps.DirectionsRenderer;
            var directionsService = new google.maps.DirectionsService;
            var core = {lat: latitude_destination1, lng: longitude_destination1}; 
            var map1 = new google.maps.Map(document.getElementById("googleMap1"), {
                zoom: 12,
                center: core });
            var marker1 = new google.maps.Marker({position: core, map: map1});
            directionsDisplay.setMap(map1);
            calcRoute(directionsService, directionsDisplay);
            document.getElementById('chooseMode1').addEventListener('change', function() {
            calcRoute(directionsService, directionsDisplay);
            });
        }

       /*
        * get a direction route on google map if a user click on any travel mode: walking, bicycling, driving
        * @param directionsService
        * @param directionsDisplay
        */
        function calcRoute(directionsService, directionsDisplay) {
            var selectedMode1 = document.getElementById("chooseMode1").value;
            directionsService.route({
                origin: {lat: latitude_start, lng: longitude_start}, 
                destination: {lat: latitude_destination1, lng: longitude_destination1},
                travelMode: google.maps.TravelMode[selectedMode1]}, 
                function(response, status) {
                    if (status == 'OK') {
                        directionsDisplay.setDirections(response);
                    }
                    else { 
                        window.alert('Directions request failed due to ' + status);
                    }
                });
        }

       /*
        * hide the two divs whose ids are "googleMap" and "travelMode" when a user clicks the search button
        */
        function clearGoogleMap() {
            if (document.getElementById("googleMap1").style.display == "block") {
                document.getElementById("googleMap1").style.display = "none";
            }
            if (document.getElementById("mode1").style.display == "block") {
                document.getElementById("mode1").style.display = "none";
            }
        }

       /*
        * clear the form when a user clicks the clear button
        */
        function clearForm() {
            document.getElementById("text_keyword").value = "";
            document.getElementById("select_category").value = "Default";
            document.getElementById("text_distance").value = "";
            document.getElementById("radio_location1").checked = true;
            document.getElementById("input_location").value = "";
            document.getElementById("input_location").disabled = true;
            document.getElementById("events").innerHTML = "";
            document.getElementById("events").style.display = "none";
            document.getElementById("googleMap1").style.display = "none";
            document.getElementById("mode1").style.display = "none";
        }    
    </script>
    </body>
</html>

