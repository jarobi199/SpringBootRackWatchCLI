package io.rackwatch.alert;

import org.springframework.stereotype.Component;


@Component
public class AlertManager {
    //TODO: Uncomment code here
/*    private final List<AlertStrategy> strategies;

    public AlertManager(List<AlertStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<AlertResult> evaluate(List<Device> devices) {
        return strategies.stream()
            .flatMap(s -> s.evaluate(devices).stream())
            .sorted(Comparator.comparing(AlertResult::severity).reversed()
                .thenComparing(AlertResult::alertType))   // CRITICAL first
            .collect(Collectors.toList());
    }*/
}