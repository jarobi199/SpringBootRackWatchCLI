package io.rackwatch.service;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import io.rackwatch.authentication.SessionContext;
import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.enums.DeviceType;
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

    public boolean areDevicesAvailable() {
        return deviceRepository.count() > 0;
    }

    public void filterAndList(int choice, String value) {
        List<Device> devices = deviceRepository.findByUserId(SessionContext.getUser().getId());
        List<Device> filteredDevices = switch (choice) {
            case 2 -> devices.stream()
                    .filter(d -> d.getRackId().equalsIgnoreCase(value))
                    .toList();
            case 3 -> devices.stream()
                    .filter(d -> d.getDeviceType().equals(DeviceType.valueOf(value)))
                    .toList();
            case 4 -> devices.stream()
                    .filter(d -> d.getTags().stream().anyMatch(tag -> tag.equals(value)))
                    .toList();
            case 5 -> devices.stream()
                    .filter(d -> d.getStatus().equals(DeviceStatus.valueOf(value)))
                    .toList();
            default -> devices;
        };

        displayDevices(filteredDevices);
    }

    public void displayDevices(List<Device> devices) {
        System.out.println("| LIST OF DEVICES |");
        Table deviceTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[*blue, bold]NAME[/]",
                        "[*blue, bold]RACK[/]",
                        "[*blue, bold]TYPE[/]",
                        "[*blue, bold]MANUFACTURER[/]",
                        "[*blue, bold]MODEL[/]",
                        "[*blue, bold]UNITS[/]",
                        "[*blue, bold]POWER WATTS[/]",
                        "[*blue, bold]PURCHASE DATE[/]",
                        "[*blue, bold]STATUS[/]"
                );
        for (Device device : devices) {
            Rack rack = rackRepository.findById(device.getRackId()).orElse(null);
            deviceTable.row(device.getName(), (rack != null) ? rack.getName() : "N/A", device.getDeviceType().name(), device.getManufacturer(), device.getModel(), device.getUnitHeight().toString(),
                    device.getPowerWatts().toString(), device.getPurchaseDate().toString(), device.getStatus().name());
        }
        deviceTable.render();
    }

    public boolean validatePlacement(int startUnit, int deviceHeight, Rack rack) {
        boolean valid = true;
        List<Device> devices = deviceRepository.findByRackId(rack.getId()).stream().filter(d -> DeviceStatus.ACTIVE.equals(d.getStatus())).toList();
        for(Device device : devices) {
            if ((startUnit < 1) || ((startUnit + deviceHeight - 1) > rack.getTotalUnits()) || (startUnit >= device.getStartUnit()) || (startUnit + deviceHeight - 1) <= (device.getStartUnit() + device.getUnitHeight() - 1)) {
                valid = false;
                break;
            }
        }

        return valid;

    }
}
