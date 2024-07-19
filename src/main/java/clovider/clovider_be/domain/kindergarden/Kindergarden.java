package clovider.clovider_be.domain.kindergarden;

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
public class Kindergarden extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String kdgName;

    @Column(nullable = false)
    private String kdgAddress;

    @Column(nullable = false)
    private String kdgScale;

    @Column(nullable = false)
    private String kdgNo;

    @Column(nullable = false)
    private String kdgTime;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String kdgInfo;

}
