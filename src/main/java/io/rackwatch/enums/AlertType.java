package io.rackwatch.enums;

public enum AlertType {
    WARRANTY_EXPIRY ("Warranty Expiry"),
    FIRMWARE_OVERDUE ("Firmware Overdue"),
    LICENSE_RENEWAL ("License Renewal"),
    MAINTENANCE_DUE ("Maintenance Due"),
    POWER_BUDGET ("Power Budget"),
    REPLACEMENT_DUE ("Replacement Due");

    private final String displayName;

    AlertType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}