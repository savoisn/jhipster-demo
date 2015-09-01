package com.sg.startmeup.repository;

import com.sg.startmeup.domain.Idea;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Idea entity.
 */
public interface IdeaRepository extends JpaRepository<Idea,Long> {

    @Query("select idea from Idea idea where idea.user.login = ?#{principal.username}")
    List<Idea> findByUserIsCurrentUser();

}
