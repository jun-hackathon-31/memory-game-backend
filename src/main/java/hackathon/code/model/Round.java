package hackathon.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "rounds")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Round implements BaseEntity{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    private Integer complexity;

    @NotNull
    private Integer moves;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private User gamer;

    @CreatedDate
    private Date createdAt;
}
