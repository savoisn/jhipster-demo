package com.sg.startmeup.repository;

import com.sg.startmeup.domain.Estimation;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Estimation entity.
 */
public interface EstimationRepository extends JpaRepository<Estimation,Long> {

    @Query("select estimation from Estimation estimation where estimation.user.login = ?#{principal.username}")
    List<Estimation> findByUserIsCurrentUser();

}
