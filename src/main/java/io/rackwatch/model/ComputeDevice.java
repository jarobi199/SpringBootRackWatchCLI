package io.rackwatch.model;

import io.rackwatch.enums.ComputeRole;
import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.enums.DeviceType;
import io.rackwatch.enums.MaintenanceType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ComputeDevice extends Device {

    private ComputeRole computeRole;
    private int cpuCores;
    private int ramGb;
    private int storageTb;

    public ComputeDevice() {

    }

    public ComputeDevice(String userId, String name, String manufacturer, String model, String serialNumber, Integer startUnit, Integer unitHeight, Integer powerWatts,
                         DeviceStatus status, LocalDate purchaseDate, LocalDate warrantExpiry, String firmwareVersion, LocalDate lastFirmwareUpdate, Integer firmwareIntervalOverrideDays,
                         String licenseName, LocalDate licenseExpiry, String notes, MaintenanceType scheduledMaintenanceType, Integer maintenanceIntervalDays, LocalDate lastScheduledMaintenanceDate,
                         BigDecimal purchasePrice, Integer expectedLifespanYears, ComputeRole computeRole, int cpuCores, int ramGb, int storageTb) {
        super(userId, name, manufacturer, model, serialNumber, startUnit, unitHeight, powerWatts, status, purchaseDate, warrantExpiry, firmwareVersion, lastFirmwareUpdate,
                firmwareIntervalOverrideDays, licenseName, licenseExpiry, notes, scheduledMaintenanceType, maintenanceIntervalDays,
                lastScheduledMaintenanceDate, purchasePrice, expectedLifespanYears);
        this.computeRole = computeRole;
        this.cpuCores = cpuCores;
        this.ramGb = ramGb;
        this.storageTb = storageTb;
    }

    public ComputeRole getComputeRole() {
        return computeRole;
    }

    public void setComputeRole(ComputeRole computeRole) {
        this.computeRole = computeRole;
    }

    public int getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    public int getRamGb() {
        return ramGb;
    }

    public void setRamGb(int ramGb) {
        this.ramGb = ramGb;
    }

    public int getStorageTb() {
        return storageTb;
    }

    public void setStorageTb(int storageTb) {
        this.storageTb = storageTb;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.COMPUTE;
    }

    @Override
    public int getFirmwareIntervalDays() {
        return 0;
    }
}
