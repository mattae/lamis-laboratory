package org.fhi360.lamis.modules.laboratory.web.rest.vm;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.lamisplus.modules.lamis.legacy.domain.entities.Facility;
import org.lamisplus.modules.lamis.legacy.domain.entities.LaboratoryLine;
import org.lamisplus.modules.lamis.legacy.domain.entities.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class LaboratoryDTO {
    private Long id;
    private String uuid;
    private Facility facility;
    private Patient patient;
    private LocalDate dateResultReceived;
    private LocalDate dateSampleCollected;
    private LocalDate dateAssay;
    private String labNo;
    private JsonNode extra;
    private Set<LaboratoryLine> lines = new HashSet<>();
}
