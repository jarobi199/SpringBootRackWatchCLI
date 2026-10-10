package io.rackwatch.model;

import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.enums.DeviceType;
import io.rackwatch.enums.MaintenanceType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "devices")
public abstract class Device {
    @Id
    private String id;
    private String userId;
    private String name;
    private String manufacturer;
    private String model;
    private String serialNumber;
    private Integer startUnit;
    private Integer unitHeight;
    private Integer powerWatts;
    private DeviceStatus status;
    private LocalDate purchaseDate;
    private LocalDate warrantExpiry;
    private String firmwareVersion;
    private LocalDate lastFirmwareUpdate;
    private Integer firmwareIntervalOverrideDays;
    private String licenseName;
    private LocalDate licenseExpiry;
    private String notes;
    private List<String> tags;
    private MaintenanceType scheduledMaintenanceType;
    private Integer maintenanceIntervalDays;
    private LocalDate lastScheduledMaintenanceDate;
    private BigDecimal purchasePrice;
    private Integer expectedLifespanYears;

    public Device() {
        //No argument constructor
    }

    public Device(String userId, String name, String manufacturer, String model, String serialNumber, Integer startUnit,
                  Integer unitHeight, Integer powerWatts, DeviceStatus status, LocalDate purchaseDate, LocalDate warrantExpiry,
                  String firmwareVersion, LocalDate lastFirmwareUpdate, Integer firmwareIntervalOverrideDays, String licenseName,
                  LocalDate licenseExpiry, String notes, MaintenanceType scheduledMaintenanceType, Integer maintenanceIntervalDays,
                  LocalDate lastScheduledMaintenanceDate, BigDecimal purchasePrice, Integer expectedLifespanYears) {
        this.userId = userId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.serialNumber = serialNumber;
        this.startUnit = startUnit;
        this.unitHeight = unitHeight;
        this.powerWatts = powerWatts;
        this.status = status;
        this.purchaseDate = purchaseDate;
        this.warrantExpiry = warrantExpiry;
        this.firmwareVersion = firmwareVersion;
        this.lastFirmwareUpdate = lastFirmwareUpdate;
        this.firmwareIntervalOverrideDays = firmwareIntervalOverrideDays;
        this.licenseName = licenseName;
        this.licenseExpiry = licenseExpiry;
        this.notes = notes;
        this.scheduledMaintenanceType = scheduledMaintenanceType;
        this.maintenanceIntervalDays = maintenanceIntervalDays;
        this.lastScheduledMaintenanceDate = lastScheduledMaintenanceDate;
        this.purchasePrice = purchasePrice;
        this.expectedLifespanYears = expectedLifespanYears;
        this.tags = new ArrayList<>();
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getStartUnit() {
        return startUnit;
    }

    public void setStartUnit(Integer startUnit) {
        this.startUnit = startUnit;
    }

    public Integer getUnitHeight() {
        return unitHeight;
    }

    public void setUnitHeight(Integer unitHeight) {
        this.unitHeight = unitHeight;
    }

    public Integer getPowerWatts() {
        return powerWatts;
    }

    public void setPowerWatts(Integer powerWatts) {
        this.powerWatts = powerWatts;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getWarrantExpiry() {
        return warrantExpiry;
    }

    public void setWarrantExpiry(LocalDate warrantExpiry) {
        this.warrantExpiry = warrantExpiry;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public LocalDate getLastFirmwareUpdate() {
        return lastFirmwareUpdate;
    }

    public void setLastFirmwareUpdate(LocalDate lastFirmwareUpdate) {
        this.lastFirmwareUpdate = lastFirmwareUpdate;
    }

    public Integer getFirmwareIntervalOverrideDays() {
        return firmwareIntervalOverrideDays;
    }

    public void setFirmwareIntervalOverrideDays(Integer firmwareIntervalOverrideDays) {
        this.firmwareIntervalOverrideDays = firmwareIntervalOverrideDays;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public LocalDate getLicenseExpiry() {
        return licenseExpiry;
    }

    public void setLicenseExpiry(LocalDate licenseExpiry) {
        this.licenseExpiry = licenseExpiry;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public MaintenanceType getScheduledMaintenanceType() {
        return scheduledMaintenanceType;
    }

    public void setScheduledMaintenanceType(MaintenanceType scheduledMaintenanceType) {
        this.scheduledMaintenanceType = scheduledMaintenanceType;
    }

    public Integer getMaintenanceIntervalDays() {
        return maintenanceIntervalDays;
    }

    public void setMaintenanceIntervalDays(Integer maintenanceIntervalDays) {
        this.maintenanceIntervalDays = maintenanceIntervalDays;
    }

    public LocalDate getLastScheduledMaintenanceDate() {
        return lastScheduledMaintenanceDate;
    }

    public void setLastScheduledMaintenanceDate(LocalDate lastScheduledMaintenanceDate) {
        this.lastScheduledMaintenanceDate = lastScheduledMaintenanceDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getExpectedLifespanYears() {
        return expectedLifespanYears;
    }

    public void setExpectedLifespanYears(Integer expectedLifespanYears) {
        this.expectedLifespanYears = expectedLifespanYears;
    }

    public LocalDate getEndOfLifeDate() {
        return expectedLifespanYears == null ? null : purchaseDate.plusYears(expectedLifespanYears);
    }

    public BigDecimal getBookValue() {
        if (purchasePrice == null || expectedLifespanYears == null) return null;
        double age = ChronoUnit.DAYS.between(purchaseDate, LocalDate.now()) / 365.25;
        double remaining = Math.max(0.0, 1.0 - age / expectedLifespanYears);
        return purchasePrice.multiply(BigDecimal.valueOf(remaining)).setScale(2, RoundingMode.HALF_UP);
    }

    public LocalDate getMaintenanceDuetDate() {
        LocalDate nextMaintenanceDate = null;
        if(maintenanceIntervalDays != null) {
            LocalDate basis = lastScheduledMaintenanceDate != null ? lastScheduledMaintenanceDate : purchaseDate;
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), basis.plusDays(maintenanceIntervalDays));
            nextMaintenanceDate = LocalDate.now().plusDays(daysLeft);
        }

        return nextMaintenanceDate;
    }

    public abstract DeviceType getDeviceType();

    public abstract int getFirmwareIntervalDays();
}

