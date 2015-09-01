package com.sg.startmeup.repository;

import com.sg.startmeup.domain.Pledge;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pledge entity.
 */
public interface PledgeRepository extends JpaRepository<Pledge,Long> {

    @Query("select pledge from Pledge pledge where pledge.user.login = ?#{principal.username}")
    List<Pledge> findByUserIsCurrentUser();

}
