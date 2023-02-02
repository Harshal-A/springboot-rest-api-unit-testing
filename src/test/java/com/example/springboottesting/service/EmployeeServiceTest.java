package com.example.springboottesting.service;

import com.example.springboottesting.model.Employee;
import com.example.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
//@SpringBootTest
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
//        employeeRepository=mock(EmployeeRepository.class);
//        employeeService=new EmployeeServiceImpl(employeeRepository);
         employee=Employee.builder()
                 .id(1L)
                .firstName("harshal")
                .lastName("aher")
                .email("harshal@abc.com")
                .build();
    }

    //test for save employee method
    @DisplayName("test for save employee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployeeObject(){
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee=employeeService.saveEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals(employee,savedEmployee);
    }

    //test save employee method for failure
    @DisplayName("test save employee method for failure")
    @Test
    public void givenAlreadyPresentEmployeeObject_whenSaveEmployee_thenThrowException(){
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        assertThrows(Exception.class,()->employeeService.saveEmployee(employee));
        verify(employeeRepository,never()).save(any(Employee.class));
    }

    //test get all employees method
    @DisplayName("test get all employee method")
    @Test
    public void given_whenGetAllEmployees_thenReturnEmployeesList(){
        Employee employee1=Employee.builder()
                .id(2L)
                .firstName("tony")
                .lastName("robbins")
                .email("tony@abc.com")
                .build();

        when(employeeRepository.findAll()).thenReturn(List.of(employee,employee1));

       List<Employee> employeeListFromDB=employeeService.getAllEmployees();
       assertNotNull(employeeListFromDB);
       assertEquals(2,employeeListFromDB.size());
    }

    //test get all employees method for failure
    @DisplayName("test get all employees method for failure")
    @Test
    public void given_whenGetAllEmployees_thenReturnNull(){
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> employeeListFromDB=employeeService.getAllEmployees();
        assertNotNull(employeeListFromDB);
        assertEquals(0,employeeListFromDB.size());
    }

    //test get employee by id method
    @DisplayName("test get employee by id method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee employeeFromDB=employeeService.getEmployeeById(employee.getId()).orElse(null);

        assertNotNull(employeeFromDB);
    }

    //test update employee method
    @DisplayName("test update employee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject(){
        when(employeeRepository.save(employee)).thenReturn(employee);

        employee.setLastName("kumar");
        employee.setEmail("harshal@kumar.com");

        Employee updatedEmployee=employeeService.updateEmployee(employee);

        assertEquals(employee.getEmail(),updatedEmployee.getEmail());
        assertEquals(employee.getLastName(),updatedEmployee.getLastName());
    }

    //test delete employee method
    @DisplayName("test delete employee method")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenDeleteEmployeeObject(){
        Long employeeId=1L;
        doNothing().when(employeeRepository).deleteById(employee.getId());

        employeeService.deleteEmployee(employeeId);

       verify(employeeRepository,times(1)).deleteById(employeeId);
    }
}
