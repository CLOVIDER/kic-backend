package clovider.clovider_be.domain.kindergarten.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KindergartenRequest {
    private String kdgName;
    private String kdgAddress;
    private String kdgScale;
    private String kdgNo;
    private String kdgTime;
    private String kdgInfo;
    private MultipartFile kdgImage;
}
