package io.rackwatch.alert;

import io.rackwatch.authentication.SessionContext;
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
public class MaintenanceDueStrategy implements AlertStrategy {

    @Override
    public List<AlertResult> evaluate(List<Device> devices) {
        int lead = SessionContext.getUser().getAlertLeadDays();
        List<AlertResult> results = new ArrayList<>();

        for (Device d : devices) {
            Integer interval = d.getMaintenanceIntervalDays();
            if (interval != null) {
                LocalDate basis = d.getLastScheduledMaintenanceDate() != null
                        ? d.getLastScheduledMaintenanceDate() : d.getPurchaseDate();
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), basis.plusDays(interval));
                if (daysLeft < Math.min(lead, interval / 2)) {
                    long elapsed = ChronoUnit.DAYS.between(basis, LocalDate.now());
                    AlertSeverity sev = elapsed > interval * 2L ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;
                    String task = d.getScheduledMaintenanceType().name();
                    String msg = daysLeft < 0
                            ? task + " overdue by " + (-daysLeft) + " days"
                            : task + " due in " + daysLeft + " days";
                    results.add(new AlertResult(d.getName(), AlertType.MAINTENANCE_DUE, sev, msg));

                }
            }
        }
        return results;
    }

}