package io.rackwatch.alert;

import io.rackwatch.interfaces.AlertStrategy;
import io.rackwatch.model.Device;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class AlertManager {
    private final List<AlertStrategy> strategies;

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public AlertManager(List<AlertStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<AlertResult> evaluate(List<Device> devices) {
        return strategies.stream()
                .flatMap(s -> s.evaluate(devices).stream())
                .sorted(Comparator.comparing(AlertResult::severity).reversed()
                        .thenComparing(AlertResult::alertType))   // CRITICAL first
                .collect(Collectors.toList());
    }

}

