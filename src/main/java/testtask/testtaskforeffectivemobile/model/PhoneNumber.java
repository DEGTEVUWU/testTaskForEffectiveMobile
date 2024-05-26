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
@Entity
@Slf4j
@Table(name = "phoneNumbers")
@EntityListeners(AuditingEntityListener.class)
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    @CreatedDate
    private LocalDate createdAt;
}
