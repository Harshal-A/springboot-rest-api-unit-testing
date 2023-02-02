package com.example.springboottesting.repository;

import com.example.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);


    //define JPQL with index parameters
    @Query("select e from Employee e where e.firstName=?1 and e.lastName=?2")
    Employee findByFirstNameAndLastName(String firstName, String lastName);

    //define JPQL with named parameters
    @Query("select e from Employee e where e.firstName=:firstName and e.lastName=:lastName")
    Employee findByFirstNameAndLastNameWithNamedParams(@Param("firstName") String firstName,
                                                       @Param("lastName") String lastName);

    //define native SQL with index parameters
    @Query(value = "select * from employees as e where e.first_name=?1 and e.last_name=?2",nativeQuery = true)
    Employee findByFirstNameAndLastNameWithSQLNativeQuery(String firstName, String lastName);

    //define native SQL with named parameters
    @Query(value = "select * from employees as e where e.first_name=:firstName and e.last_name=:lastName",nativeQuery = true)
    Employee findByFirstNameAndLastNameWithSQLNativeQueryNamedParams(@Param("firstName") String firstName,
                                                                     @Param("lastName") String lastName);
}
