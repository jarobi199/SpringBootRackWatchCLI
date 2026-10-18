package io.rackwatch.model;

import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.enums.DeviceType;
import io.rackwatch.enums.MaintenanceType;
import io.rackwatch.enums.NetworkRole;
import io.rackwatch.firmware.NetworkFirmwareStrategy;
import io.rackwatch.interfaces.FirmwareIntervalStrategy;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class NetworkDevice extends Device {
    private NetworkRole networkRole;
    private int portCount;
    private int maxSpeedGbps;
    private boolean isManaged;
    @Transient
    private FirmwareIntervalStrategy<NetworkDevice> firmwareIntervalStrategy;

    public NetworkDevice() {
        this.firmwareIntervalStrategy = new NetworkFirmwareStrategy();
    }

    public NetworkDevice(String userId, String rackId, String name, String manufacturer, String model, String serialNumber, Integer startUnit, Integer unitHeight, Integer powerWatts,
                         DeviceStatus status, LocalDate purchaseDate, LocalDate warrantExpiry, String firmwareVersion, LocalDate lastFirmwareUpdate, Integer firmwareIntervalOverrideDays,
                         String licenseName, LocalDate licenseExpiry, String notes, List<String> tags, MaintenanceType scheduledMaintenanceType, Integer maintenanceIntervalDays, LocalDate lastScheduledMaintenanceDate,
                         BigDecimal purchasePrice, Integer expectedLifespanYears, NetworkRole networkRole, int portCount, int maxSpeedGbps, boolean isManaged) {
        super(userId, rackId, name, manufacturer, model, serialNumber, startUnit, unitHeight, powerWatts, status, purchaseDate, warrantExpiry, firmwareVersion, lastFirmwareUpdate,
                firmwareIntervalOverrideDays, licenseName, licenseExpiry, notes, tags, scheduledMaintenanceType, maintenanceIntervalDays,
                lastScheduledMaintenanceDate, purchasePrice, expectedLifespanYears);
        this.networkRole = networkRole;
        this.portCount = portCount;
        this.maxSpeedGbps = maxSpeedGbps;
        this.isManaged = isManaged;
    }

    public NetworkRole getNetworkRole() {
        return networkRole;
    }

    public void setNetworkRole(NetworkRole networkRole) {
        this.networkRole = networkRole;
    }

    public int getPortCount() {
        return portCount;
    }

    public void setPortCount(int portCount) {
        this.portCount = portCount;
    }

    public int getMaxSpeedGbps() {
        return maxSpeedGbps;
    }

    public void setMaxSpeedGbps(int maxSpeedGbps) {
        this.maxSpeedGbps = maxSpeedGbps;
    }

    public boolean isManaged() {
        return isManaged;
    }

    public void setManaged(boolean managed) {
        isManaged = managed;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.NETWORK;
    }

    @Override
    public int getFirmwareIntervalDays() {
        return firmwareIntervalStrategy.intervalDays(this);
    }
}
