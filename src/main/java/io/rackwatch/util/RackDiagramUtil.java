package io.rackwatch.util;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import io.rackwatch.model.Device;
import io.rackwatch.model.Rack;

import java.util.List;

public class RackDiagramUtil {

    public static void render(List<Device> devices) {
        String unitDisplay = null;
        String rackDisplay = null;

        System.out.println("| RACK ELEVATION |");
        Table rackElevationTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[*blue, bold]UNIT[/]",
                        "[*blue, bold]RACK[/]"
                );

        for(int unitCount = Rack.MAX_RACK_SIZE; unitCount >= 1; unitCount--) {
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

}
