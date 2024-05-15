package ru.practicum.explorewithme.basic.service.compilations.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.basic.lib.dto.compilations.CompilationDto;
import ru.practicum.explorewithme.basic.lib.dto.compilations.NewCompilationDto;
import ru.practicum.explorewithme.basic.lib.dto.compilations.UpdateCompilationRequest;
import ru.practicum.explorewithme.basic.service.compilations.models.Compilation;
import ru.practicum.explorewithme.basic.service.events.mappers.EventMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {EventMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CompilationMapper {

    Compilation toCompilation(NewCompilationDto dto);

    CompilationDto toCompilationDto(Compilation src);

    List<CompilationDto> toCompilationDto(List<Compilation> src);

    @Mapping(target = "title", expression = "java(upd != null && upd.getTitle() != null ? upd.getTitle() : src.getTitle())")
    @Mapping(target = "pinned", expression = "java(upd != null && upd.getPinned() != null ? upd.getPinned() : src.getPinned())")
    @Mapping(target = "events", expression = "java(upd != null && upd.getEvents() != null ? eventMapper.mapToEvent(upd.getEvents()) : src.getEvents())")
    Compilation updateCompilation(Compilation src, UpdateCompilationRequest upd);

}
