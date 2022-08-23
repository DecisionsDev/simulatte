package com.ibm.simulatte.core.repositories.decisionService;

import com.ibm.simulatte.core.datamodels.decisionService.DecisionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisionServiceRepository extends JpaRepository<DecisionService, Integer> {
}
