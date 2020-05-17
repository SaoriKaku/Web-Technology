package com.example.suguo.eventsearch;

public class EventResult {
    private String eventId;
    private String segment;
    private String eventName;
    private String venueName;
    private String dateTime;
    private boolean inFavorites;

    public EventResult(String eventId, String segment, String eventName, String venueName, String dateTime, boolean inFavorites) {
        this.eventId = eventId;
        this.segment = segment;
        this.eventName = eventName;
        this.venueName = venueName;
        this.dateTime = dateTime;
        this.inFavorites = inFavorites;
    }

    public String getEventId() {
        return eventId;
    }

    public String getSegment() {
        return segment;
    }

    public String getEventName() {
        return eventName;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean getInFavorites() {
        return inFavorites;
    }

    public void setInFavorites(boolean isFavorite) {
        inFavorites = isFavorite;
    }
}
