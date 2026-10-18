package io.rackwatch.interfaces;

import io.rackwatch.model.Device;

public interface FirmwareIntervalStrategy <T extends Device> {
    int intervalDays(T device);
}
