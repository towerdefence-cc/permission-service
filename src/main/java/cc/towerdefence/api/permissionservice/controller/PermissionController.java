package cc.towerdefence.api.permissionservice.controller;

import cc.towerdefence.api.permissionservice.service.PermissionService;
import cc.towerdefence.api.service.PermissionProto;
import cc.towerdefence.api.service.PermissionServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@GrpcService
@Controller
@RequiredArgsConstructor
public class PermissionController extends PermissionServiceGrpc.PermissionServiceImplBase {
    private final PermissionService permissionService;

    @Override
    public void getRoles(Empty request, StreamObserver<PermissionProto.RolesResponse> responseObserver) {
        responseObserver.onNext(PermissionProto.RolesResponse.newBuilder()
                .addAllRoles(this.permissionService.getRoles())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPlayerRoles(PermissionProto.PlayerRequest request, StreamObserver<PermissionProto.PlayerRolesResponse> responseObserver) {
        responseObserver.onNext(PermissionProto.PlayerRolesResponse.newBuilder()
                .addAllRoleIds(this.permissionService.getPlayerRoleIds(UUID.fromString(request.getPlayerId())))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void createRole(PermissionProto.RoleCreateRequest request, StreamObserver<PermissionProto.RoleResponse> responseObserver) {
        responseObserver.onNext(this.permissionService.createRole(request));
        responseObserver.onCompleted();
    }

    @Override
    public void updateRole(PermissionProto.RoleUpdateRequest request, StreamObserver<PermissionProto.RoleResponse> responseObserver) {
        responseObserver.onNext(this.permissionService.updateRole(request));
        responseObserver.onCompleted();
    }

    @Override
    public void addRoleToPlayer(PermissionProto.AddRoleToPlayerRequest request, StreamObserver<Empty> responseObserver) {
        this.permissionService.addRoleToPlayer(UUID.fromString(request.getPlayerId()), request.getRoleId());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void removeRoleFromPlayer(PermissionProto.RemoveRoleFromPlayerRequest request, StreamObserver<Empty> responseObserver) {
        this.permissionService.removeRoleFromPlayer(UUID.fromString(request.getPlayerId()), request.getRoleId());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
