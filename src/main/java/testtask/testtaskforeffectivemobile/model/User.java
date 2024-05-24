package testtask.testtaskforeffectivemobile.model;

import jakarta.persistence.*;
import testtask.testtaskforeffectivemobile.model.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String surname;

    @Column(unique = true)
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Email> email;

    @Column(unique = true)
    private String login;

    @Column(unique = true)
    @OneToMany(cascade = CascadeType.ALL)
    private Set<PhoneNumber> phoneNumber;

    @Column(nullable = false)
    private String passwordDigest;

    @CreatedDate
    private LocalDate createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    private BankAccount bankAccount;


}
