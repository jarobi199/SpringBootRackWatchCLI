package io.rackwatch.service;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;

import io.rackwatch.alert.AlertManager;
import io.rackwatch.alert.AlertResult;
import io.rackwatch.enums.AlertType;
import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private DeviceService deviceService;

    public void displayAlerts(AlertType alertType) {
        List<Device> devices = deviceService.getAllDevicesByStatus(DeviceStatus.ACTIVE);
        List<AlertResult> results = alertManager.evaluate(devices);
        if(alertType != null) {
            results = results.stream().filter(result -> result.alertType().equals(alertType)).toList();
        }
        displayAlertResults(results);
    }

    private void displayAlertResults(List<AlertResult> alertResults) {
        if(alertResults.isEmpty()) {
            System.out.println("No alerts found.\n");
        }
        else
        {
            System.out.println("| ALERTS |");
            Table table = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]SUBJECT[/]",
                            "[*blue, bold]ALERT TYPE[/]",
                            "[*blue, bold]SEVERITY[/]",
                            "[*blue, bold]MESSAGE[/]"
                    );
            for (AlertResult alertResult : alertResults) {
                table.row(alertResult.subject(), alertResult.alertType().name(),  alertResult.severity().name(), alertResult.message());
            }
            table.render();
        }
    }
}
