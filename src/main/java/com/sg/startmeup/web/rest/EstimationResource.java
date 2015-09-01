package com.sg.startmeup.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sg.startmeup.domain.Estimation;
import com.sg.startmeup.repository.EstimationRepository;
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
 * REST controller for managing Estimation.
 */
@RestController
@RequestMapping("/api")
public class EstimationResource {

    private final Logger log = LoggerFactory.getLogger(EstimationResource.class);

    @Inject
    private EstimationRepository estimationRepository;

    /**
     * POST  /estimations -> Create a new estimation.
     */
    @RequestMapping(value = "/estimations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estimation> create(@RequestBody Estimation estimation) throws URISyntaxException {
        log.debug("REST request to save Estimation : {}", estimation);
        if (estimation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new estimation cannot already have an ID").body(null);
        }
        Estimation result = estimationRepository.save(estimation);
        return ResponseEntity.created(new URI("/api/estimations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("estimation", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /estimations -> Updates an existing estimation.
     */
    @RequestMapping(value = "/estimations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estimation> update(@RequestBody Estimation estimation) throws URISyntaxException {
        log.debug("REST request to update Estimation : {}", estimation);
        if (estimation.getId() == null) {
            return create(estimation);
        }
        Estimation result = estimationRepository.save(estimation);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("estimation", estimation.getId().toString()))
                .body(result);
    }

    /**
     * GET  /estimations -> get all the estimations.
     */
    @RequestMapping(value = "/estimations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Estimation> getAll() {
        log.debug("REST request to get all Estimations");
        return estimationRepository.findAll();
    }

    /**
     * GET  /estimations/:id -> get the "id" estimation.
     */
    @RequestMapping(value = "/estimations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estimation> get(@PathVariable Long id) {
        log.debug("REST request to get Estimation : {}", id);
        return Optional.ofNullable(estimationRepository.findOne(id))
            .map(estimation -> new ResponseEntity<>(
                estimation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /estimations/:id -> delete the "id" estimation.
     */
    @RequestMapping(value = "/estimations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Estimation : {}", id);
        estimationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("estimation", id.toString())).build();
    }
}
