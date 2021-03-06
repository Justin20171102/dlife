package com.aitp.dlife.pinfan.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aitp.dlife.pinfan.service.AttendeeService;
import com.aitp.dlife.pinfan.web.rest.errors.BadRequestAlertException;
import com.aitp.dlife.pinfan.web.rest.util.HeaderUtil;
import com.aitp.dlife.pinfan.service.dto.AttendeeDTO;
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
 * REST controller for managing Attendee.
 */
@RestController
@RequestMapping("/api")
public class AttendeeResource {

    private final Logger log = LoggerFactory.getLogger(AttendeeResource.class);

    private static final String ENTITY_NAME = "attendee";

    private final AttendeeService attendeeService;

    public AttendeeResource(AttendeeService attendeeService) {
        this.attendeeService = attendeeService;
    }

    /**
     * POST  /attendees : Create a new attendee.
     *
     * @param attendeeDTO the attendeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attendeeDTO, or with status 400 (Bad Request) if the attendee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attendees")
    @Timed
    public ResponseEntity<AttendeeDTO> createAttendee(@Valid @RequestBody AttendeeDTO attendeeDTO) throws URISyntaxException {
        log.debug("REST request to save Attendee : {}", attendeeDTO);
        if (attendeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new attendee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttendeeDTO result = attendeeService.save(attendeeDTO);
        return ResponseEntity.created(new URI("/api/attendees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attendees : Updates an existing attendee.
     *
     * @param attendeeDTO the attendeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attendeeDTO,
     * or with status 400 (Bad Request) if the attendeeDTO is not valid,
     * or with status 500 (Internal Server Error) if the attendeeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attendees")
    @Timed
    public ResponseEntity<AttendeeDTO> updateAttendee(@Valid @RequestBody AttendeeDTO attendeeDTO) throws URISyntaxException {
        log.debug("REST request to update Attendee : {}", attendeeDTO);
        if (attendeeDTO.getId() == null) {
            return createAttendee(attendeeDTO);
        }
        AttendeeDTO result = attendeeService.save(attendeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attendeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attendees : get all the attendees.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attendees in body
     */
    @GetMapping("/attendees")
    @Timed
    public List<AttendeeDTO> getAllAttendees() {
        log.debug("REST request to get all Attendees");
        return attendeeService.findAll();
        }

    /**
     * GET  /attendees/:id : get the "id" attendee.
     *
     * @param id the id of the attendeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attendeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/attendees/{id}")
    @Timed
    public ResponseEntity<AttendeeDTO> getAttendee(@PathVariable Long id) {
        log.debug("REST request to get Attendee : {}", id);
        AttendeeDTO attendeeDTO = attendeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attendeeDTO));
    }

    /**
     * DELETE  /attendees/:id : delete the "id" attendee.
     *
     * @param id the id of the attendeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attendees/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttendee(@PathVariable Long id) {
        log.debug("REST request to delete Attendee : {}", id);
        attendeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/attendees?query=:query : search for the attendee corresponding
     * to the query.
     *
     * @param query the query of the attendee search
     * @return the result of the search
     */
    @GetMapping("/_search/attendees")
    @Timed
    public List<AttendeeDTO> searchAttendees(@RequestParam String query) {
        log.debug("REST request to search Attendees for query {}", query);
        return attendeeService.search(query);
    }

}
