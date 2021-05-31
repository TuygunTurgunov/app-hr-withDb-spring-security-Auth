package uz.pdp.online.m6l5apphr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.online.m6l5apphr.entity.template.AbsEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task extends AbsEntity {

    private String name;

    private String description;

    @ManyToOne
    private Status statusName;


    @Email
    private String workerEmail;

    private Timestamp deadlineDate;

    private String taskCode;

    private Boolean completed=false;

    private Boolean completedOnTime=false;

}
