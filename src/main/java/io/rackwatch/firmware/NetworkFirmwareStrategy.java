package io.rackwatch.firmware;

import io.rackwatch.enums.NetworkRole;
import io.rackwatch.interfaces.FirmwareIntervalStrategy;
import io.rackwatch.model.NetworkDevice;

public class NetworkFirmwareStrategy implements FirmwareIntervalStrategy<NetworkDevice> {
    @Override
    public int intervalDays(NetworkDevice networkDevice) {
        return (NetworkRole.FIREWALL.equals(networkDevice.getNetworkRole()) || NetworkRole.ROUTER.equals(networkDevice.getNetworkRole())) ? 60 : 90;
    }
}
