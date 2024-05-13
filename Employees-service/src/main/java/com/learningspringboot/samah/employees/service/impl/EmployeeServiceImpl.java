package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.Util.EmployeeType;
import com.learningspringboot.samah.employees.Util.PhoneNumberValidator;
import com.learningspringboot.samah.employees.dto.EmployeeDto;
import com.learningspringboot.samah.employees.dto.Mail;
import com.learningspringboot.samah.employees.exception.EmployeeNotFoundException;
import com.learningspringboot.samah.employees.exception.InvalidDataException;
import com.learningspringboot.samah.employees.exception.UserNotFoundException;
import com.learningspringboot.samah.employees.mapping.EmployeeMapper;
import com.learningspringboot.samah.employees.model.*;
import com.learningspringboot.samah.employees.publisher.RabbitmqMailProducer;
import com.learningspringboot.samah.employees.repository.*;
import com.learningspringboot.samah.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MyUserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FullTimeEmployeeRepository fullTimeEmployeeRepository;
    @Autowired
    private PartTimeEmployeeRepository partTimeEmployeeRepository;
    @Autowired
    private RabbitmqMailProducer producer;

    private boolean validateEmployeeDto(EmployeeDto employeeDto) {
        if (employeeDto.getEmployeeName().isBlank()
                || employeeDto.getPhone().isBlank()
                || employeeDto.getDepartmentId().toString().isBlank()
                //|| employeeDto.getEmployeeType().equals("")
                || employeeDto.getEmployeeName() == null
                || employeeDto.getPhone() == null
                || employeeDto.getEmployeeType() == null
                || employeeDto.getDepartmentId() == null
        ) {
            throw new InvalidDataException("Employee data cannot be empty");
        }
        if ((employeeDto.getEmployeeName().length() < 2 || (employeeDto.getEmployeeName().length() > 50))) {
            throw new InvalidDataException("Employee name should be between 2 and 50 characters");
        }
        if (employeeDto.getPhone().length() < 9 || employeeDto.getPhone().length() > 16) {
            throw new InvalidDataException("Phone number should be between 9 and 16 numbers");
        }
        if (!PhoneNumberValidator.isValidPhoneNumber(employeeDto.getPhone())) {
            throw new InvalidDataException("Phone number should be a number between 9 and 16 numbers");
        }
        if (!EnumSet.allOf(EmployeeType.class).contains(employeeDto.getEmployeeType()))
            throw new InvalidDataException("Invalid employee type");

        if (employeeRepository.findByUser(MyUser.builder().id(employeeDto.getUserId()).build()).isPresent())
            throw new InvalidDataException("User id already exist");
        return true;
    }

    @Override
    public List<EmployeeDto> getAllEmployees(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return employeeRepository.findAll(pageable)
                .getContent().stream()
                .map(EmployeeMapper::employeeToEmployeeDto).toList();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        if (!validateEmployeeDto(employeeDto))
            throw new RuntimeException("Employee Data not valid");

        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new InvalidDataException("Department not found"));
        MyUser user = userRepository.findById(employeeDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.isEnabled())
            throw new InvalidDataException("User is not activated");
        Employee manager = null;
        if (employeeDto.getManagerId() != null) {
            manager = employeeRepository.findById(employeeDto.getManagerId())
                    .orElseThrow(() -> new InvalidDataException("Manager not found"));
        }
        Employee employee = EmployeeMapper.employeeDtoToEmployee(employeeDto);
        employee.setUser(user);
        employee.setDepartment(department);
        employee.setManager(manager);
        employee = employeeRepository.save(employee);
        user.setEmployee(employee);
        userRepository.save(user);
        if (employee.getEmployeeType() == EmployeeType.FULL_TIME) {
            if (employeeDto.getSalary() != null) {
                if (employeeDto.getSalary() < 1000)
                    throw new InvalidDataException("Salary can not be less than 1000");
                FullTimeEmployee fullTimeEmployee = new FullTimeEmployee();
                fullTimeEmployee.setEmployee(employee);
                fullTimeEmployee.setSalary(employeeDto.getSalary());
                if (employeeDto.getBonus() != null)
                    if (employeeDto.getBonus() > 0)
                        fullTimeEmployee.setBonus(employeeDto.getBonus());
                fullTimeEmployeeRepository.save(fullTimeEmployee);
            }
        }
        if (employee.getEmployeeType() == EmployeeType.FULL_TIME) {
            if (employeeDto.getHourlyRate() != null) {
                if (employeeDto.getHourlyRate() < 0)
                    throw new InvalidDataException("HourlyRate can not be negative");
                PartTimeEmployee partTimeEmployee = new PartTimeEmployee();
                partTimeEmployee.setEmployee(employee);
                partTimeEmployee.setHourlyRate(employeeDto.getHourlyRate());
                if (employeeDto.getHoursWorkedPerWeek() != null)
                    if (employeeDto.getHoursWorkedPerWeek() > 0)
                        partTimeEmployee.setHoursWorkedPerWeek(employeeDto.getHoursWorkedPerWeek());
                partTimeEmployeeRepository.save(partTimeEmployee);
            }
        }

        producer.sendJsonMessage(new Mail(employee.getUser().getEmail(), "Welcome " + employee.getEmployeeName(),
                "Welcome to our company !!"));
        return EmployeeMapper.employeeToEmployeeDto(employee);
    }

    @Override
    public EmployeeDto editEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(employeeDto.getId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        if (employeeDto.getEmployeeName() != null) {
            if (employeeDto.getEmployeeName().isBlank())
                throw new InvalidDataException("Employee name can not be blank");
            if ((employeeDto.getEmployeeName().length() < 2 || (employeeDto.getEmployeeName().length() > 50)))
                throw new InvalidDataException("Employee name should be between 2 and 50 characters");
            employee.setEmployeeName(employeeDto.getEmployeeName());
        }
        if (employeeDto.getPhone() != null) {
            if (employeeDto.getPhone().isBlank())
                throw new InvalidDataException("Employee phone can not be blank");
            if (employeeDto.getPhone().length() < 9 || employeeDto.getPhone().length() > 16)
                throw new InvalidDataException("Employee phone should be between 9 and 16 numbers");
            if (!PhoneNumberValidator.isValidPhoneNumber(employeeDto.getPhone()))
                throw new InvalidDataException("Phone number should be a number between 9 and 16 numbers");
            employee.setPhone(employeeDto.getPhone());
        }
        if (employeeDto.getJobTitle() != null) {
            if (employeeDto.getJobTitle().isBlank())
                throw new InvalidDataException("Employee JobTitle can not be blank");
            employee.setJobTitle(employeeDto.getJobTitle());
        }
        if (employeeDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                    .orElseThrow(() -> new InvalidDataException("Department not found"));
            employee.setDepartment(department);
        }
        if (employeeDto.getAddress() != null) {
            Address address = new Address();
            if (employeeDto.getAddress().getStreetAddress() != null)
                address.setStreetAddress(employeeDto.getAddress().getStreetAddress());
            if (employeeDto.getAddress().getCity() != null)
                address.setCity(employeeDto.getAddress().getCity());
            if (employeeDto.getAddress().getPostalCode() != null)
                address.setPostalCode(employeeDto.getAddress().getPostalCode());
            if (employeeDto.getAddress().getCountry() != null)
                address.setCountry(employeeDto.getAddress().getCountry());
            if (employeeDto.getAddress().getState() != null)
                address.setState(employeeDto.getAddress().getState());
            employee.setAddress(address);
        }
        if (employeeDto.getManagerId() != null) {
            Employee manager = employeeRepository.findById(employeeDto.getManagerId())
                    .orElseThrow(() -> new InvalidDataException("Manager not found"));
            employee.setManager(manager);
        }
        List<Project> projects = new ArrayList<>();
        Project project;
        if (employeeDto.getProjectsIds() != null) {
            for (int i = 0; i < employeeDto.getProjectsIds().size(); i++) {
                project = projectRepository.findById(employeeDto.getProjectsIds().get(i))
                        .orElseThrow(() -> new InvalidDataException("Invalid Project"));
                projects.add(project);
            }
            employee.setProjects(projects);
        }
        if (employeeDto.getUserId() != null) {
            MyUser user = userRepository.findById(employeeDto.getUserId())
                    .orElseThrow(() -> new InvalidDataException("User not found"));
            employee.setUser(user);
        }
        if (employee.getEmployeeType() != null) {
            if (!EnumSet.allOf(EmployeeType.class).contains(employee.getEmployeeType()))
                throw new InvalidDataException("Invalid employee type");
            employee.setEmployeeType(employee.getEmployeeType());
        }

        employeeRepository.save(employee);
        return EmployeeMapper.employeeToEmployeeDto(employee);
    }

    @Override
    public EmployeeDto getEmployee(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        return EmployeeMapper.employeeToEmployeeDto(employee);
    }

    @Override
    public void deleteEmployee(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("Employee not found"));
        MyUser user = userRepository.findById(employee.getUser().getId())
                .orElseThrow(() -> new InvalidDataException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
        //employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String n) {
        return employeeRepository.findByDepartmentName(n);
    }

    @Override
    public List<Employee> getEmployeesByNameAndDepartmentName(String name, String department) {
        return employeeRepository.findByEmployeeNameAndDepartmentName(name, department);
    }

    @Override
    public List<Employee> getEmployeesByNameContaining(String keyword) {
        return employeeRepository.findByEmployeeNameContainingIgnoreCase(keyword);
    }

    @Override
    public Employee getEmployeesByName(String name) {
        return employeeRepository.findByEmployeeNameIgnoreCase(name);
    }

    @Override
    public List<Employee> getEmployeesByJobTitle(String jobTitle) {
        return employeeRepository.findByJobTitle(jobTitle);
    }
//    public double calculateTotalSalaryForAllEmployees() {
//        List<Employee> allEmployees = empRepo.findAll();
//        return allEmployees.stream()
//                .mapToDouble(Employee::getSalary)
//                .sum();
//    }

//    public double calculateTotalSalaryForDepartment(String department) {
//        List<Employee> employeesInDepartment = empRepo.findByDepartment(department);
//        return employeesInDepartment.stream()
//                .mapToDouble(Employee::getSalary)
//                .sum();
//    }

//    public double calculateAverageSalaryForAllEmployees() {
//        List<Employee> allEmployees = empRepo.findAll();
//        return allEmployees.stream()
//                .mapToDouble(Employee::getSalary)
//                .average()
//                .orElse(0.0);
//    }

//    public List<String> getEmployeesAboveSalaryThreshold(double salaryThreshold) {
//        List<Employee> employeesAboveThreshold = empRepo.findAll().stream()
//                .filter(employee -> employee.getSalary() > salaryThreshold)
//                .collect(Collectors.toList());
//
//        return employeesAboveThreshold.stream()
//                .map(Employee::getEmployeeName)
//                .collect(Collectors.toList());
//    }

//    public Employee getEmployeeWithHighestSalary() {
//        List<Employee> allEmployees = empRepo.findAll();
//        Optional<Employee> employeeWithMaxSalary = allEmployees.stream()
//                .max(Comparator.comparing(Employee::getSalary));
//        return employeeWithMaxSalary.orElse(null);
//    }

//    public Employee getEmployeeWithLowestSalary() {
//        List<Employee> allEmployees = empRepo.findAll();
//        Optional<Employee> employeeWithMinSalary = allEmployees.stream()
//                .min(Comparator.comparing(Employee::getSalary));
//        return employeeWithMinSalary.orElse(null);
//    }

//    public double getAverageSalaryByDepartment(String department) {
//        List<Employee> employeesInDepartment = empRepo.findByDepartment(department);
//        return employeesInDepartment.stream()
//                .mapToDouble(Employee::getSalary)
//                .average()
//                .orElse(0.0);
//    }


}
