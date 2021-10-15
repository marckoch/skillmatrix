package de.marckoch.skillmatrix.skills.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.time.YearMonth;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @Column(name = "name")
    @NotEmpty
    String name;

    @Column(name = "since")
    @Convert(converter = YearMonthStringAttributeConverter.class)
    YearMonth since;

    @Column(name = "until")
    @Convert(converter = YearMonthStringAttributeConverter.class)
    YearMonth until;
}
