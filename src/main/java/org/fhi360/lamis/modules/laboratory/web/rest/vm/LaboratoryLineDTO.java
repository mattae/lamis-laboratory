package org.fhi360.lamis.modules.laboratory.web.rest.vm;

import lombok.Data;
import org.lamisplus.modules.lamis.legacy.domain.entities.LabTest;

@Data
public class LaboratoryLineDTO {
    private Long id;
    private String result;
    private String comment;
    private String indication;
    private LabTest labTest;
}
