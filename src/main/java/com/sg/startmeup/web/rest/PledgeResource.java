package com.sg.startmeup.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sg.startmeup.domain.Pledge;
import com.sg.startmeup.repository.PledgeRepository;
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
 * REST controller for managing Pledge.
 */
@RestController
@RequestMapping("/api")
public class PledgeResource {

    private final Logger log = LoggerFactory.getLogger(PledgeResource.class);

    @Inject
    private PledgeRepository pledgeRepository;

    /**
     * POST  /pledges -> Create a new pledge.
     */
    @RequestMapping(value = "/pledges",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pledge> create(@RequestBody Pledge pledge) throws URISyntaxException {
        log.debug("REST request to save Pledge : {}", pledge);
        if (pledge.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new pledge cannot already have an ID").body(null);
        }
        Pledge result = pledgeRepository.save(pledge);
        return ResponseEntity.created(new URI("/api/pledges/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("pledge", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /pledges -> Updates an existing pledge.
     */
    @RequestMapping(value = "/pledges",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pledge> update(@RequestBody Pledge pledge) throws URISyntaxException {
        log.debug("REST request to update Pledge : {}", pledge);
        if (pledge.getId() == null) {
            return create(pledge);
        }
        Pledge result = pledgeRepository.save(pledge);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("pledge", pledge.getId().toString()))
                .body(result);
    }

    /**
     * GET  /pledges -> get all the pledges.
     */
    @RequestMapping(value = "/pledges",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pledge> getAll() {
        log.debug("REST request to get all Pledges");
        return pledgeRepository.findAll();
    }

    /**
     * GET  /pledges/:id -> get the "id" pledge.
     */
    @RequestMapping(value = "/pledges/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pledge> get(@PathVariable Long id) {
        log.debug("REST request to get Pledge : {}", id);
        return Optional.ofNullable(pledgeRepository.findOne(id))
            .map(pledge -> new ResponseEntity<>(
                pledge,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pledges/:id -> delete the "id" pledge.
     */
    @RequestMapping(value = "/pledges/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Pledge : {}", id);
        pledgeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pledge", id.toString())).build();
    }
}
