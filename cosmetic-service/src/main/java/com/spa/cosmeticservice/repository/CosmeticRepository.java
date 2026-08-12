package com.spa.cosmeticservice.repository;

import com.spa.cosmeticservice.entity.Cosmetic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CosmeticRepository extends JpaRepository<Cosmetic, String> {
}
