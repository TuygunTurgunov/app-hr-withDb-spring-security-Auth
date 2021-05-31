package uz.pdp.online.m6l5apphr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.online.m6l5apphr.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Salary extends AbsEntity {

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private Double salaryAmount;

    @ManyToOne
    private Monthly monthly;

    @Column(nullable = false)
    private Integer year;










}
