package clovider.clovider_be.domain.recruit.controller;


import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.domain.recruit.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/lotteries")

public class RecruitController {

    private final RecruitService recruitService;
    private final RecruitRepository recruitRepository;

    @Autowired
    public RecruitController(RecruitService recruitService, RecruitRepository recruitRepository) {
        this.recruitService=recruitService;
        this.recruitRepository = recruitRepository;
    }
    @PostMapping("/plz/{recruitId}")
    public ResponseEntity<Recruit> plz(@PathVariable Long recruitId) {
        Optional<Recruit> recruit = recruitRepository.findById(recruitId);

        if (recruit.isPresent()) {
            return ResponseEntity.ok(recruit.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
