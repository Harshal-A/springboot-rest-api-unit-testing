package com.example.springboottesting.controller;

import com.example.springboottesting.model.Employee;
import com.example.springboottesting.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.hibernate.result.spi.ResultContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployeeObject() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("harshal")
                .lastName("aher")
                .email("harshal@abc.com")
                .build();
        when(employeeService.saveEmployee(any(Employee.class))).thenAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnSavedEmployeeList() throws Exception {
        //given
        Employee employee1 = Employee.builder()
                .firstName("harshal")
                .lastName("aher")
                .email("harshal@abc.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("raju")
                .lastName("rastogi")
                .email("raju@abc.com")
                .build();

        Employee employee3 = Employee.builder()
                .firstName("michael")
                .lastName("scott")
                .email("michael@abc.com")
                .build();

        List<Employee> employeeList=new ArrayList<>(Arrays.asList(employee1,employee2,employee3));
        when(employeeService.getAllEmployees()).thenReturn(employeeList);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeList)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(employeeList.size())));
    }

    //get employee by id - valid id - positive scenario
    @DisplayName("get employee by id - valid id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given
        Long employeeId=1L;
        Employee employee = Employee.builder()
                .firstName("harshal")
                .lastName("aher")
                .email("harshal@abc.com")
                .build();
        when(employeeService.getEmployeeById(anyLong())).thenReturn(Optional.of(employee));

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //get employee by id - invalid id - negative scenario
    @DisplayName("get employee by id - invalid id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnOptionalEmpty() throws Exception {
        //given
        Long employeeId=1L;
        Employee employee = Employee.builder()
                .firstName("harshal")
                .lastName("aher")
                .email("harshal@abc.com")
                .build();
        when(employeeService.getEmployeeById(anyLong())).thenReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());

    }

    //update employee positive scenario
    @DisplayName("update employee positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        //given
        Long employeeId=1L;
        Employee savedEmployee = Employee.builder()
                .firstName("harshal")
                .lastName("aher")
                .email("harshal@abc.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("harshalkumar")
                .lastName("aher")
                .email("harshal@aher.com")
                .build();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(savedEmployee));
        when(employeeService.updateEmployee(any(Employee.class))).thenAnswer(invocation->invocation.getArgument(0));


        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

    }

    //update employee negative scenario
    @DisplayName("update employee negative scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmployeeNotFound() throws Exception {
        //given
        Long employeeId=1L;
        Employee savedEmployee = Employee.builder()
                .firstName("harshal")
                .lastName("aher")
                .email("harshal@abc.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("harshalkumar")
                .lastName("aher")
                .email("harshal@aher.com")
                .build();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());

    }

    //delete employee positive scenario
    @DisplayName("delete employee positive scenario")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnStatus200() throws Exception {
        //given
        Long employeeId=1L;
        doNothing().when(employeeService).deleteEmployee(employeeId);

        //when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}",employeeId));

        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
