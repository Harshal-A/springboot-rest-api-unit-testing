package com.example.springboottesting.service;

import com.example.springboottesting.model.Employee;
import com.example.springboottesting.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EmployeeService {

   Employee saveEmployee(Employee employee);

   List<Employee> getAllEmployees();

   Optional<Employee> getEmployeeById(Long id);

   Employee updateEmployee(Employee updatedEmployee);

   void deleteEmployee(Long id);


}
