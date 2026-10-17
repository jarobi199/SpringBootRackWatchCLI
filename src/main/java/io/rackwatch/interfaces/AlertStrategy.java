package io.rackwatch.interfaces;

import io.rackwatch.alert.AlertResult;
import io.rackwatch.model.Device;

import java.util.List;

public interface AlertStrategy {
    List<AlertResult> evaluate(List<Device> devices);
}