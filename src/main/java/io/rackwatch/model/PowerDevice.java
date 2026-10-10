package io.rackwatch.model;

import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.enums.DeviceType;
import io.rackwatch.enums.MaintenanceType;
import io.rackwatch.enums.PowerRole;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PowerDevice extends Device {
    private PowerRole powerRole;
    private int outletCount;
    private int capacityWatts;

    public PowerDevice() {

    }

    public PowerDevice(String userId, String rackId, String name, String manufacturer, String model, String serialNumber, Integer startUnit, Integer unitHeight, Integer powerWatts, DeviceStatus status,
                       LocalDate purchaseDate, LocalDate warrantExpiry, String firmwareVersion, LocalDate lastFirmwareUpdate, Integer firmwareIntervalOverrideDays, String licenseName,
                       LocalDate licenseExpiry, String notes, MaintenanceType scheduledMaintenanceType, Integer maintenanceIntervalDays, LocalDate lastScheduledMaintenanceDate,
                       BigDecimal purchasePrice, Integer expectedLifespanYears, PowerRole powerRole, int outletCount, int capacityWatts) {
        super(userId, rackId, name, manufacturer, model, serialNumber, startUnit, unitHeight, powerWatts, status, purchaseDate, warrantExpiry, firmwareVersion, lastFirmwareUpdate, firmwareIntervalOverrideDays,
                licenseName, licenseExpiry, notes, scheduledMaintenanceType, maintenanceIntervalDays, lastScheduledMaintenanceDate, purchasePrice, expectedLifespanYears);
        this.powerRole = powerRole;
        this.outletCount = outletCount;
        this.capacityWatts = capacityWatts;
    }

    public PowerRole getPowerRole() {
        return powerRole;
    }

    public void setPowerRole(PowerRole powerRole) {
        this.powerRole = powerRole;
    }

    public int getOutletCount() {
        return outletCount;
    }

    public void setOutletCount(int outletCount) {
        this.outletCount = outletCount;
    }

    public int getCapacityWatts() {
        return capacityWatts;
    }

    public void setCapacityWatts(int capacityWatts) {
        this.capacityWatts = capacityWatts;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.POWER;
    }

    @Override
    public int getFirmwareIntervalDays() {
        return 0;
    }
}
