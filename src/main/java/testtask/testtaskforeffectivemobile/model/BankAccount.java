package testtask.testtaskforeffectivemobile.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "bankAccounts")
@EntityListeners(AuditingEntityListener.class)
public class BankAccount implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private BigDecimal balance;

    private BigDecimal initialDeposit;

    @CreatedDate
    private LocalDate createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

}
