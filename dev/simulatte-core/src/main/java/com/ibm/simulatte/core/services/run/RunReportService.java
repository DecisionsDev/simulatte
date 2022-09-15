package com.ibm.simulatte.core.services.run;

import com.ibm.simulatte.core.datamodels.run.RunReport;
import com.ibm.simulatte.core.datamodels.run.RunStatusType;
import com.ibm.simulatte.core.repositories.run.RunReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RunReportService {

    private final RunReportRepository runReportRepository;

    public RunReportService(RunReportRepository runReportRepository) {
        this.runReportRepository = runReportRepository;
    }

    public RunReport getByRunReportUid(int uid) {
        return runReportRepository.findByUid(uid);
    }

    public void setByRunReportUid(RunReport runReport) {
        runReportRepository.save(runReport);
    }

    public void setNumberOfDecisionsAndProgress(int uid, int numberOfDecisions, float progress) {
        runReportRepository.updateNumberOfDecisionsAndProgress(uid, numberOfDecisions, progress);
    }

    public void setStatusAndNumberOfDecisionsAndProgress(int uid, RunStatusType status, int numberOfDecisions, float progress) {
        runReportRepository.updateStatusAndNumberOfDecisionsAndProgress(uid, status, numberOfDecisions, progress);
    }

    public void setStatus(int uid, RunStatusType runStatusType) {
        runReportRepository.updateStatus(uid, runStatusType);
    }

    public void setDuration(int uid, int duration) {
        runReportRepository.updateDuration(uid, duration);
    }
}
