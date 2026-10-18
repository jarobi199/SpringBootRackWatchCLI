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
public class WarrantyExpiryStrategy implements AlertStrategy {
    @Override
    public List<AlertResult> evaluate(List<Device> devices) {
        int lead = SessionContext.getUser().getAlertLeadDays();
        List<AlertResult> results = new ArrayList<>();

        for (Device d : devices) {
            if (d.getWarrantyExpiry() != null) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), d.getWarrantyExpiry());

                if (daysLeft < lead) {
                    AlertSeverity severity = daysLeft < 0 ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;
                    String msg = (daysLeft < 0)
                            ? "Warranty expired " + (-daysLeft) + " days ago"
                            : "Warranty expires in " + daysLeft + " days";
                    results.add(new AlertResult(d.getName(), AlertType.WARRANTY_EXPIRY, severity, msg));
                }
            }
        }

        return results;
    }

}