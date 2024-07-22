package clovider.clovider_be.domain.kindergarten.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "새로운 어린이집 상세정보를 등록하기 위한 요청 DTO")
public class KindergartenRequest {

    @Schema(description = "어린이집 이름", example = "샛별 어린이집", required = true)
    private String kindergartenNm;

    @Schema(description = "어린이집 주소", example = "경기도 성남시", required = true)
    private String kindergartenAddr;

    @Schema(description = "어린이집 규모(정원, 크기)", example = "정원: 150명, 크기: 10m^2/명", required = true)
    private String kindergartenScale;

    @Schema(description = "어린이집 전화번호", example = "031-1234-5678", required = true)
    private String kindergartenNo;

    @Schema(description = "어린이집 운영시간", example = "7:00 - 22:00", required = true)
    private String kindergartenTime;

    @Schema(description = "어린이집 기타 정보", example = "- 저희 어린이집은 어쩌구이고\n- 어쩌구입니다.", required = true)
    private String kindergartenInfo;

    @Schema(description = "어린이집 이미지", example = "(binary)", required = true)
    private MultipartFile kindergartenImage;

    // 생성자에서 null 값을 체크하고 기본값으로 초기화
    public KindergartenRequest(String kindergartenNm, String kindergartenAddr, String kindergartenScale,
            String kindergartenNo, String kindergartenTime, String kindergartenInfo,
            MultipartFile kindergartenImage) {
        this.kindergartenNm = (kindergartenNm != null) ? kindergartenNm : "Default Name";
        this.kindergartenAddr = (kindergartenAddr != null) ? kindergartenAddr : "Default Address";
        this.kindergartenScale = (kindergartenScale != null) ? kindergartenScale : "정원: 0명, 크기: 0m^2/명";
        this.kindergartenNo = (kindergartenNo != null) ? kindergartenNo : "000-0000-0000";
        this.kindergartenTime = (kindergartenTime != null) ? kindergartenTime : "00:00 - 00:00";
        this.kindergartenInfo = (kindergartenInfo != null) ? kindergartenInfo : "Default Info";
        this.kindergartenImage = (kindergartenImage != null) ? kindergartenImage : null;
    }
}
