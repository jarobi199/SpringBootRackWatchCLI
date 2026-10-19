package io.rackwatch.service;

import io.rackwatch.authentication.SessionContext;
import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.model.Device;
import io.rackwatch.model.Rack;
import io.rackwatch.model.RackSummary;
import io.rackwatch.repository.DeviceRepository;
import io.rackwatch.repository.RackRepository;
import io.rackwatch.util.BarChartUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private RackRepository rackRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    public void generateRackUtilization() {
        List<Rack> racks = rackRepository.findByUserId(SessionContext.getUser().getId());
        if (!racks.isEmpty()) {
            System.out.println("| RACK SUMMARIES |");

            System.out.println();
            for (Rack rack : racks) {
                int totalUnitsUsed = deviceRepository.findByRackId(rack.getId()).stream().mapToInt(Device::getUnitHeight).sum();
                int totalUnitsFree = rack.getTotalUnits() - totalUnitsUsed;
                int totalWattsUsed = deviceRepository.findByRackId(rack.getId()).stream().mapToInt(Device::getPowerWatts).sum();
                int deviceCount = deviceRepository.findByRackId(rack.getId()).size();
                RackSummary rackSummary = new RackSummary(rack.getTotalUnits(), totalUnitsUsed, totalUnitsFree, totalWattsUsed, rack.getMaxPowerWatts(), deviceCount);

                System.out.println("Rack Summary: " + rack.getName());
                System.out.println("------------------------");
                BarChartUtil.builder().printRatioBar("Units Used/Units Free", rackSummary.usedUnits(), rackSummary.freeUnits());
                BarChartUtil.builder().printRatioBar("Watts Used/Watts Free", rackSummary.totalWatts(), rackSummary.maxWatts() -rackSummary.totalWatts());
                System.out.println("TOTAL DEVICES: " + rackSummary.deviceCount());
                System.out.println();
            }
        }

    }

    public void generateUpcomingExpirations() {
        Map<Boolean, List<Device>> devicesMap = deviceRepository.findByUserIdAndStatus(SessionContext.getUser().getId(), DeviceStatus.ACTIVE).stream()
                .sorted(Comparator.comparing(Device::getLicenseExpiry).thenComparing(Device::getWarrantyExpiry))
                .collect(Collectors.partitioningBy(
                        device -> device.isExpired(device.getLicenseExpiry()) || device.isExpired(device.getWarrantyExpiry())));

        System.out.println("UPCOMING EXPIRATIONS: " + devicesMap.get(true));
        System.out.println("UNEXPIRED DEVICES: " + devicesMap.get(false));
    }
}
