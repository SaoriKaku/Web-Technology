# Web-Technology

Course Homework/Project for CSCI572 Search Engine

## Part 1: a simple sample to present Android Entertainment Event Search App

main page(tab:SEARCH,FAVORITE) <-> Search Results page <-> event page(tab:EVENT,ARTIST(S),VENUE,UPCOMING)  

1. Search an event based on keyword, category, distance(unit: mile), and the starting location.   
1.1 The main page loads when you open this search app, including two tabs "SEARCH" and "FAVORITE". The "SEARCH" tab shows first by default.  
1.2 When you input a keyword, autocomplete suggestions pop up in the dropdown list.  
1.3 Choose a category, including all, music, sports, arts & theatre, film and miscellaneous.  
1.4 The "Search Results" page shows when you click the button "search". The button "<-" leads you back to main page.
![image](https://github.com/SaoriKaku/Web-Technology/blob/master/images/EventSearch-1.jpg)

2. When you click one event in the "Search Results" page, it redirects to the event page. There are 4 tabs(EVENT, ARTIST(S), VENUE, UPCOMING) for a specific event page. The default tab is "EVENT".  
2.1 In the "EVENT" tab, there are important information about this event, such as artists, time, price and so on.  
2.2 In the "ARTIST(S)" tab, it indicates an introduction to artist(s) and some photos.  
2.3 In the "VENUE" tab(1), it lists details about the event location.  
2.4 In the "VENUE" tab(2), the event location is marked in a google map.
![image](https://github.com/SaoriKaku/Web-Technology/blob/master/images/EventSearch-2.jpg)

3. Sort the upcoming events and add events to your favorite list.  
3.1 In the "UPCOMING" tab(1), you can sort the upcoming events by default(no rule), event name, time, artist and type.  
3.2 In the "UPCOMING" tab(2), you specify the upcoming events sorted by ascending or descending.  
3.3 Go back to "Search Results" page by click the button "<-" in event page and choose some events as favorites there.  
3.4 Click the "FAVORITE" tab in main page, there are favorites events you added.
![image](https://github.com/SaoriKaku/Web-Technology/blob/master/images/EventSearch-3.jpg)

## Part 2: technical points

1. Client Side: designed an Android mobile app for users to search entertainment events (with geo-location, event keywords, event category, etc.), save events as favorites and post events on Twitter.
2. Server Side: utilized Node.js with framework Express.js and Promise to deal with nested asynchronous callback.
3. Developed with Android Studio and these following third-party libraries: Volley to load data with asynchronous HTTP request and Picasso to download and cache images.
4. Integrated these following APIs: Ticketmaster APIs for event search, Spotify APIs for artists information, Google Maps APIs for location, Songkick APIs for upcoming events and Twitter APIs for post.

**Thank you for reading!**
