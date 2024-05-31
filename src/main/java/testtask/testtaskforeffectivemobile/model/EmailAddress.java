package testtask.testtaskforeffectivemobile.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDate;

@Getter
@Setter
@Slf4j
@Entity
@Table(name = "emails")
@EntityListeners(AuditingEntityListener.class)
public class EmailAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @CreatedDate
    private LocalDate createdAt;
}
