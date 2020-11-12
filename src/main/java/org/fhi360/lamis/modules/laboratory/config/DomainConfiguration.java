package org.fhi360.lamis.modules.laboratory.config;

import org.fhi360.lamis.modules.laboratory.domain.LaboratoryDowmain;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {LaboratoryDowmain.class})
public class DomainConfiguration {
}
