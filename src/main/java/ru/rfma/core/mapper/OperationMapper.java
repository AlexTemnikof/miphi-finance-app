package ru.rfma.core.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.rfma.core.dto.OperationDto;
import ru.rfma.core.entities.Operation;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface OperationMapper {

    OperationDto toDto(Operation operation);
    List<OperationDto> toDtos(List<Operation> operationList);
    Operation toEntity(OperationDto operationDto);
    List<Operation> toEntities(List<OperationDto> operationDtoList);
}