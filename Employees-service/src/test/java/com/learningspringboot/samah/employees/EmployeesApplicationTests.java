package com.learningspringboot.samah.employees;

import com.learningspringboot.samah.employees.repository.EmployeeRepository;
import com.learningspringboot.samah.employees.service.impl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import static org.junit.Assert.*;

//import static org.assertj.core.api.ClassBasedNavigableIterableAssert.assertThat;

//import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmployeesApplicationTests {

//	@MockBean
	EmployeeRepository employeeRepository;
	@Autowired
	EmployeeServiceImpl employeeService;
//
//	@BeforeEach
//	void setUp(){
//		Employee employee = Employee.builder()
//				.name("sama")
//				.email("test@nctr.sd")
//				.department("QA")
//				.phone("+09111111111").build();
//		Mockito.when(employeeRepository.findByName("sama")).thenReturn(employee);
//	}
//
//	@Test
//	@DisplayName("Get employee data when valid name is given")
//	public void whenValidEmployeeNameGiven_thenShouldFound(){
//        Employee employee = Employee.builder()
//				.name("sama")
//				.email("test@nctr.sd")
//				.department("QA")
//				.phone("+09111111111").build();
//		String employeeName = "sama";
//		Employee found = employeeService.getEmployeesByName(employeeName);
//		assertEquals(employeeName,found.getName());
//	}

}
