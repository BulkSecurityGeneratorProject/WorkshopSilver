package io.github.robertovillarejo.silverworkshop.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.github.robertovillarejo.silverworkshop.domain.Model;
import io.github.robertovillarejo.silverworkshop.domain.Photo;
import io.github.robertovillarejo.silverworkshop.repository.ModelRepository;
import io.github.robertovillarejo.silverworkshop.repository.PhotoRepository;
import io.github.robertovillarejo.silverworkshop.web.rest.errors.BadRequestAlertException;
import io.github.robertovillarejo.silverworkshop.web.rest.util.HeaderUtil;
import io.github.robertovillarejo.silverworkshop.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Model.
 */
@RestController
@RequestMapping("/api")
public class ModelResource {

    private final Logger log = LoggerFactory.getLogger(ModelResource.class);

    private static final String ENTITY_NAME = "model";

    private final ModelRepository modelRepository;
    private final PhotoRepository photoRepository;

    public ModelResource(ModelRepository modelRepository, PhotoRepository photoRepository) {
        this.modelRepository = modelRepository;
        this.photoRepository = photoRepository;
    }

    @GetMapping("/models/{id}/photos")
    @Timed
    public List<Photo> getPhotosByModel(@PathVariable Long id) {
        log.debug("REST request to get Photos of Model : {}", id);
        return photoRepository.findAllPhotosById(id);
    }

    /**
     * POST /models : Create a new model.
     *
     * @param model
     *            the model to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     *         new model, or with status 400 (Bad Request) if the model has
     *         already an ID
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @PostMapping("/models")
    @Timed
    public ResponseEntity<Model> createModel(@Valid @RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to save Model : {}", model);
        if (model.getId() != null) {
            throw new BadRequestAlertException("A new model cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Model result = modelRepository.save(model);
        return ResponseEntity.created(new URI("/api/models/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT /models : Updates an existing model.
     *
     * @param model
     *            the model to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         model, or with status 400 (Bad Request) if the model is not
     *         valid, or with status 500 (Internal Server Error) if the model
     *         couldn't be updated
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @PutMapping("/models")
    @Timed
    public ResponseEntity<Model> updateModel(@Valid @RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to update Model : {}", model);
        if (model.getId() == null) {
            return createModel(model);
        }
        Model result = modelRepository.save(model);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, model.getId().toString()))
                .body(result);
    }

    /**
     * GET /models : get all the models.
     *
     * @param pageable
     *            the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of models in
     *         body
     */
    @GetMapping("/models")
    @Timed
    public ResponseEntity<List<Model>> getAllModels(Pageable pageable) {
        log.debug("REST request to get a page of Models");
        Page<Model> page = modelRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/models");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /models/:id : get the "id" model.
     *
     * @param id
     *            the id of the model to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the model,
     *         or with status 404 (Not Found)
     */
    @GetMapping("/models/{id}")
    @Timed
    public ResponseEntity<Model> getModel(@PathVariable Long id) {
        log.debug("REST request to get Model : {}", id);
        Model model = modelRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(model));
    }

    /**
     * DELETE /models/:id : delete the "id" model.
     *
     * @param id
     *            the id of the model to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/models/{id}")
    @Timed
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        log.debug("REST request to delete Model : {}", id);
        modelRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}