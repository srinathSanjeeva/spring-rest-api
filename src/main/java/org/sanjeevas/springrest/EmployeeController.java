package org.sanjeevas.springrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

        private final EmployeeRepository repository;


    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    //Aggregate Root
    @GetMapping("/employees")
    public List<Employee> all(){
        return repository.findAll();
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee emp){
        return repository.save(emp);
    }

    @GetMapping("/employees/{id}")
    public Employee getById(@PathVariable Long id){
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employees/{id}")
    public Employee replaceEmployee(@RequestBody Employee emp, @PathVariable Long id){
        return repository.findById(id).map(employee -> {
            employee.setName(emp.getName());
            employee.setRole(emp.getRole());
            return repository.save(employee);
        }).orElseGet(() -> {
            emp.setId(id);
            return repository.save(emp);
        });
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
    }

}
