package io.rackwatch.model;

import io.rackwatch.enums.MaintenanceType;

import java.time.LocalDate;
import java.util.Map;

public record MaintenanceSummary (int totalLogs, Map<MaintenanceType, Integer> logsByType, LocalDate lastMaintenanceDate) {}
