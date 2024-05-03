package com.learningspringboot.samah.employees.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
