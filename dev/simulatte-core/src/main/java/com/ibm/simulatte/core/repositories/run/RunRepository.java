package com.ibm.simulatte.core.repositories.run;

import com.ibm.simulatte.core.datamodels.run.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunRepository extends JpaRepository<Run, Integer> {

    Run findByUid(int uid);

    /*@Modifying
    @Transactional
    @Query("update Run r set r.action = :action where r.uid = :uid")
    void updateAction(@Param(value = "uid") int uid, @Param(value = "action") RunAction action);
     */

    //List<Run> findAllBySimulationUid();

    Run findTopByOrderByUidDesc();

    List<Run> findAllBySimulationUid(int simulationUid);

    boolean existsByUid(int runUid);

    Run findBySimulationUidAndUid(int simulationUid, int runUid);
}
