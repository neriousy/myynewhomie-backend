package com.druzynav.models.user;

import com.druzynav.models.comments.Comments;
import com.druzynav.models.flat.Flat;
import com.druzynav.models.housingConfirmation.HousingConfirmation;
import com.druzynav.models.photo.Photo;
import com.druzynav.models.userCharacteristic.UserCharacteristic;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    @Column(name="email", unique = true)
    private String email;
    private String password;
    private Integer age;
    private String gender;
    private String phonenumber;
    private Boolean enabled;
    private String description;
    private Boolean still_looking;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Flat flat;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Photo photo;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<UserCharacteristic> characteristics;

    @OneToMany(mappedBy = "commentAuthor", cascade = CascadeType.ALL)
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "commentedUser", cascade = CascadeType.ALL)
    private List<Comments> receivedComments = new ArrayList<>();

    @Enumerated(EnumType.STRING )
    private Role role;

    @Enumerated(EnumType.STRING )
    private Status status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                // Add other properties here for meaningful representation
                '}';
    }
}
