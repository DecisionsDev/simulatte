package com.ibm.simulatte.core.repositories.run;

import com.ibm.simulatte.core.datamodels.run.RunReport;
import com.ibm.simulatte.core.datamodels.run.RunStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RunReportRepository extends JpaRepository<RunReport, Integer> {
    RunReport findByUid(int uid);

    @Modifying
    @Query("update RunReport ra set ra.status = :status where ra.uid = :uid")
    void updateStatus(@Param(value = "uid") int uid, @Param(value = "status") RunStatusType status);

    @Modifying
    @Transactional
    @Query("update RunReport ra set ra.duration = :duration where ra.uid = :uid")
    void updateDuration(@Param(value = "uid") int uid, @Param(value = "duration") int duration);

    @Modifying
    @Transactional
    @Query("update RunReport ra set ra.numberOfDecisions = :numberOfDecisions, ra.progress = :progress where ra.uid = :uid")
    void updateNumberOfDecisionsAndProgress(@Param(value = "uid") int uid, @Param(value = "numberOfDecisions") int numberOfDecisions, @Param(value = "progress") float progress);

    @Modifying
    @Transactional
    @Query("update RunReport ra set ra.status = :status, ra.numberOfDecisions = :numberOfDecisions, ra.progress = :progress where ra.uid = :uid")
    void updateStatusAndNumberOfDecisionsAndProgress(@Param(value = "uid") int uid, @Param(value = "status") RunStatusType status, @Param(value = "numberOfDecisions") int numberOfDecisions, @Param(value = "progress") float progress);
}
