package org.fhi360.lamis.modules.laboratory.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.modules.laboratory.service.LaboratoryService;
import org.fhi360.lamis.modules.laboratory.web.rest.vm.LaboratoryDTO;
import org.fhi360.lamis.modules.laboratory.web.rest.vm.LaboratoryLineDTO;
import org.lamisplus.modules.base.web.errors.BadRequestAlertException;
import org.lamisplus.modules.base.web.util.HeaderUtil;
import org.lamisplus.modules.lamis.legacy.domain.entities.LabTest;
import org.lamisplus.modules.lamis.legacy.domain.entities.LabTestCategory;
import org.lamisplus.modules.lamis.legacy.domain.entities.Laboratory;
import org.lamisplus.modules.lamis.legacy.domain.repositories.LabTestCategoryRepository;
import org.lamisplus.modules.lamis.legacy.domain.repositories.LabTestRepository;
import org.lamisplus.modules.lamis.legacy.domain.repositories.LaboratoryRepository;
import org.lamisplus.modules.lamis.legacy.domain.repositories.PatientRepository;
import org.lamisplus.modules.lamis.legacy.domain.repositories.projections.ReportedDates;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class LaboratoryResource {
    private static final String ENTITY_NAME = "laboratory";

    private final LaboratoryRepository laboratoryRepository;
    private final PatientRepository patientRepository;
    private final LaboratoryService laboratoryService;
    private final LabTestRepository labTestRepository;
    private final LabTestCategoryRepository labTestCategoryRepository;

    /**
     * POST  /laboratories : Create a new laboratory.
     *
     * @param laboratory the laboratory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new laboratory, or with status 400 (Bad Request) if the laboratory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/laboratories")
    public ResponseEntity<Laboratory> createLaboratory(@RequestBody LaboratoryDTO laboratory) throws URISyntaxException {
        LOG.debug("REST request to save Laboratory");
        if (laboratory.getId() != null) {
            throw new BadRequestAlertException("A new laboratory cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Laboratory result = laboratoryService.saveLaboratory(laboratory);
        return ResponseEntity.created(new URI("/api/laboratory/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /laboratories : Updates an existing laboratory.
     *
     * @param laboratory the laboratory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated laboratory,
     * or with status 400 (Bad Request) if the laboratory is not valid,
     * or with status 500 (Internal Server Error) if the laboratory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/laboratories")
    public ResponseEntity<Laboratory> updateLaboratory(@RequestBody LaboratoryDTO laboratory) throws URISyntaxException {
        LOG.debug("REST request to update laboratory : {}", laboratory);
        if (laboratory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Laboratory result = laboratoryService.updateLaboratory(laboratory);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, laboratory.getId().toString()))
                .body(result);
    }

    /**
     * GET  /laboratories/:id : get the "id" laboratory.
     *
     * @param id the id of the laboratory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the laboratory, or with status 404 (Not Found)
     */
    @GetMapping("/laboratories/{id}")
    public ResponseEntity<Laboratory> getLaboratory(@PathVariable Long id) {
        LOG.debug("REST request to get laboratory : {}", id);
        Optional<Laboratory> laboratory = laboratoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(laboratory);
    }

    /**
     * GET  /laboratories/:id : get the "uuid" laboratory.
     *
     * @param id the uuid of the laboratory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the laboratory, or with status 404 (Not Found)
     */
    @GetMapping("/laboratories/by-uuid/{id}")
    public ResponseEntity<Laboratory> getLaboratoryByUuid(@PathVariable String id) {
        LOG.debug("REST request to get laboratory : {}", id);
        Optional<Laboratory> laboratory = laboratoryRepository.findByUuid(id);
        return ResponseUtil.wrapOrNotFound(laboratory);
    }


    /**
     * DELETE  /laboratories/:id : delete the "id" laboratory.
     *
     * @param id the id of the laboratory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/laboratories/{id}")
    public ResponseEntity<Void> deleteLaboratory(@PathVariable Long id) {
        LOG.debug("REST request to delete laboratory : {}", id);

        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE  /laboratories/:id : delete the "uuid" laboratory.
     *
     * @param id the uuid of the laboratory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/laboratories/by-uuid/{id}")
    public ResponseEntity<Void> deleteLaboratoryByUuid(@PathVariable String id) {
        LOG.debug("REST request to delete laboratory : {}", id);

        laboratoryRepository.findByUuid(id).ifPresent(laboratory -> laboratoryService.deleteLaboratory(laboratory.getId()));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    @GetMapping("/laboratories/test-categories")
    public List<LabTestCategory> getLabTestCategories() {
        return labTestCategoryRepository.findAll();
    }

   /* @GetMapping("/laboratories/{laboratoryId}/lines")
    public List<LaboratoryLineDTO> getLaboratoryLines(@PathVariable Long laboratoryId) {
        return laboratoryService.getLaboratoryLines(laboratoryId);
    }*/

    @GetMapping("/laboratories/lab-tests/category/{categoryId}")
    public List<LabTest> getLabTestByCategory(@PathVariable Long categoryId) {
        Optional<LabTestCategory> labTestCategory = labTestCategoryRepository.findById(categoryId);
        if (labTestCategory.isPresent()) {
            return labTestRepository.findByLabTestCategory(labTestCategory.get());
        }
        return new ArrayList<>();
    }

    @GetMapping("/laboratories/lab-test/{id}")
    public ResponseEntity<LabTest> getLabTestById(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(labTestRepository.findById(id));
    }

    @GetMapping("/laboratories/patient/{id}/report-dates")
    public List<LocalDate> getVisitDatesByPatient(@PathVariable Long id) {
        List<LocalDate> visitDates = new ArrayList<>();
        patientRepository.findById(id).ifPresent(patient -> {
            List<LocalDate> dates = laboratoryRepository.findVisitsByPatient(patient).stream()
                    .map(ReportedDates::getDateSampleCollected)
                    .collect(Collectors.toList());
            visitDates.addAll(dates);
        });
        return visitDates;
    }
}
