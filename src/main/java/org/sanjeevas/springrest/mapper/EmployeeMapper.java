package org.sanjeevas.springrest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.sanjeevas.springrest.dto.EmployeeDto;
import org.sanjeevas.springrest.Employee;
import java.util.List;

/**
 * MapStruct mapper for Employee entity and DTO conversion
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EmployeeMapper {

    /**
     * Convert Employee entity to EmployeeDto
     * 
     * @param employee The employee entity
     * @return EmployeeDto
     */
    EmployeeDto toDto(Employee employee);

    /**
     * Convert EmployeeDto to Employee entity
     * 
     * @param employeeDto The employee DTO
     * @return Employee entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Employee toEntity(EmployeeDto employeeDto);

    /**
     * Convert list of Employee entities to list of EmployeeDtos
     * 
     * @param employees List of employee entities
     * @return List of EmployeeDtos
     */
    List<EmployeeDto> toDtoList(List<Employee> employees);

    /**
     * Convert list of EmployeeDtos to list of Employee entities
     * 
     * @param employeeDtos List of employee DTOs
     * @return List of Employee entities
     */
    List<Employee> toEntityList(List<EmployeeDto> employeeDtos);

    /**
     * Update existing Employee entity from EmployeeDto
     * 
     * @param employeeDto Source DTO
     * @param employee Target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(EmployeeDto employeeDto, @MappingTarget Employee employee);
}
