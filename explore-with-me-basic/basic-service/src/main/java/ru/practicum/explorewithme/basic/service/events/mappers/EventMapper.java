package ru.practicum.explorewithme.basic.service.events.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.basic.service.categories.mappers.CategoryMapper;
import ru.practicum.explorewithme.basic.service.categories.models.Category;
import ru.practicum.explorewithme.basic.service.common.mappers.EnumMapper;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.EventFullDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.EventShortDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.NewEventDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.UpdateEventUserRequest;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.EventSort;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.EventState;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.StateAction;
import ru.practicum.explorewithme.basic.service.events.models.Event;
import ru.practicum.explorewithme.basic.service.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.service.users.mappers.UserMapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        imports = {Event.class, EventState.class, Category.class, StateAction.class},
        uses = {UserMapper.class, CategoryMapper.class, LocationMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EventMapper {

    @Mapping(target = "state", expression = "java(EventState.PENDING)")
    @Mapping(target = "category", expression = "java(Category.builder().id(dto.getCategory()).build())")
    @Mapping(target = "eventDate", source = "dto.eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event mapToEvent(NewEventDto dto);

    @Mapping(target = "state", expression = "java(event.getState() != null ? event.getState().name() : null)")
    @Mapping(target = "createdOn", source = "event.createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "eventDate", source = "event.eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "publishedOn", source = "event.publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventFullDto mapToEventFullDto(Event event);

    List<EventFullDto> mapToEventFullDto(List<Event> event);

    @Mapping(target = "eventDate", source = "event.eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventShortDto mapToEventShortDto(Event event);

    List<EventShortDto> mapToEventShortDto(List<Event> event);

    @Mapping(target = "annotation", expression = "java(upd != null && upd.getAnnotation() != null ? upd.getAnnotation() : src.getAnnotation())")
    @Mapping(target = "description", expression = "java(upd != null && upd.getDescription() != null ? upd.getDescription() : src.getDescription())")
    @Mapping(target = "eventDate", expression = "java(upd != null && upd.getEventDate() != null ? upd.getEventDate() : src.getEventDate())")
    @Mapping(target = "location", expression = "java(upd != null && upd.getLocation() != null ? locationMapper.mapToLocation(upd.getLocation()) : src.getLocation())")
    @Mapping(target = "paid", expression = "java(upd != null && upd.getPaid() != null ? upd.getPaid() : src.getPaid())")
    @Mapping(target = "participantLimit", expression = "java(upd != null && upd.getParticipantLimit() != null ? upd.getParticipantLimit() : src.getParticipantLimit())")
    @Mapping(target = "requestModeration", expression = "java(upd != null && upd.getRequestModeration() != null ? upd.getRequestModeration() : src.getRequestModeration())")
    @Mapping(target = "state", expression = "java(upd != null && upd.getStateAction() != null ? upd.getStateAction().getNextState() : src.getState())")
    @Mapping(target = "title", expression = "java(upd != null && upd.getTitle() != null ? upd.getTitle() : src.getTitle())")
    Event updateEvent(Event src, UpdateEventUserRequest upd);

    @Mapping(target = "annotation", expression = "java(upd != null && upd.getAnnotation() != null ? upd.getAnnotation() : src.getAnnotation())")
    @Mapping(target = "category", expression = "java(upd != null && upd.getCategory() != null ? Category.builder().id(upd.getCategory()).build() : src.getCategory())")
    @Mapping(target = "description", expression = "java(upd != null && upd.getDescription() != null ? upd.getDescription() : src.getDescription())")
    @Mapping(target = "eventDate", expression = "java(upd != null && upd.getEventDate() != null ? upd.getEventDate() : src.getEventDate())")
    @Mapping(target = "location", expression = "java(upd != null && upd.getLocation() != null ? locationMapper.mapToLocation(upd.getLocation()) : src.getLocation())")
    @Mapping(target = "paid", expression = "java(upd != null && upd.getPaid() != null ? upd.getPaid() : src.getPaid())")
    @Mapping(target = "participantLimit", expression = "java(upd != null && upd.getParticipantLimit() != null ? upd.getParticipantLimit() : src.getParticipantLimit())")
    @Mapping(target = "requestModeration", expression = "java(upd != null && upd.getRequestModeration() != null ? upd.getRequestModeration() : src.getRequestModeration())")
    @Mapping(target = "state", expression = "java(upd != null && upd.getStateAction() != null ? upd.getStateAction().getNextState() : src.getState())")
    @Mapping(target = "title", expression = "java(upd != null && upd.getTitle() != null ? upd.getTitle() : src.getTitle())")
    Event updateEvent(Event src, UpdateEventAdminRequest upd);

    default EventState toEventState(String src) {
        return new EnumMapper().toEnum(src, EventState.class);
    }

    List<EventState> toEventState(List<String> src);

    default EventSort toEventSort(String src) {
        return new EnumMapper().toEnum(src, EventSort.class);
    }

    List<EventSort> toEventSort(List<String> src);

    Event mapToEvent(Long id);

    Set<Event> mapToEvent(Set<Long> id);
}
