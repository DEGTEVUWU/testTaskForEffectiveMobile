package testtask.testtaskforeffectivemobile.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Slf4j
@Entity
@Table(name = "emails")
@EntityListeners(AuditingEntityListener.class)
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @CreatedDate
    private LocalDate createdAt;
}
