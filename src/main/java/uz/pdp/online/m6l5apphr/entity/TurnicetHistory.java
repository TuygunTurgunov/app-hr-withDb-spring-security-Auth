package uz.pdp.online.m6l5apphr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.online.m6l5apphr.entity.template.AbsEntity;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@EntityListeners(value = AuditingEntityListener.class)
public class TurnicetHistory extends AbsEntity {


    @ManyToOne
    private Turnicet turnicet;

    @Column(nullable = false)
    private Boolean entered;

}
