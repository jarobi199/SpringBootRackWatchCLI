package io.rackwatch.service;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import io.rackwatch.model.Device;
import io.rackwatch.model.Rack;
import io.rackwatch.repository.DeviceRepository;
import io.rackwatch.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private RackRepository rackRepository;

    public void filterAndList(int choice, String value) {
        List<Device> devices;
    }

    public void displayDevices(List<Device> devices) {
        System.out.println("| LIST OF DEVICES |");
        Table deviceTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[yellow, bold]NAME[/]",
                        "[yellow, bold]RACK[/]",
                        "[yellow, bold]TYPE[/]",
                        "[yellow, bold]MANUFACTURER[/]",
                        "[yellow, bold]MODEL[/]",
                        "[yellow, bold]UNITS[/]",
                        "[yellow, bold]POWER WATTS[/]",
                        "[yellow, bold]PURCHASE DATE[/]",
                        "[yellow, bold]STATUS[/]"
                );
        for (Device device : devices) {
            Rack rack = rackRepository.findById(device.getRackId()).orElse(null);
            deviceTable.row(device.getName(), (rack != null) ? rack.getName() : "N/A", device.getDeviceType().name(), device.getManufacturer(), device.getModel(), device.getUnitHeight().toString(),
                    device.getPowerWatts().toString(), device.getPurchaseDate().toString(), device.getStatus().name());
        }
        deviceTable.render();
    }
}
