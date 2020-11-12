package org.fhi360.lamis.modules.laboratory.service;

import lombok.AllArgsConstructor;
import org.fhi360.lamis.modules.patient.service.providers.PatientObservationViewProvider;
import org.lamisplus.modules.lamis.legacy.domain.entities.Patient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LaboratoryRequestObservation implements PatientObservationViewProvider {

    @Override
    public boolean applicableTo(Patient patient) {
        return true;
    }

    @Override
    public String getName() {
        return "Laboratory Request";
    }

    @Override
    public String getPath() {
        return "laboratories";
    }

    @Override
    public String getTooltip() {
        return "New Laboratory Request";
    }

    @Override
    public String getIcon() {
        return null;
    }
}
