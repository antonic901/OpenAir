package openair.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Name can not be blank")
    @Size(min = 1, max = 30, message = "Name must be between 1 and 30")
    private String name;

    @Column
    @NotBlank(message = "Surname can not be blank")
    @Size(min = 1, max = 30, message = "Surname must be between 1 and 30")
    private String surname;

    @Column
    @Email(message = "Email must be valid")
    @NotNull(message = "Email can not be null")
    private String email;

    @Column
    @NotBlank(message = "Username can not be blank")
    @Size(min = 1, max = 30, message = "Username must be between 1 and 30")
    private String username;

    @JsonIgnore
    @Column
    @NotBlank(message = "Password can not be blank")
    private String password;

    @Column
    private String phone;

    @Column(name = "usertype", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Usertype can not be null")
    private UserType userType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}
