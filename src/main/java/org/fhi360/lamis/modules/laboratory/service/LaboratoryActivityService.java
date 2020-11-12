package org.fhi360.lamis.modules.laboratory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.modules.patient.service.providers.PatientActivityProvider;
import org.fhi360.lamis.modules.patient.service.providers.vm.PatientActivity;
import org.lamisplus.modules.lamis.legacy.domain.entities.Laboratory;
import org.lamisplus.modules.lamis.legacy.domain.entities.Patient;
import org.lamisplus.modules.lamis.legacy.domain.repositories.LaboratoryRepository;
import org.lamisplus.modules.lamis.legacy.domain.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaboratoryActivityService implements PatientActivityProvider {
    private final LaboratoryRepository laboratoryRepository;
    private final PatientRepository patientRepository;

    @Override
    public List<PatientActivity> getActivitiesFor(Patient patient) {
        List<PatientActivity> activities = new ArrayList<>();
        patient = patientRepository.getOne(patient.getId());
        List<Laboratory> laboratories = laboratoryRepository.findByPatient(patient);
        laboratories.forEach(laboratory -> {
            String name = "Laboratory Request";
            PatientActivity activity = new PatientActivity(laboratory.getUuid(), name,
                    laboratory.getDateSampleCollected() != null ? laboratory.getDateSampleCollected() :
                            laboratory.getDateResultReceived(), "", "laboratories");
            activities.add(activity);
        });
        return activities;
    }
}
