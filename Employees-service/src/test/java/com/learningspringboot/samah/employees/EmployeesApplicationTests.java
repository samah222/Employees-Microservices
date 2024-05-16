package com.learningspringboot.samah.employees;

//@SpringBootTest
//class EmployeesApplicationTests {

//	@MockBean
//	EmployeeRepository employeeRepository;
//	@Autowired
//	EmployeeServiceImpl employeeService;
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

//}
