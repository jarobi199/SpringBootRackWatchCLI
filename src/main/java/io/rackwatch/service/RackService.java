package io.rackwatch.service;

import io.rackwatch.authentication.SessionContext;
import io.rackwatch.model.Rack;
import io.rackwatch.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RackService {
    @Autowired
    private RackRepository rackRepository;

    public List<Rack> getAllRacks() {
        return rackRepository.findByUserId(SessionContext.getUser().getId());
    }
}
