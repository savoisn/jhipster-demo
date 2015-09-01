package com.sg.startmeup.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sg.startmeup.domain.Idea;
import com.sg.startmeup.repository.IdeaRepository;
import com.sg.startmeup.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Idea.
 */
@RestController
@RequestMapping("/api")
public class IdeaResource {

    private final Logger log = LoggerFactory.getLogger(IdeaResource.class);

    @Inject
    private IdeaRepository ideaRepository;

    /**
     * POST  /ideas -> Create a new idea.
     */
    @RequestMapping(value = "/ideas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Idea> create(@RequestBody Idea idea) throws URISyntaxException {
        log.debug("REST request to save Idea : {}", idea);
        if (idea.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new idea cannot already have an ID").body(null);
        }
        Idea result = ideaRepository.save(idea);
        return ResponseEntity.created(new URI("/api/ideas/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("idea", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /ideas -> Updates an existing idea.
     */
    @RequestMapping(value = "/ideas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Idea> update(@RequestBody Idea idea) throws URISyntaxException {
        log.debug("REST request to update Idea : {}", idea);
        if (idea.getId() == null) {
            return create(idea);
        }
        Idea result = ideaRepository.save(idea);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("idea", idea.getId().toString()))
                .body(result);
    }

    /**
     * GET  /ideas -> get all the ideas.
     */
    @RequestMapping(value = "/ideas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Idea> getAll() {
        log.debug("REST request to get all Ideas");
        return ideaRepository.findAll();
    }

    /**
     * GET  /ideas/:id -> get the "id" idea.
     */
    @RequestMapping(value = "/ideas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Idea> get(@PathVariable Long id) {
        log.debug("REST request to get Idea : {}", id);
        return Optional.ofNullable(ideaRepository.findOne(id))
            .map(idea -> new ResponseEntity<>(
                idea,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ideas/:id -> delete the "id" idea.
     */
    @RequestMapping(value = "/ideas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Idea : {}", id);
        ideaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("idea", id.toString())).build();
    }
}
