package com.ibm.simulatte.core.repositories.data;

import com.ibm.simulatte.core.datamodels.data.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DataSourceRepository extends JpaRepository<DataSource, Integer> {
    DataSource findBySimulationUid(int simulationUid);

    DataSource findBySimulationUidAndType(int simulationUid, String dataType);

    DataSource findByUid(int uid);
}
