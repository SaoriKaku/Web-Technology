package com.example.suguo.eventsearch;

public class UpcomingEvent {

    private String displayName;
    private String uri;
    private String artist;
    private String dateTime;
    private Long timeStamp;
    private String type;

    public UpcomingEvent(String displayName, String uri, String artist, String dateTime, Long timeStamp, String type) {
        this.displayName = displayName;
        this.uri = uri;
        this.artist = artist;
        this.dateTime = dateTime;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUri() {
        return uri;
    }

    public String getArtist() {
        return artist;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getType() {
        return type;
    }

}
