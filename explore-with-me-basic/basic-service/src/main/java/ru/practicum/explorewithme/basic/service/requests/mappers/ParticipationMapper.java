package ru.practicum.explorewithme.basic.service.requests.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.basic.service.common.mappers.EnumMapper;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.basic.lib.dto.requests.enums.ParticipationState;
import ru.practicum.explorewithme.basic.service.requests.models.Participation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {

    @Mapping(target = "event", source = "src.event.id")
    @Mapping(target = "requester", source = "src.requester.id")
    ParticipationRequestDto toParticipationRequestDto(Participation src);

    List<ParticipationRequestDto> toParticipationRequestDto(List<Participation> src);

    default ParticipationState toEventState(String src) {
        return new EnumMapper().toEnum(src, ParticipationState.class);
    }
}
