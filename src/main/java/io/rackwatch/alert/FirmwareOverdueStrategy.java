package io.rackwatch.alert;

import io.rackwatch.enums.AlertSeverity;
import io.rackwatch.enums.AlertType;
import io.rackwatch.interfaces.AlertStrategy;
import io.rackwatch.model.Device;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class FirmwareOverdueStrategy implements AlertStrategy {

    @Override
    public List<AlertResult> evaluate(List<Device> devices) {
        List<AlertResult> results = new ArrayList<>();

        for (Device d : devices) {
            LocalDate basis = (d.getLastFirmwareUpdate() != null) ? d.getLastFirmwareUpdate() : d.getPurchaseDate();
            long daysSince = ChronoUnit.DAYS.between(basis, LocalDate.now());
            int  interval  = d.getFirmwareIntervalDays();   // override, else subtype strategy

            if (daysSince >= interval) {
                AlertSeverity sev = (daysSince > interval * 2L) ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;
                results.add(new AlertResult(d.getName(), AlertType.FIRMWARE_OVERDUE, sev, "Firmware " + (daysSince - interval) + " days overdue"));
            }
        }

        return results;
    }

}