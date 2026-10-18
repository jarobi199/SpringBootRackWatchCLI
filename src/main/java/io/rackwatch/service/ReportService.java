package io.rackwatch.service;

import io.rackwatch.authentication.SessionContext;
import io.rackwatch.model.Device;
import io.rackwatch.model.Rack;
import io.rackwatch.repository.DeviceRepository;
import io.rackwatch.repository.RackRepository;
import io.rackwatch.util.BarChartUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReportService {
    @Autowired
    private RackRepository rackRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    public void generateRackUtilization() {
        List<Rack> racks = rackRepository.findByUserId(SessionContext.getUser().getId());
        if (!racks.isEmpty()) {
            BarChartUtil.Builder barChartBuilder = new BarChartUtil.Builder();
            System.out.println("| RACK SUMMARIES |");

            System.out.println();
            for (Rack rack : racks) {
                System.out.println("Rack Summary: " + rack.getName());
                System.out.println("------------------------");

                int totalUnitsUsed = deviceRepository.findByRackId(rack.getId()).stream().mapToInt(Device::getUnitHeight).sum();
                int totalUnitsFree = rack.getTotalUnits() - totalUnitsUsed;

                int totalWattsUsed = deviceRepository.findByRackId(rack.getId()).stream().mapToInt(Device::getPowerWatts).sum();
                int totalWattsFree = rack.getMaxPowerWatts() - totalUnitsUsed;

                int deviceCount = deviceRepository.findByRackId(rack.getId()).size();

            }
        }

    }
}
