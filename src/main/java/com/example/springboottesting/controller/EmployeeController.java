package com.example.springboottesting.controller;

import com.example.springboottesting.model.Employee;
import com.example.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id){
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long id,@RequestBody Employee employee){
         return employeeService.getEmployeeById(id)
                 .map(savedEmployee->{
                     savedEmployee.setFirstName(employee.getFirstName());
                     savedEmployee.setLastName(employee.getLastName());
                     savedEmployee.setEmail(employee.getEmail());

                     Employee updatedEmployee=employeeService.updateEmployee(savedEmployee);
                     return new ResponseEntity<Employee>(updatedEmployee,HttpStatus.OK);
                 }).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id){
         employeeService.deleteEmployee(id);
         return new ResponseEntity<>("Employee with id: "+id+" deleted successfully",HttpStatus.OK);
    }
    }
