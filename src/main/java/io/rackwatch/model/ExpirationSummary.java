package io.rackwatch.model;

import io.rackwatch.enums.AlertType;
import io.rackwatch.enums.DeviceType;

import java.time.LocalDate;

public record ExpirationSummary(String deviceName, DeviceType deviceType, LocalDate date, AlertType expirationType){}
