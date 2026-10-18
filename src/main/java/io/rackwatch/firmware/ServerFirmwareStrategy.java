package io.rackwatch.firmware;

import io.rackwatch.interfaces.FirmwareIntervalStrategy;
import io.rackwatch.model.ComputeDevice;

public class ServerFirmwareStrategy implements FirmwareIntervalStrategy<ComputeDevice> {
    @Override
    public int intervalDays(ComputeDevice computeDevice) {
        return 180;
    }
}
