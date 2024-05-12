package ru.practicum.explorewithme.basic.service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.practicum.explorewithme.basic.service.common.exceptions.ErrorData;
import ru.practicum.explorewithme.basic.service.common.exceptions.EwmExceptionHandler;

import java.util.AbstractMap;
import java.util.Map;

@Configuration
@ComponentScan("ru.practicum.explorewithme.basic")
@EnableTransactionManagement
public class AppConfig {

    @Bean
    EwmExceptionHandler ewmExceptionHandler() {
        Map<String, ErrorData> dbConstraintsErr = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("uq_categories_name_lower", ErrorData.create("Categories title must be unique.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("categories_name_unique_idx", ErrorData.create("Categories title must be unique.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("fk_events_category_id", ErrorData.create("Event category - referential integrity violation.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("fk_events_initiator_id", ErrorData.create("Event initiator - referential integrity violation.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("fk_participation_event_id", ErrorData.create("Participation event - referential integrity violation.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("fk_participation_requester_id", ErrorData.create("Participation requester - referential integrity violation.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("uq_participation_event_id_requester_id", ErrorData.create("Duplicate requests are not allowed", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("uq_users_email_lower", ErrorData.create("User email must be unique.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("users_email_unique_idx", ErrorData.create("User email must be unique.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("uq_compilations_title_lower", ErrorData.create("Compilation title must be unique.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("compilations_title_unique_idx", ErrorData.create("Compilation title must be unique.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("fk_events_compilations_event_id", ErrorData.create("Event was not found", HttpStatus.BAD_REQUEST)),
                new AbstractMap.SimpleEntry<>("fk_events_compilations_compilation_id", ErrorData.create("Compilation was not found", HttpStatus.BAD_REQUEST)),
                new AbstractMap.SimpleEntry<>("uq_places_name_lower", ErrorData.create("Place name must be unique.", HttpStatus.CONFLICT)),
                new AbstractMap.SimpleEntry<>("places_name_unique_idx", ErrorData.create("Place name must be unique.", HttpStatus.CONFLICT))
        );
        return new EwmExceptionHandler(dbConstraintsErr);
    }
}
