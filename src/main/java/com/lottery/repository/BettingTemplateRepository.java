package com.lottery.repository;

import com.lottery.entity.BettingTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BettingTemplateRepository extends JpaRepository<BettingTemplate, Long> {
    List<BettingTemplate> findByStatusOrderByName(Integer status);
    List<BettingTemplate> findByBettingTypeOrderByName(String bettingType);
}
