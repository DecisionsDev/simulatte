package com.ibm.simulatte.core.repositories.analytic;

import com.ibm.simulatte.core.datamodels.analytic.KPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KpiRepository extends JpaRepository<KPI, Integer> {
}
