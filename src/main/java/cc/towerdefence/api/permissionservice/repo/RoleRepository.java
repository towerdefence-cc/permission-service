package cc.towerdefence.api.permissionservice.repo;

import cc.towerdefence.api.permissionservice.model.Player;
import cc.towerdefence.api.permissionservice.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
}
