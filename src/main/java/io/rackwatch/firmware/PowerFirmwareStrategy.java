package io.rackwatch.firmware;

import io.rackwatch.interfaces.FirmwareIntervalStrategy;
import io.rackwatch.model.PowerDevice;

public class PowerFirmwareStrategy implements FirmwareIntervalStrategy<PowerDevice> {
    @Override
    public int intervalDays(PowerDevice powerDevice) {
        return 365;
    }
}
