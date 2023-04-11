package pl.alekaug.quizplatform.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import static pl.alekaug.quizplatform.constants.DatabaseConstants.*;

@Entity
@Table( name = RESOURCES_TABLE )
@Data public class Resource {
    @Id @GeneratedValue
    @Column( name = RESOURCE_ID_COLUMN, nullable = false )
    @JsonIgnore
    private long id;

    @Column( name = RESOURCE_URL_COLUMN, nullable = false )
    private String url;

    @Column( name = RESOURCE_TYPE_COLUMN, nullable = false )
    @Enumerated( EnumType.ORDINAL )
    private Type type;

    public enum Type {
        AUDIO, VIDEO, IMAGE
    }
}
