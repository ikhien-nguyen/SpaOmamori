package com.spa.profileservice.controller;

import com.spa.profileservice.entity.Therapist;
import com.spa.profileservice.repository.TherapistRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/therapists")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TherapistController {

    TherapistRepository therapistRepository;

    @GetMapping("/{id}")
    public Therapist getById(@PathVariable String id) {

        return therapistRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Therapist không tồn tại"));
    }
}