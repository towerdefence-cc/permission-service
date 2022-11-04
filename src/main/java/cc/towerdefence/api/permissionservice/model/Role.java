package cc.towerdefence.api.permissionservice.model;

import cc.towerdefence.api.service.PermissionProto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "roles")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private String id;

    // 0 - Integer.MAX_VALUE
    private int priority;

    private @Nullable String displayPrefix;

    private @Nullable String displayName;

    private Set<String> permissions;

    public PermissionProto.RoleResponse toProto() {
        PermissionProto.RoleResponse.Builder builder = PermissionProto.RoleResponse.newBuilder()
                .setId(this.id)
                .setPriority(this.priority)
                .addAllPermissions(this.permissions);

        if (this.displayPrefix!= null) builder.setDisplayPrefix(this.displayPrefix);
        if (this.displayName != null) builder.setDisplayName(this.displayName);

        return builder.build();
    }
}
