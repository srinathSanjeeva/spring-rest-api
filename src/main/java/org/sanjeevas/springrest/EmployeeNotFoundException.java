package org.sanjeevas.springrest;

public class EmployeeNotFoundException extends RuntimeException{

    EmployeeNotFoundException(Long id){
        super("Could not find employee for id : " + id);
    }
}
