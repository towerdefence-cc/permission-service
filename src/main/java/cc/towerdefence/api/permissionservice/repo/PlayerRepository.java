package cc.towerdefence.api.permissionservice.repo;

import cc.towerdefence.api.permissionservice.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerRepository extends MongoRepository<Player, UUID> {

}
