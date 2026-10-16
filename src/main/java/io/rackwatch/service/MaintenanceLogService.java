package io.rackwatch.service;

import io.rackwatch.repository.MaintenanceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceLogService {
    @Autowired
    private MaintenanceLogRepository maintenanceLogRepository;

}
