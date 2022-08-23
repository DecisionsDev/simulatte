package com.ibm.simulatte.core.services.data;

import com.ibm.simulatte.core.datamodels.data.DataSource;
import com.ibm.simulatte.core.repositories.data.DataSourceRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@NoArgsConstructor
public class DataSourceService implements Serializable {

    @Autowired
    DataSourceRepository dataSourceRepository;

    public DataSource findBySimulationUid(int simulationUid) {
        return dataSourceRepository.findBySimulationUid(simulationUid);
    }


    public DataSource getByUid(int dataSourceUid) {
        return dataSourceRepository.findByUid(dataSourceUid);
    }
}
