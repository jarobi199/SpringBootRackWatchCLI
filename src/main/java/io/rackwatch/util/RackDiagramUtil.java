package io.rackwatch.util;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import io.rackwatch.model.Device;
import io.rackwatch.model.PowerDevice;

import java.util.List;

public class RackDiagramUtil {

    public static final int MAX_RACK_SIZE = 60;

    public static void render(List<Device> devices) {
        String unitDisplay = null;
        String rackDisplay = null;

        System.out.println("| RACK ELEVATION |");
        Table rackElevationTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[yellow, bold]UNIT[/]",
                        "[yellow, bold]RACK[/]"
                );

        for(int unitCount = MAX_RACK_SIZE; unitCount >= 1; unitCount--) {
            unitDisplay = String.valueOf(unitCount);
            rackDisplay = "FREE";

            for(Device device : devices) {
                if ((unitCount >= device.getStartUnit()) && (unitCount <= (device.getStartUnit() + device.getUnitHeight() - 1))) {
                    unitDisplay = "[bg_yellow, black, bold]" + unitCount + "[/]";
                    rackDisplay = "[bg_yellow, black, bold]" + device.getName() + "[/]";
                    break;
                }
            }

            rackElevationTable.row(unitDisplay, rackDisplay);
        }

        rackElevationTable.render();
    }

    public static void main(String[] args) {
        Device device = new PowerDevice();
        device.setName("Power Device");
        device.setStartUnit(5);
        device.setUnitHeight(4);
        RackDiagramUtil.render(List.of(device));
    }

}
