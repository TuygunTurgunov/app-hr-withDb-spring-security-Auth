package uz.pdp.online.m6l5apphr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)//table ga bo'layotgan o'zgarishlarni kuzatib turadi( configuratsiya ham qilish kere config package da)
public class Product {
    @Id
    @GeneratedValue
    private UUID id ;

    private String name;

    @CreatedBy//muxim annotatsiya
    private UUID createdBy; // Kim qo'shganligi ,userning id sini tipi

    @LastModifiedBy//muxim annotatsiya update uchun
    private UUID updatedBy; // Kim taxrirlaganligi

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;




}
