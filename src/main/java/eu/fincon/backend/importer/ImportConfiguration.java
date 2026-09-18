package eu.fincon.backend.importer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ColumnMapping.class)
public class ImportConfiguration {
}
