package com.example.nisa.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nisa.Entity.Asset;


//資産情報のDAO　JPAでデータベース操作を行う
@Repository
public interface AssetMapper extends JpaRepository<Asset, Long> {

    List<Asset> findByUserEmailOrderByIdDesc(String email);

    Optional<Asset> findByIdAndUserEmail(Long id, String email);
}
