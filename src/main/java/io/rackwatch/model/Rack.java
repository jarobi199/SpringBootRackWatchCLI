package io.rackwatch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "racks")
public class Rack {
    @Id
    private String id;
    private String userId;
    private String name;
    private String location;
    private int totalUnits;
    private int maxPowerWatts ;

    public Rack() {
        //No argument constructor
    }

    public Rack(String userId, String name, String location, int totalUnits, int maxPowerWatts) {
        this.userId = userId;
        this.name = name;
        this.location = location;
        this.totalUnits = totalUnits;
        this.maxPowerWatts = maxPowerWatts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
    }

    public int getMaxPowerWatts() {
        return maxPowerWatts;
    }

    public void setMaxPowerWatts(int maxPowerWatts) {
        this.maxPowerWatts = maxPowerWatts;
    }
}
