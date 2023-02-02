package com.example.springboottesting.repository;

import com.example.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;


    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();
    }

    //test for save employee operation
    @DisplayName("test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given- precondition or setup
       /* Employee employee=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/

        //when- action or behaviour to be tested
        Employee savedEmployee = employeeRepository.save(employee);

        //then- verify the output
        assertNotNull(savedEmployee);
        assertTrue(savedEmployee.getId() > 0, "SavedEmployee id should be greater than 0");
    }

    //test for get all employees operation
    @DisplayName("test for get all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenReturnEmployeesList() {
       /* Employee employee1=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/
        Employee employee2 = Employee.builder()
                .firstName("tim").lastName("shallots").email("tim@abc.com").build();
        Employee employee3 = Employee.builder()
                .firstName("robbie").lastName("onions").email("robbie@abc.com").build();
        List<Employee> employees = new ArrayList<>(Arrays.asList(employee, employee2, employee3));


        employeeRepository.save(employee);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        List<Employee> employeeListFromDB = employeeRepository.findAll();

        assertNotNull(employeeListFromDB);
        assertEquals(3, employeeListFromDB.size());
        assertIterableEquals(employees, employeeListFromDB);


    }

    //test for get employee by id operation
    @DisplayName("test for get employee by id operation")
    @Test
    public void givenEmployeeId_whenFindById_thenReturnEmployees() {
       /* Employee employee1=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/
        employeeRepository.save(employee);


        Employee employeeFromDB = employeeRepository.findById(employee.getId()).orElse(null);

        assertNotNull(employeeFromDB);
        assertEquals(employee, employeeFromDB);
    }

    //test for get employee by email operation
    @DisplayName("test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
       /* Employee employee1=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/
        employeeRepository.save(employee);

        Employee employeeFromDB = employeeRepository.findByEmail(employee.getEmail()).orElse(null);

        assertNotNull(employeeFromDB);
        assertEquals(employee, employeeFromDB);
    }

    //test for update employee operation
    @DisplayName("test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
       /* Employee employee1=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/
        employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.findById(employee.getId()).orElse(null);
        savedEmployee.setEmail("harshal@aher.com");
        employeeRepository.save(savedEmployee);

        Employee employeeFromDB = employeeRepository.findByEmail(savedEmployee.getEmail()).orElse(null);

        assertNotNull(employeeFromDB);
        assertEquals("harshal@aher.com", employeeFromDB.getEmail());
    }

    //test for delete employee operation
    @DisplayName("test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenDeleteEmployeeObject() {
       /* Employee employee=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/
        employeeRepository.save(employee);

        employeeRepository.delete(employee);
        Employee employeeFromDB = employeeRepository.findByEmail(employee.getEmail()).orElse(null);

        assertNull(employeeFromDB);
    }

    //test for get employee by firstName and lastName operation using custom JPQL query using index parameters
    @DisplayName("test for get employee by firstName and lastName operation using JPQL index params")
    @Test
    public void givenEmployeeFirstNameANdLAstName_whenFindByFirstNameAndLAstName_thenReturnEmployeeObject() {
       /* Employee employee=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/
        employeeRepository.save(employee);

        Employee employeeFromDB = employeeRepository.findByFirstNameAndLastName(employee.getFirstName()
                , employee.getLastName());

        assertNotNull(employeeFromDB);
        assertEquals(employee.getEmail(), employeeFromDB.getEmail());
    }

    //test for get employee by firstName and lastName operation using custom JPQL query using named parameters
    @DisplayName("test for get employee by firstName and lastName operation using JPQL named params")
    @Test
    public void givenEmployeeFirstNameANdLAstName_whenFindByFirstNameAndLAstName_thenReturnEmployeeObject_namedParams() {
        /*Employee employee=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/
        employeeRepository.save(employee);

        Employee employeeFromDB = employeeRepository.findByFirstNameAndLastNameWithNamedParams(employee.getFirstName()
                , employee.getLastName());

        assertNotNull(employeeFromDB);
        assertEquals(employee.getEmail(), employeeFromDB.getEmail());
    }

    //test for get employee by firstName and lastName operation using native SQL query using index parameters
    @DisplayName("test for get employee by firstName and lastName operation using native SQL query using index parameters")
    @Test
    public void givenEmployeeFirstNameANdLAstName_whenFindByFirstNameAndLAstName_thenReturnEmployeeObject_nativeSQl() {
       /* Employee employee=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/
        employeeRepository.save(employee);

        Employee employeeFromDB = employeeRepository.findByFirstNameAndLastNameWithSQLNativeQuery(employee.getFirstName()
                , employee.getLastName());

        assertNotNull(employeeFromDB);
        assertEquals(employee.getEmail(), employeeFromDB.getEmail());
    }

    //test for get employee by firstName and lastName operation using native SQL query using named parameters
    @DisplayName("test for get employee by firstName and lastName operation using native SQL query using index parameters")
    @Test
    public void givenEmployeeFirstNameANdLAstName_whenFindByFirstNameAndLAstName_thenReturnEmployeeObject_nativeSQlNamedParams() {
       /* Employee employee=Employee.builder()
                .firstName("harshal").lastName("aher").email("harshal@abc.com").build();*/
        employeeRepository.save(employee);

        Employee employeeFromDB = employeeRepository
                .findByFirstNameAndLastNameWithSQLNativeQueryNamedParams(employee.getFirstName()
                        , employee.getLastName());

        assertNotNull(employeeFromDB);
        assertEquals(employee.getEmail(), employeeFromDB.getEmail());
    }


}

