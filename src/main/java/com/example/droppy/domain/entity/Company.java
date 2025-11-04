package com.example.droppy.domain.entity;

import com.example.droppy.domain.enums.Category;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy =  jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    @Column (name = "phone_number")
    private String phoneNumber;
    @Column(name = "work_start")
    private LocalTime workStart;
    @Column(name = "work_end")
    private LocalTime workEnd;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "category", columnDefinition = "category_enum")

    private Category category;
}
