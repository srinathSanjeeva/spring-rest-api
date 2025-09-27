package org.sanjeevas.springrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Response wrapper for employee list with pagination metadata
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@Schema(description = "Employee list response with HAL-like structure")
public class EmployeeListResponseDto {

    @Schema(description = "Embedded employees data")
    private EmbeddedEmployees embedded;

    @Schema(description = "Pagination and metadata information")
    private PageMetadata page;

    public EmployeeListResponseDto(List<EmployeeDto> employees) {
        this.embedded = new EmbeddedEmployees(employees);
        this.page = new PageMetadata(employees.size(), 0, employees.size());
    }

    public EmployeeListResponseDto(List<EmployeeDto> employees, int page, int size, long totalElements) {
        this.embedded = new EmbeddedEmployees(employees);
        this.page = new PageMetadata(size, page, totalElements);
    }

    public EmbeddedEmployees getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmbeddedEmployees embedded) {
        this.embedded = embedded;
    }

    public PageMetadata getPage() {
        return page;
    }

    public void setPage(PageMetadata page) {
        this.page = page;
    }

    @Schema(description = "Embedded employees collection")
    public static class EmbeddedEmployees {
        @Schema(description = "List of employees")
        private List<EmployeeDto> employeeList;

        public EmbeddedEmployees(List<EmployeeDto> employeeList) {
            this.employeeList = employeeList;
        }

        public List<EmployeeDto> getEmployeeList() {
            return employeeList;
        }

        public void setEmployeeList(List<EmployeeDto> employeeList) {
            this.employeeList = employeeList;
        }
    }

    @Schema(description = "Pagination metadata")
    public static class PageMetadata {
        @Schema(description = "Number of elements in current page", example = "10")
        private int size;

        @Schema(description = "Current page number (0-based)", example = "0")
        private int number;

        @Schema(description = "Total number of elements", example = "100")
        private long totalElements;

        @Schema(description = "Total number of pages", example = "10")
        private int totalPages;

        public PageMetadata(int size, int number, long totalElements) {
            this.size = size;
            this.number = number;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
        }

        // Getters and setters
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }

        public int getNumber() { return number; }
        public void setNumber(int number) { this.number = number; }

        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    }
}
