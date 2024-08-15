package clovider.clovider_be.domain.kindergarten;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "kindergarten_tb")
public class Kindergarten extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kindergarten_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String kindergartenNm;

    @Column(nullable = false)
    private String kindergartenAddr;

    @Column(nullable = false)
    private Integer kindergartenScale;

    @Column(nullable = false)
    private Integer kindergartenCapacity;

    @Column(nullable = false, length = 50)
    private String kindergartenNo;

    @Column(nullable = false)
    private String kindergartenTime;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String kindergartenInfo;

    @OneToMany(mappedBy = "kindergarten", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KindergartenClass> kindergartenClass = new ArrayList<>();

    @OneToMany(mappedBy = "kindergarten", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<KindergartenImage> kindergartenImages;

    public KindergartenBuilder toBuilder() {
        return Kindergarten.builder()
                .id(this.id)
                .kindergartenNm(this.kindergartenNm)
                .kindergartenAddr(this.kindergartenAddr)
                .kindergartenScale(this.kindergartenScale)
                .kindergartenCapacity(this.kindergartenCapacity)
                .kindergartenNo(this.kindergartenNo)
                .kindergartenTime(this.kindergartenTime)
                .kindergartenInfo(this.kindergartenInfo)
                .kindergartenClass(this.kindergartenClass)
                .kindergartenImages(this.kindergartenImages);
    }


    public void updateKindergarten(String kindergartenNm, String kindergartenAddr, Integer kindergartenScale,
            Integer kindergartenCapacity, String kindergartenNo, String kindergartenTime, String kindergartenInfo) {
        if (kindergartenNm != null) {
            this.kindergartenNm = kindergartenNm;
        }
        if (kindergartenAddr != null) {
            this.kindergartenAddr = kindergartenAddr;
        }
        if (kindergartenScale != null) {
            this.kindergartenScale = kindergartenScale;
        }
        if (kindergartenCapacity != null) {
            this.kindergartenCapacity = kindergartenCapacity;
        }
        if (kindergartenNo != null) {
            this.kindergartenNo = kindergartenNo;
        }
        if (kindergartenTime != null) {
            this.kindergartenTime = kindergartenTime;
        }
        if (kindergartenInfo != null) {
            this.kindergartenInfo = kindergartenInfo;
        }
    }
}

