package io.rackwatch.alert;

import io.rackwatch.enums.AlertSeverity;
import io.rackwatch.enums.AlertType;

public record AlertResult(String subject, AlertType alertType, AlertSeverity severity, String message) {}
