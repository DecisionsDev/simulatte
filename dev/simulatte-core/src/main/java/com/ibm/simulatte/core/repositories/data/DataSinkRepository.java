package com.ibm.simulatte.core.repositories.data;

import com.ibm.simulatte.core.datamodels.data.DataSink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSinkRepository extends JpaRepository<DataSink, Integer> {
    DataSink findBySimulationUid(int simulationUid);

    DataSink findBySimulationUidAndType(int simulationUid, String dataType);

    DataSink findByUid(int uid);
}
