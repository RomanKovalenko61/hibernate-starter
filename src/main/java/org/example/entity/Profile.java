package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profile", schema = "public")
public class Profile {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne()
//    @JoinColumn(name = "user_id")
    @PrimaryKeyJoinColumn
    private User user;

    @Column(name = "street")
    private String street;

    @Column(name = "language")
    private String language;

    public void setUser(User user) {
        user.setProfile(this);
        this.user = user;
        this.id = user.getId();
    }
}
