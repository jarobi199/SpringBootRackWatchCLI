package io.rackwatch.model;

public record RackSummary(int totalUnits, int usedUnits, int freeUnits, int totalWatts, int maxWatts, int deviceCount) {}