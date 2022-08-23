package com.ibm.simulatte.core.repositories.analytic;

import com.ibm.simulatte.core.datamodels.analytic.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Integer> {
}
