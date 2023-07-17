package org.example.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "shortName", typeClass = JsonBinaryType.class)
public class User {

    @EmbeddedId
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(name = "username", unique = true)
    private String username;

    //    @Type(type = "jsonb")
    @Type(type = "shortName")
    private String info;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
}