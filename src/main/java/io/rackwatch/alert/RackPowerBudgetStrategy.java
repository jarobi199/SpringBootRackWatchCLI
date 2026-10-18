package io.rackwatch.alert;

import io.rackwatch.enums.AlertSeverity;
import io.rackwatch.enums.AlertType;
import io.rackwatch.interfaces.AlertStrategy;
import io.rackwatch.model.Device;
import io.rackwatch.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RackPowerBudgetStrategy implements AlertStrategy {

    @Autowired
    private RackRepository rackRepository;
    private static final double THRESHOLD = 0.90;

    @Override
    public List<AlertResult> evaluate(List<Device> devices) {
        Map<String, Integer> wattsByRack = devices.stream().collect(
            Collectors.groupingBy(Device::getRackId, Collectors.summingInt(Device::getPowerWatts)));
        List<AlertResult> results = new ArrayList<>();

        wattsByRack.forEach((rackId, watts) -> rackRepository.findById(rackId).ifPresent(rack -> {
            int max = rack.getMaxPowerWatts();
            if (max <= 0 || watts <= max * THRESHOLD) return;
            AlertSeverity sev = watts > max ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;
            results.add(new AlertResult(rack.getName(), AlertType.POWER_BUDGET, sev,
                "Drawing " + watts + " of " + max + " W (" + Math.round(100.0 * watts / max) + "%)"));
        }));

        return results;
    }
}