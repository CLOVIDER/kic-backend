package clovider.clovider_be.domain.kindergarten;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String kindergartenScale;

    @Column(nullable = false, length = 50)
    private String kindergartenNo;

    @Column(nullable = false)
    private String kindergartenTime;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String kindergartenInfo;

    @OneToMany(mappedBy = "kindergarten", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KindergartenImage> kindergartenImages;

    public void updateKindergarten(String kindergartenNm, String kindergartenAddr, String kindergartenScale,
            String kindergartenNo, String kindergartenTime, String kindergartenInfo) {
        this.kindergartenNm = kindergartenNm;
        this.kindergartenAddr = kindergartenAddr;
        this.kindergartenScale = kindergartenScale;
        this.kindergartenNo = kindergartenNo;
        this.kindergartenTime = kindergartenTime;
        this.kindergartenInfo = kindergartenInfo;
    }
}

