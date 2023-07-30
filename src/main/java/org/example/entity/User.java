package org.example.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQuery(name = "findUserByName", query = "select u from User u " +
        "left join u.company c " +
        "where u.personalInfo.firstname = :firstname and c.name = :companyName " +
        "order by u.personalInfo.lastname desc ")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "profile", "userChats"})
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "shortName", typeClass = JsonBinaryType.class)
//@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class User implements Comparable<User>, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(name = "username", unique = true, columnDefinition = "")
    private String username;

    //    @Type(type = "jsonb")
    @Type(type = "shortName")
    private String info;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Profile profile;

    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }
}