package com.akazaki.api.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    private long id;
    private String name;
    private BigDecimal discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;

}
