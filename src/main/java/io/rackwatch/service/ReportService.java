package io.rackwatch.service;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableConfiguration;
import io.github.kusoroadeolu.clique.configuration.TableType;
import io.rackwatch.authentication.SessionContext;
import io.rackwatch.enums.AlertType;
import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.model.Device;
import io.rackwatch.model.ExpirationSummary;
import io.rackwatch.model.Rack;
import io.rackwatch.model.RackSummary;
import io.rackwatch.repository.DeviceRepository;
import io.rackwatch.repository.RackRepository;
import io.rackwatch.util.BarChartUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
        List<ExpirationSummary> expirationSummaries = new ArrayList<>();
        deviceRepository.findByUserIdAndStatus(SessionContext.getUser().getId(), DeviceStatus.ACTIVE)
            .forEach(device -> {
                if (device.getLicenseExpiry() != null) {
                    expirationSummaries.add(new ExpirationSummary(device.getName(), device.getDeviceType(), device.getLicenseExpiry(), AlertType.LICENSE_RENEWAL));
                }
                if (device.getWarrantyExpiry() != null) {
                    expirationSummaries.add(new ExpirationSummary(device.getName(), device.getDeviceType(), device.getWarrantyExpiry(), AlertType.WARRANTY_EXPIRY));
                }
            });

        Map<Boolean, List<ExpirationSummary>> grouped = expirationSummaries.stream().sorted(Comparator.comparing(ExpirationSummary::date)).toList().stream()
                .collect(Collectors.groupingBy(expirationSummary -> expirationSummary.date().isBefore(LocalDate.now())));

        System.out.println("| UPCOMING EXPIRATIONS |");
        TableConfiguration config = TableConfiguration.builder()
                .nullReplacement("N/A")
                .build();

        Table upcomingExpirationsTable = Clique.table(TableType.BOX_DRAW, config)
                .headers(
                        "[*blue, bold]DEVICE NAME[/]",
                        "[*blue, bold]DEVICE TYPE[/]",
                        "[*blue, bold]DATE[/]",
                        "[*blue, bold]EXPIRY TYPE[/]"
                );

        grouped.get(true).forEach(expirationSummary -> {
            upcomingExpirationsTable.row(expirationSummary.deviceName(), expirationSummary.deviceType().name(), "[*red, bold]" + expirationSummary.date() + "[/]", "[*red, bold]" + expirationSummary.expirationType() .getDisplayName()+ "[/]");
        });
        grouped.get(false).forEach(expirationSummary -> {
            upcomingExpirationsTable.row(expirationSummary.deviceName(), expirationSummary.deviceType().name(), expirationSummary.date().toString(), expirationSummary.expirationType().getDisplayName());
        });

        upcomingExpirationsTable.render();
    }

    public void firmwareStatus() {
        List<Device> devices = deviceRepository.findByUserIdAndStatus(SessionContext.getUser().getId(), DeviceStatus.ACTIVE);

        Table firmwareStatusTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[*blue, bold]DEVICE NAME[/]",
                        "[*blue, bold]DEVICE TYPE[/]",
                        "[*blue, bold]CURRENT FIRMWARE VERSION[/]",
                        "[*blue, bold]FIRMWARE LAST UPDATED[/]",
                        "[*blue, bold]FIRMWARE INTERVAL DAYS[/]",
                        "[*blue, bold]DAYS UNTIL DUE/OVERDUE[/]"
                );

        devices.forEach(device -> {
            LocalDate lastUpdate = (device.getLastFirmwareUpdate() != null) ? device.getLastFirmwareUpdate() : device.getPurchaseDate();
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), lastUpdate.plusDays(device.getFirmwareIntervalDays()));
            String daysLeftDisplay = (daysLeft <  0) ? "[*red, bold]" + Math.abs(daysLeft) + "[/]" : String.valueOf(daysLeft);
            firmwareStatusTable.row(device.getName(), device.getDeviceType().name(), device.getFirmwareVersion(), device.getLastFirmwareUpdate().toString(),
                    String.valueOf(device.getFirmwareIntervalDays()), daysLeftDisplay);
        });
        firmwareStatusTable.render();
    }
}
