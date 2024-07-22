package clovider.clovider_be.domain.kindergarten;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Kindergarten extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kindergarten_id")
    private Long id;

    @Column(nullable = false)
    private String kindergartenNm;

    @Column(nullable = false)
    private String kindergartenAddr;

    @Column(nullable = false)
    private String kindergartenScale;

    @Column(nullable = false)
    private String kindergartenNo;

    @Column(nullable = false)
    private String kindergartenTime;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String kindergartenInfo;

}
