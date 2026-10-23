package io.rackwatch.model;

import io.rackwatch.enums.DeviceType;
import io.rackwatch.enums.MaintenanceType;

import java.time.LocalDate;
import java.util.Map;

public record MaintenanceSummary (String deviceName, DeviceType deviceType, int totalLogs, Map<MaintenanceType, Integer> logsByType, LocalDate lastMaintenanceDate) {}
