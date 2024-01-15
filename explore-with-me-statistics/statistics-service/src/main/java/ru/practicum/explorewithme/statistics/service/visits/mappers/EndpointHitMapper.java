package ru.practicum.explorewithme.statistics.service.visits.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.statistics.lib.dto.CreateEndpointHitDto;
import ru.practicum.explorewithme.statistics.lib.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.service.visits.models.EndpointHit;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {
    @Mapping(target = "timestamp", source = "dto.timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EndpointHit mapToEndpointHit(CreateEndpointHitDto dto);

    @Mapping(target = "timestamp", source = "endpointHit.timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EndpointHitDto mapToEndpointHitDto(EndpointHit endpointHit);
}
