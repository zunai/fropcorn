package com.sourcey.materiallogindemo;

/**
 * Created by Bhawana on 9/18/2016.
 */
public class Event {

    private String EventId;
    private String EventKey;
    private String BarOperatorUserId;
    private String Name;
    private String DateTimeStartUtc;
    private String DateTimeEndUtc;
    private String CloudinaryPublicImageId;

    public Event(){}

    public Event(String id, String name, String eventkey, String baroperator ,String starttime,String endtime,String cloudinary) {
        this.EventId = id;
        this.Name = name;
        this.EventKey = eventkey;
        this.BarOperatorUserId = baroperator;
        this.DateTimeStartUtc = starttime;
        this.DateTimeEndUtc = endtime;
        this.CloudinaryPublicImageId = cloudinary;

    }

    public String getId() {
        return EventId;
    }

    public void setId(String id) {
        this.EventId = id;
    }public String getName() {
        return Name;
    }

    public void setName(String id) {
        this.Name = id;
    }
    public String getKey() {
        return EventKey;
    }

    public void setKey(String id) {
        this.EventKey = id;
    }
    public String getBarOperator() {
        return BarOperatorUserId;
    }

    public void setBarOperator(String id) {
        this.BarOperatorUserId = id;
    }
    public String getStarttime() {
        return DateTimeStartUtc;
    }

    public void setStarttime(String id) {
        this.DateTimeStartUtc = id;
    }
    public String getEndtime() {
        return DateTimeEndUtc;
    }

    public void setEndtime(String id) {
        this.DateTimeEndUtc = id;
    }
    public String getCloudinary() {
        return CloudinaryPublicImageId;
    }

    public void setCloudinary(String id) {
        this.CloudinaryPublicImageId = id;
    }

}