package io.rackwatch.model;

import io.rackwatch.enums.MaintenanceType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "maintenance_logs")
public class MaintenanceLog {
    @Id
    private String id;
    private String userId;
    private String deviceId;
    private String deviceName;
    private String rackName;
    private MaintenanceType type;
    private LocalDate performedDate;
    private String notes;
    private String firmwareVersionBefore;
    private String firmwareVersionAfter;
    private LocalDate newExpiryDate;

    public MaintenanceLog(String userId, String deviceId, String deviceName, String rackName, MaintenanceType type, LocalDate performedDate,
                          String notes, String firmwareVersionBefore, String firmwareVersionAfter, LocalDate newExpiryDate) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.rackName = rackName;
        this.type = type;
        this.performedDate = performedDate;
        this.notes = notes;
        this.firmwareVersionBefore = firmwareVersionBefore;
        this.firmwareVersionAfter = firmwareVersionAfter;
        this.newExpiryDate = newExpiryDate;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRackName() {
        return rackName;
    }

    public void setRackName(String rackName) {
        this.rackName = rackName;
    }

    public MaintenanceType getType() {
        return type;
    }

    public void setType(MaintenanceType type) {
        this.type = type;
    }

    public LocalDate getPerformedDate() {
        return performedDate;
    }

    public void setPerformedDate(LocalDate performedDate) {
        this.performedDate = performedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFirmwareVersionBefore() {
        return firmwareVersionBefore;
    }

    public void setFirmwareVersionBefore(String firmwareVersionBefore) {
        this.firmwareVersionBefore = firmwareVersionBefore;
    }

    public String getFirmwareVersionAfter() {
        return firmwareVersionAfter;
    }

    public void setFirmwareVersionAfter(String firmwareVersionAfter) {
        this.firmwareVersionAfter = firmwareVersionAfter;
    }

    public LocalDate getNewExpiryDate() {
        return newExpiryDate;
    }

    public void setNewExpiryDate(LocalDate newExpiryDate) {
        this.newExpiryDate = newExpiryDate;
    }
}
