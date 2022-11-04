package cc.towerdefence.api.permissionservice.model;


import cc.towerdefence.api.service.PermissionProto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionNode {
    private String node;
    private PermissionState state;

    public enum PermissionState {
        ALLOW(PermissionProto.PermissionNode.PermissionState.ALLOW),
        DENY(PermissionProto.PermissionNode.PermissionState.DENY);

        private final PermissionProto.PermissionNode.PermissionState protoState;

        PermissionState(PermissionProto.PermissionNode.PermissionState protoState) {
            this.protoState = protoState;
        }

        public PermissionProto.PermissionNode.PermissionState getProtoState() {
            return this.protoState;
        }
    }

    public static PermissionNode fromProto(PermissionProto.PermissionNode grpcNode) {
        PermissionState state = switch (grpcNode.getState()) {
            case ALLOW -> PermissionState.ALLOW;
            case DENY -> PermissionState.DENY;
            default -> throw new IllegalStateException("Unexpected value: " + grpcNode.getState());
        };
        return new PermissionNode(grpcNode.getNode(), state);
    }

    public static PermissionProto.PermissionNode toProto(PermissionNode node) {
        return PermissionProto.PermissionNode.newBuilder()
                .setNode(node.getNode())
                .setState(node.getState().getProtoState())
                .build();
    }
}
