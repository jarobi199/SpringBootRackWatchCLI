package io.rackwatch.service;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableConfiguration;
import io.github.kusoroadeolu.clique.configuration.TableType;
import io.rackwatch.authentication.SessionContext;
import io.rackwatch.enums.AlertType;
import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.enums.MaintenanceType;
import io.rackwatch.model.*;
import io.rackwatch.repository.DeviceRepository;
import io.rackwatch.repository.MaintenanceLogRepository;
import io.rackwatch.repository.RackRepository;
import io.rackwatch.util.BarChartUtil;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private RackRepository rackRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private MaintenanceLogRepository maintenanceLogRepository;

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

    public void costAndDepreciation() {
        List<Device> devices = deviceRepository.findByUserIdAndStatus(SessionContext.getUser().getId(), DeviceStatus.ACTIVE);
        List<Device> notTracked = new ArrayList<>();
        BigDecimal grandTotal = BigDecimal.ZERO;

        Map<String, List<Device>> devicesMap = new HashMap<>();
        devices.forEach(device -> {
            if((device.getPurchasePrice() != null) || (device.getExpectedLifespanYears() != null)) {
                if(devicesMap.containsKey(device.getRackId())) {
                    devicesMap.get(device.getRackId()).add(device);
                }
                else
                {
                    devicesMap.put(device.getRackId(), new ArrayList<>(List.of(device)));
                }
            }
            else
            {
                notTracked.add(device);
            }
        });

        for (Map.Entry<String, List<Device>> entry : devicesMap.entrySet()) {
            Rack rack = rackRepository.findById(entry.getKey()).orElse(null);
            if (rack != null) {
                System.out.println("| COST AND DEPRECIATION  |");
                System.out.println("RACK NAME: " + rack.getName());
                Table costAndDepreciationTable = Clique.table(TableType.BOX_DRAW)
                        .headers(
                                "[*blue, bold]PRICE[/]",
                                "[*blue, bold]AGE[/]",
                                "[*blue, bold]END OF LIFE DATE[/]",
                                "[*blue, bold]BOOK VALUE[/]",
                                "[*blue, bold]PERCENT DEPRECIATED[/]"
                        );
                entry.getValue().forEach(device -> {
                    String age = String.valueOf(ChronoUnit.YEARS.between(device.getPurchaseDate(), LocalDate.now()));
                    String endOfLifeDate = LocalDate.now().plusYears(device.getExpectedLifespanYears()).toString();
                    String bookValue = "$" + device.getBookValue();
                    String percentDepreciated = device.getPurchasePrice().subtract(device.getBookValue()).divide(device.getPurchasePrice(), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) + "%";
                    costAndDepreciationTable.row(InputHandler.formatAsMoney(device.getPurchasePrice().doubleValue()), age, endOfLifeDate, bookValue, percentDepreciated);
                });
                costAndDepreciationTable.render();
                BigDecimal subtotal = entry.getValue().stream().map(Device::getPurchasePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                grandTotal = grandTotal.add(subtotal);
                System.out.println("SUBTOTAL: $" + subtotal);
                System.out.println();
            }
        }

        System.out.println("------------------");
        System.out.println("GRAND TOTAL: $" + grandTotal);
        System.out.println("------------------");

        if (!notTracked.isEmpty()) {
            System.out.println("| NOT TRACKED |");
            Table notTrackedTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]DEVICE NAME[/]",
                            "[*blue, bold]DEVICE TYPE[/]"
                    );
            notTracked.forEach(device -> {
                notTrackedTable.row(device.getName(), device.getDeviceType().name());
            });
            notTrackedTable.render();
        }

    }

    public void maintenanceSummary(Device device) {
        List<Device> devices = null;
        List<MaintenanceSummary> maintenanceSummaryList = new ArrayList<>();

        if(device != null) {
            devices = List.of(device);
        }
        else
        {
            devices = deviceRepository.findByUserIdAndStatus(SessionContext.getUser().getId(), DeviceStatus.ACTIVE);
        }

        for (Device d : devices) {
            Map<MaintenanceType, Integer> logsByType = new HashMap<>();
            List<MaintenanceLog> maintenanceLogs = maintenanceLogRepository.findByDeviceId(d.getId()).stream().sorted(Comparator.comparing(MaintenanceLog::getPerformedDate).reversed()).toList();
            int totalMaintenanceLogs = maintenanceLogs.size();
            LocalDate lastMaintenanceDate = (!maintenanceLogs.isEmpty()) ? maintenanceLogs.getFirst().getPerformedDate() : null;
            for(MaintenanceType maintenanceType : MaintenanceType.values()) {
                long count = maintenanceLogs.stream().filter(m -> m.getType().equals(maintenanceType)).count();
                logsByType.put(maintenanceType, Math.toIntExact(count));
            }
            maintenanceSummaryList.add(new MaintenanceSummary(d.getName(), totalMaintenanceLogs, logsByType, lastMaintenanceDate));
        }

        for (MaintenanceSummary maintenanceSummary : maintenanceSummaryList) {
            BarChartUtil.Builder barChartBuilder = BarChartUtil.builder();
            barChartBuilder.title("Count Per Maintenance Type");
            System.out.println("| MAINTENANCE SUMMARY - " + maintenanceSummary.deviceName() + "|");
            System.out.println("Total logs: " + maintenanceSummary.totalLogs());
            System.out.println("Most recent maintenance date: " + maintenanceSummary.lastMaintenanceDate());
            maintenanceSummary.logsByType().forEach((key, value) -> {
                barChartBuilder.bar(key.name(), value, value + " maintenance logs");
            });
            barChartBuilder.render();
            System.out.println("-------");
        }
    }
}
