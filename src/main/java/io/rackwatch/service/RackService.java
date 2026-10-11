package io.rackwatch.service;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import io.rackwatch.authentication.SessionContext;
import io.rackwatch.model.Device;
import io.rackwatch.model.Rack;
import io.rackwatch.repository.DeviceRepository;
import io.rackwatch.repository.RackRepository;
import io.rackwatch.util.RackDiagramUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RackService {
    @Autowired
    private RackRepository rackRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    public List<Rack> getAllRacks() {
        return rackRepository.findByUserId(SessionContext.getUser().getId());
    }

    public void listAllRacks() {
        List<Rack> racks = rackRepository.findByUserId(SessionContext.getUser().getId());
        if (racks.isEmpty()) {
            System.out.println("No racks found");
        }
        else
        {
            System.out.println("| LIST OF RACKS |");
            Table rackTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]NAME[/]",
                            "[*blue, bold]LOCATION[/]",
                            "[*blue, bold]TOTAL UNITS[/]",
                            "[*blue, bold]MAX POWER WATTS[/]"
                    );
            racks.forEach(rack -> rackTable.row(rack.getName(), rack.getLocation(), String.valueOf(rack.getTotalUnits()), String.valueOf(rack.getMaxPowerWatts())));
            rackTable.render();
        }
    }

    public void addRack(String rackName, String rackLocation, int rackTotalUnits, int rackMaxPowerWatts) {
        Rack rack = new Rack(SessionContext.getUser().getId(), rackName, rackLocation, rackTotalUnits, rackMaxPowerWatts);
        rackRepository.save(rack);
    }

    public void editRack(Rack rack, String rackName, String rackLocation, int rackMaxPowerWatts) {
        rack.setName(rackName);
        rack.setLocation(rackLocation);
        rack.setMaxPowerWatts(rackMaxPowerWatts);
        rackRepository.save(rack);
    }

    public void deleteRack(Rack rack) {
        rackRepository.delete(rack);
    }

    public boolean hasDependencies(Rack rack) {
        return !deviceRepository.findByRackId(rack.getId()).isEmpty();
    }

    public void viewRackElevation(Rack rack) {
        List<Device> devices = deviceRepository.findByRackId(rack.getId());
        RackDiagramUtil.render(devices);
    }
}
