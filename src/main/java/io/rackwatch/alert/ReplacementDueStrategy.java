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
public class ReplacementDueStrategy implements AlertStrategy {

    @Override
    public List<AlertResult> evaluate(List<Device> devices) {
        List<AlertResult> results = new ArrayList<>();

        for (Device d : devices) {
            LocalDate endOfLife = d.getEndOfLifeDate();          // null if no lifespan set
            if (endOfLife != null) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), endOfLife);
                if (daysLeft < 365) {
                    AlertSeverity sev = daysLeft < 0 ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;
                    String msg = daysLeft < 0
                            ? "Past expected end of life by " + (-daysLeft) + " days"
                            : "Reaches expected end of life in " + daysLeft + " days";
                    results.add(new AlertResult(d.getName(), AlertType.REPLACEMENT_DUE, sev, msg));
                }
            }
        }
        return results;
    }
}