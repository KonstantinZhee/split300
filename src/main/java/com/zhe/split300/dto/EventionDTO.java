package com.zhe.split300.dto;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Operation;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class EventionDTO {

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MMMM/yyyy - HH:mm:ss")
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MMMM/yyyy - HH:mm:ss")
    private Date endTime;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "evention")
    @ToString.Exclude
    private List<Operation> operations;
}
