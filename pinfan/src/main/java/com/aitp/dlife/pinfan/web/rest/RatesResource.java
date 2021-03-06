package com.aitp.dlife.pinfan.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aitp.dlife.pinfan.service.RatesService;
import com.aitp.dlife.pinfan.web.rest.errors.BadRequestAlertException;
import com.aitp.dlife.pinfan.web.rest.util.HeaderUtil;
import com.aitp.dlife.pinfan.service.dto.RatesDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Rates.
 */
@RestController
@RequestMapping("/api")
public class RatesResource {

    private final Logger log = LoggerFactory.getLogger(RatesResource.class);

    private static final String ENTITY_NAME = "rates";

    private final RatesService ratesService;

    public RatesResource(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    /**
     * POST  /rates : Create a new rates.
     *
     * @param ratesDTO the ratesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ratesDTO, or with status 400 (Bad Request) if the rates has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rates")
    @Timed
    public ResponseEntity<RatesDTO> createRates(@Valid @RequestBody RatesDTO ratesDTO) throws URISyntaxException {
        log.debug("REST request to save Rates : {}", ratesDTO);
        if (ratesDTO.getId() != null) {
            throw new BadRequestAlertException("A new rates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RatesDTO result = ratesService.save(ratesDTO);
        return ResponseEntity.created(new URI("/api/rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rates : Updates an existing rates.
     *
     * @param ratesDTO the ratesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ratesDTO,
     * or with status 400 (Bad Request) if the ratesDTO is not valid,
     * or with status 500 (Internal Server Error) if the ratesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rates")
    @Timed
    public ResponseEntity<RatesDTO> updateRates(@Valid @RequestBody RatesDTO ratesDTO) throws URISyntaxException {
        log.debug("REST request to update Rates : {}", ratesDTO);
        if (ratesDTO.getId() == null) {
            return createRates(ratesDTO);
        }
        RatesDTO result = ratesService.save(ratesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ratesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rates : get all the rates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rates in body
     */
    @GetMapping("/rates")
    @Timed
    public List<RatesDTO> getAllRates() {
        log.debug("REST request to get all Rates");
        return ratesService.findAll();
        }

    /**
     * GET  /rates/:id : get the "id" rates.
     *
     * @param id the id of the ratesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ratesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rates/{id}")
    @Timed
    public ResponseEntity<RatesDTO> getRates(@PathVariable Long id) {
        log.debug("REST request to get Rates : {}", id);
        RatesDTO ratesDTO = ratesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ratesDTO));
    }

    /**
     * DELETE  /rates/:id : delete the "id" rates.
     *
     * @param id the id of the ratesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rates/{id}")
    @Timed
    public ResponseEntity<Void> deleteRates(@PathVariable Long id) {
        log.debug("REST request to delete Rates : {}", id);
        ratesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rates?query=:query : search for the rates corresponding
     * to the query.
     *
     * @param query the query of the rates search
     * @return the result of the search
     */
    @GetMapping("/_search/rates")
    @Timed
    public List<RatesDTO> searchRates(@RequestParam String query) {
        log.debug("REST request to search Rates for query {}", query);
        return ratesService.search(query);
    }

}
