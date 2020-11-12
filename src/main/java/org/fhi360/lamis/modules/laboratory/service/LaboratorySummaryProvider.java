package org.fhi360.lamis.modules.laboratory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.modules.patient.service.providers.PatientSummaryProvider;
import org.fhi360.lamis.modules.patient.service.providers.vm.Field;
import org.fhi360.lamis.modules.patient.service.providers.vm.Summary;
import org.lamisplus.modules.lamis.legacy.domain.entities.Laboratory;
import org.lamisplus.modules.lamis.legacy.domain.entities.LaboratoryLine;
import org.lamisplus.modules.lamis.legacy.domain.entities.Patient;
//import org.lamisplus.modules.lamis.legacy.domain.repositories.LaboratoryLineRepository;
import org.lamisplus.modules.lamis.legacy.domain.repositories.LaboratoryRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaboratorySummaryProvider implements PatientSummaryProvider {
    private final LaboratoryRepository laboratoryRepository;
    //private final LaboratoryLineRepository laboratoryLineRepository;

    @Override
    public List<Summary> getSummaries(Patient patient) {
        List<Summary> summaries = new ArrayList<>();
        Summary summary = new Summary();
        summary.setHeader("Viral Load Detail");
        List<Field> fields = new ArrayList<>();
        List<Laboratory> laboratories = laboratoryRepository.findViralLoadTestByPatient(patient.getId());
        if (laboratories.isEmpty()) {
            fields.add(new Field(Field.FieldType.TEXT, "Last Viral Load", null));
            fields.add(new Field(Field.FieldType.TEXT, "Date Result Received", null));
            summary.setFields(fields);
            summaries.add(summary);
        } else {
            Laboratory laboratory = laboratories.stream()
                    .sorted((l1, l2) -> l2.getDateResultReceived().compareTo(l1.getDateResultReceived()))
                    .findFirst().get();
            LaboratoryLine line = laboratory.getLines().stream()
                    .filter(l -> Objects.equals(l.getLabTestId(), 16L))
                    .findFirst().get();
            fields.add(new Field(Field.FieldType.INT, "Last Viral Load", line.getResult()));
            fields.add(new Field(Field.FieldType.DATE, "Date Sample Collected", laboratory.getDateSampleCollected()));
            fields.add(new Field(Field.FieldType.DATE, "Date Assay", laboratory.getDateAssay()));
            fields.add(new Field(Field.FieldType.DATE, "Date Result Received", laboratory.getDateResultReceived()));
            summary.setFields(fields);
            summaries.add(summary);
        }
        return summaries;
    }

    @PostConstruct
    public void log() {
        LOG.info("Component init");
    }
}
