package clovider.clovider_be.domain.application.entity;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "application")
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id", nullable = false)
    private Long id;

    private Long workYears;
    private Boolean singleParent;
    private Integer childrenCnt;
    private Boolean disability;
    private Boolean dualIncome;
    private Boolean employeeCouple;
    private Boolean sibling;

    private Boolean tempSave;

    private String childName;
    private Long employeeId;
}