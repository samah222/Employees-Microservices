package com.learningspringboot.samah.employees.mapping;

import com.learningspringboot.samah.employees.dto.EmployeeDto;
import com.learningspringboot.samah.employees.model.Department;
import com.learningspringboot.samah.employees.model.Employee;
import com.learningspringboot.samah.employees.model.MyUser;
import com.learningspringboot.samah.employees.model.Project;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {
    public static EmployeeDto employeeToEmployeeDto(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setEmployeeType(employee.getEmployeeType());
        dto.setEmployeeName(employee.getEmployeeName());
        dto.setPhone(employee.getPhone());

        MyUser myUser = employee.getUser();
        if (myUser != null)
            dto.setUserId(myUser.getId());
        if (employee.getManager() != null)
            dto.setManagerId(employee.getManager().getId());
        if (employee.getDepartment() != null)
            dto.setDepartmentId(employee.getDepartment().getId());
        dto.setAddress(employee.getAddress());
        dto.setJobTitle(employee.getJobTitle());
        if (dto.getProjectsIds() != null)
            dto.setProjectsIds(employee.getProjects().stream().map(p -> p.getId()).toList());

        return dto;
    }

    public static Employee employeeDtoToEmployee(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        Department department = new Department();
        Employee manager = new Employee();
        MyUser user = new MyUser();

        employee.setId(dto.getId());
        employee.setEmployeeName(dto.getEmployeeName());
        employee.setAddress(dto.getAddress());
        employee.setEmployeeType(dto.getEmployeeType());
        employee.setPhone(dto.getPhone());
        employee.setJobTitle(dto.getJobTitle());
        department.setId(dto.getDepartmentId());
        employee.setDepartment(department);

//        if (dto.getDepartmentId() != null) {
//            employee.setDepartment(Department.builder().id(dto.getDepartmentId()).build());
//        }
//        if (dto.getUserId() != null) {
//            employee.setUser(MyUser.builder().id(dto.getUserId()).build());
//        }
//        if (dto.getManagerId() != null) {
//            employee.setManager(Employee.builder().id(dto.getManagerId()).build());
//        }

//        if (dto.getDepartmentId() != null) {
//            department.setId(dto.getDepartmentId());
//            employee.setDepartment(department);
//        }
//        if (dto.getUserId() != null) {
//            user.setId(dto.getUserId());
//            employee.setUser(user);
//        }
//        if (dto.getManagerId() != null) {
//            manager.setId(dto.getManagerId());
//            employee.setManager(manager);
//        }
        if (dto.getProjectsIds() != null) {
            List<Project> projects = new ArrayList<>();
            for (int i = 0; i < dto.getProjectsIds().size(); i++) {
                projects.get(i).setId(dto.getProjectsIds().get(i));
            }
            employee.setProjects(projects);
        }

        return employee;
    }
}
