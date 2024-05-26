package testtask.testtaskforeffectivemobile.model;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import testtask.testtaskforeffectivemobile.model.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Slf4j
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements BaseEntity, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String surname;

    private LocalDate birthDate;

    @Column(unique = true)
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Set<Email> email;

    @Column(unique = true)
    private String login;

    @Column(unique = true)
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Set<PhoneNumber> phoneNumber;

    @Column(nullable = false)
    private String passwordDigest;

    @CreatedDate
    private LocalDate createdAt;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private BankAccount bankAccount;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return passwordDigest;
    }

    @Override
    public String getUsername() {
        return login;
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
