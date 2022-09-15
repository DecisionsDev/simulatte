package com.ibm.simulatte.core.services.data;

import com.ibm.simulatte.core.datamodels.data.DataSink;
import com.ibm.simulatte.core.datamodels.data.DataSource;
import com.ibm.simulatte.core.repositories.data.DataSinkRepository;
import com.ibm.simulatte.core.repositories.data.DataSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    final
    DataSourceRepository dataSourceRepository;
    final
    DataSinkRepository dataSinkRepository;

    public DataService(DataSourceRepository dataSourceRepository, DataSinkRepository dataSinkRepository) {
        this.dataSourceRepository = dataSourceRepository;
        this.dataSinkRepository = dataSinkRepository;
    }

    public DataSource getByDataSourceUid(int uid) {
        return dataSourceRepository.findByUid(uid);
    }

    public DataSink getByDataSinkUid(int uid) {
        return dataSinkRepository.findByUid(uid);
    }

    public DataSink findBySimulationUid(int simulationUid, String dataType) {
        return dataSinkRepository.findBySimulationUidAndType(simulationUid, dataType);
    }

    public DataSource getDataSource(int simulationUid, String dataType) {
        return dataSourceRepository.findBySimulationUidAndType(simulationUid, dataType);
    }

    public DataSink getDataSink(int simulationUid, String dataType) {
        return dataSinkRepository.findBySimulationUidAndType(simulationUid, dataType);
    }
}
