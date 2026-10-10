package io.rackwatch.repository;

import io.rackwatch.model.Rack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RackRepository extends MongoRepository<Rack, String> {
    List<Rack> findByUserId(String userId);
}
