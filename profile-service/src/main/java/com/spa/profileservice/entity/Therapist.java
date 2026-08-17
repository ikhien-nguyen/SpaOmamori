package com.spa.profileservice.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "therapist",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_therapist_user",
                        columnNames = "user_id"
                )
        }
)
public class Therapist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    /**
     * Logical ID tới User Service.
     * Không phải FK database.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    String userId;

    /**
     * Có thể bổ sung thông tin nghiệp vụ Therapist sau.
     */
    @Column(name = "specialization")
    String specialization;

    @Column(name = "certificate")
    String certificate;

    @Column(name = "experience")
    String experience;

    @Builder.Default
    @Column(name = "active", nullable = false)
    boolean active = true;
}