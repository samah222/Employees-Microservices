package com.learningspringboot.samah.employees.controller;

import com.learningspringboot.samah.employees.dto.EmployeeDto;
import com.learningspringboot.samah.employees.model.Employee;
import com.learningspringboot.samah.employees.service.EmployeeService;
import com.learningspringboot.samah.employees.service.FullTimeEmployeeService;
import com.learningspringboot.samah.employees.service.PartTimeEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Tag(name = "Employees APIs", description = "All Employees project APIs")
@RequestMapping("v1/employee")
@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private FullTimeEmployeeService fullTimeEmployeeService;
    @Autowired
    private PartTimeEmployeeService partTimeEmployeeService;

    @Operation(summary = "Add an employee ", description = "Add an employee")
    @ApiResponses(value = {@ApiResponse(responseCode = "201")})
    @PostMapping
    public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<EmployeeDto>(employeeService.addEmployee(employeeDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get pages for all employees ", description = "Get pages for all employees")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return new ResponseEntity<List<EmployeeDto>>(employeeService.getAllEmployees(pageNumber, pageSize), HttpStatus.OK);
    }

    @Operation(summary = "Get all employees ", description = "Get all employees")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping({"/allEmployees", "/", "/list"})
    public ModelAndView showEmployees() {
        ModelAndView modelAndView = new ModelAndView("list-employees");
        List<Employee> employees = employeeService.getAllEmployees();
        modelAndView.addObject("employees", employees);
        return modelAndView;
    }

    @Operation(summary = "Add employee in form ", description = "Add employee in form")
    //@ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/addEmployeeForm")
    public ModelAndView addEmployeeForm() {
        ModelAndView modelAndView = new ModelAndView("add-employee-form");
        Employee newEmployee = new Employee();
        modelAndView.addObject("employee", newEmployee);
        return modelAndView;
    }

//    @Operation(summary = "Save employee in form ", description = "Save employee in form")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
//    @PostMapping("/saveEmployee")
//    public String saveEmployee(@ModelAttribute Employee employee) {
//        employeeService.addEmployee(employee);
//        return "redirect:/list";
//    }

    @Operation(summary = "Show update form ", description = "Show update form")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam int employeeId) {
        ModelAndView modelAndView = new ModelAndView("add-employee-form");
        EmployeeDto existingEmployee = employeeService.getEmployee(employeeId);
        modelAndView.addObject("employee", existingEmployee);
        return modelAndView;
    }

    @Operation(summary = "Delete employee by form ", description = "Delete employee by form")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/deleteEmployee")
    public String deleteEmployeeFromList(@RequestParam int employeeId) {
        employeeService.deleteEmployee(employeeId);
        return "redirect:/list";
    }


    @Operation(summary = "Edit an employee ", description = "Edit an employee")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @PutMapping
    public ResponseEntity<EmployeeDto> editEmployee(@Valid @RequestBody EmployeeDto employeeDto, @RequestParam int id) {
        employeeDto.setId(id);
        return new ResponseEntity<EmployeeDto>(employeeService.editEmployee(employeeDto), HttpStatus.OK);
    }

    @Operation(summary = "Get an employee ", description = "Edit an employee")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") int id) {
        return new ResponseEntity<EmployeeDto>(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @Operation(summary = "Delete an employee ", description = "Delete an employee")
    @ApiResponses(value = {@ApiResponse(responseCode = "204")})
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("id") int id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get employees by department ", description = "Get employees by department")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/filterByDepartment/{departmentName}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartmentName(@PathVariable("departmentName") String departmentName) {
        return new ResponseEntity<List<Employee>>(employeeService.getEmployeesByDepartment(departmentName), HttpStatus.OK);
    }

    @Operation(summary = "Get employees by salary ", description = "Get employees by salary")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/filterBySalary")
    public ResponseEntity<List<Employee>> getEmployeesBetween(@RequestParam double min, @RequestParam double max) {
        return new ResponseEntity<List<Employee>>(
                fullTimeEmployeeService.getEmployeesBySalaryRange(min, max).stream().map(e -> e.getEmployee()).toList(),
                HttpStatus.OK);
    }

    @Operation(summary = "Get employees by name ", description = "Get employees by name")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/filterByName")
    public ResponseEntity<Employee> getEmployeesByName(@RequestParam String name) {
        return new ResponseEntity<Employee>(employeeService.getEmployeesByName(name), HttpStatus.OK);
    }

    @Operation(summary = "Get employees by JobTitle ", description = "Get employees by JobTitle")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/filterByJobTitle")
    public ResponseEntity<List<Employee>> getEmployeesByJobTitle(@RequestParam String jobTitle) {
        return new ResponseEntity<List<Employee>>(employeeService.getEmployeesByJobTitle(jobTitle), HttpStatus.OK);
    }

    @Operation(summary = "Get employees by name and department", description = "Get employees by name and department")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/filterByNameAndDepartment")
    public ResponseEntity<List<Employee>> getEmployeesByNameAndDepartment(@RequestParam String name, @RequestParam String department) {
        return new ResponseEntity<List<Employee>>(employeeService.getEmployeesByNameAndDepartmentName(name, department), HttpStatus.OK);
    }

    @Operation(summary = "Get employees by name keyword", description = "Get employees by name containing keyword")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/filterByNameContaining")
    public ResponseEntity<List<Employee>> getEmployeesByNameContaining(@RequestParam String keyword) {
        return new ResponseEntity<List<Employee>>(employeeService.getEmployeesByNameContaining(keyword), HttpStatus.OK);
    }
}
