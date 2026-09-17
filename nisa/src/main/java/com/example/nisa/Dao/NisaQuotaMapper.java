package com.example.nisa.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nisa.Entity.NisaQuota;

@Repository
public interface NisaQuotaMapper extends JpaRepository<NisaQuota, Long> {

    Optional<NisaQuota> findByUserEmail(String email);
}
