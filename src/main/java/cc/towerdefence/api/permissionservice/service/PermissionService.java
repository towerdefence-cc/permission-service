package cc.towerdefence.api.permissionservice.service;

import cc.towerdefence.api.permissionservice.model.Player;
import cc.towerdefence.api.permissionservice.model.Role;
import cc.towerdefence.api.permissionservice.repo.PlayerRepository;
import cc.towerdefence.api.permissionservice.repo.RoleRepository;
import cc.towerdefence.api.service.PermissionProto;
import cc.towerdefence.api.utils.spring.exception.ResourceAlreadyExistsException;
import cc.towerdefence.api.utils.spring.exception.ResourceNotFoundException;
import com.google.common.collect.Lists;
import io.grpc.Metadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PlayerRepository playerRepository;
    private final RoleRepository roleRepository;

    public List<PermissionProto.RoleResponse> getRoles() {
        return this.roleRepository.findAll()
                .stream()
                .map(Role::toProto)
                .toList();
    }

    public List<String> getPlayerRoleIds(UUID playerId) {
        Player player = this.playerRepository.findById(playerId).orElseGet(() -> {
            Player newPlayer = new Player(playerId, Lists.newArrayList("default"));
            return this.playerRepository.save(newPlayer);
        });

        return player.getRoleIds();
    }

    public PermissionProto.RoleResponse createRole(PermissionProto.RoleCreateRequest request) {
        if (this.roleRepository.existsById(request.getId()))
            throw new ResourceAlreadyExistsException("Role %s already exists".formatted(request.getId()));

        Role role = this.roleRepository.save(new Role(
                request.getId(),
                request.getPriority(),
                request.getDisplayPrefix(),
                request.getDisplayName(),
                new HashSet<>())
        );

        return role.toProto();
    }

    public PermissionProto.RoleResponse updateRole(PermissionProto.RoleUpdateRequest request) {
        Role role = this.roleRepository.findById(request.getId()).orElseThrow(() -> new ResourceNotFoundException("Role %s not found".formatted(request.getId())));

        if (request.hasPriority()) role.setPriority(request.getPriority());
        if (request.hasDisplayPrefix()) role.setDisplayPrefix(request.getDisplayPrefix());
        if (request.hasDisplayName()) role.setDisplayName(request.getDisplayName());

        for (String permission : request.getRemovedPermissionsList())
            role.getPermissions().remove(permission);

        for (String permission : request.getAddedPermissionsList())
            role.getPermissions().add(permission);

        return this.roleRepository.save(role).toProto();
    }

    public void addRoleToPlayer(UUID playerId, String roleId) {
        Player player = this.playerRepository.findById(playerId).orElseThrow(() -> new ResourceNotFoundException("Player %s not found".formatted(playerId)));

        if (player.getRoleIds().contains(roleId)) {
            Metadata metadata = new Metadata();
            metadata.put(Metadata.Key.of("custom_cause", Metadata.ASCII_STRING_MARSHALLER), "ALREADY_HAS_ROLE");

            throw new ResourceAlreadyExistsException("Player %s already has role %s".formatted(playerId, roleId))
                    .withMetadata(metadata);
        }

        player.getRoleIds().add(roleId);
        this.playerRepository.save(player);
    }

    public void removeRoleFromPlayer(UUID playerId, String roleId) {
        Player player = this.playerRepository.findById(playerId).orElseThrow(() -> new ResourceNotFoundException("Player %s not found".formatted(playerId)));

        if (player.getRoleIds().remove(roleId)) {
            this.playerRepository.save(player);
        } else {
            Metadata metadata = new Metadata();
            metadata.put(Metadata.Key.of("custom_cause", Metadata.ASCII_STRING_MARSHALLER), "NO_ROLE");

            throw new ResourceNotFoundException("Player %s does not have role %s".formatted(playerId, roleId))
                    .withMetadata(metadata);
        }
        player.getRoleIds().remove(roleId);
        this.playerRepository.save(player);
    }
}
