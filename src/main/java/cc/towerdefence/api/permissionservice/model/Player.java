package cc.towerdefence.api.permissionservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "players")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    private UUID id;

    private List<String> roleIds;
}
