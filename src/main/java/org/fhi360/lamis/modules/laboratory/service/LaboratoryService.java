package org.fhi360.lamis.modules.laboratory.service;

import lombok.RequiredArgsConstructor;
import org.fhi360.lamis.modules.laboratory.web.rest.vm.LaboratoryDTO;
import org.fhi360.lamis.modules.laboratory.web.rest.vm.LaboratoryLineDTO;
import org.lamisplus.modules.lamis.legacy.domain.entities.Devolve;
import org.lamisplus.modules.lamis.legacy.domain.entities.Laboratory;
import org.lamisplus.modules.lamis.legacy.domain.entities.LaboratoryLine;
import org.lamisplus.modules.lamis.legacy.domain.entities.Pharmacy;
import org.lamisplus.modules.lamis.legacy.domain.repositories.DevolveRepository;
//import org.lamisplus.modules.lamis.legacy.domain.repositories.LaboratoryLineRepository;
import org.lamisplus.modules.lamis.legacy.domain.repositories.LaboratoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaboratoryService {
    private final LaboratoryRepository laboratoryRepository;
    //private final LaboratoryLineRepository laboratoryLineRepository;
    private final DevolveRepository devolveRepository;

    public Laboratory saveLaboratory(LaboratoryDTO dto) {
        Laboratory laboratory = new Laboratory();
        laboratory.setId(dto.getId());
        laboratory.setPatient(dto.getPatient());
        laboratory.setFacility(dto.getFacility());
        laboratory.setDateSampleCollected(dto.getDateSampleCollected());
        laboratory.setDateResultReceived(dto.getDateResultReceived());
        laboratory.setDateAssay(dto.getDateAssay());
        laboratory.setLabNo(dto.getLabNo());
        laboratory.setExtra(dto.getExtra());
        laboratory.setLines(dto.getLines());
        laboratory.setUuid(dto.getUuid());
        Laboratory laboratory1 = laboratoryRepository.save(laboratory);
        return laboratory1;
    }

    public Laboratory updateLaboratory(LaboratoryDTO dto) {
        return saveLaboratory(dto);
    }

    public void deleteLaboratory(Long laboratoryId) {
        laboratoryRepository.findById(laboratoryId).ifPresent(laboratory -> {
            devolveRepository.findByPatient(laboratory.getPatient())
                    .forEach(devolve -> {
                        if (devolve.getRelatedViralLoad() != null && Objects.equals(laboratoryId, devolve.getRelatedViralLoad().getId())) {
                            devolve.setRelatedViralLoad(null);
                            devolveRepository.save(devolve);
                        }
                        if (devolve.getRelatedCd4() != null && Objects.equals(laboratoryId, devolve.getRelatedCd4().getId())) {
                            devolve.setRelatedCd4(null);
                            devolveRepository.save(devolve);
                        }
                    });
            laboratoryRepository.delete(laboratory);
        });
    }
}
