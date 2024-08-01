package clovider.clovider_be.global.util;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import com.lowagie.text.pdf.BaseFont;
import java.io.ByteArrayOutputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
@RequiredArgsConstructor
public class PdfUtil {

    private final TemplateEngine templateEngine;

    public byte[] resultPdf(List<RecruitResult> results, RecruitInfo recruitInfo) {

        try {
            Context context = new Context();
            context.setVariable("results", results);
            context.setVariable("info", recruitInfo);

            String exportHtml = templateEngine.process("exportResult", context);
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver()
                    .addFont(
                            new ClassPathResource("NanumGothic.ttf")
                                    .getURL()
                                    .toString(),
                            BaseFont.IDENTITY_H,
                            BaseFont.EMBEDDED);
            renderer.setDocumentFromString(exportHtml);
            renderer.layout();
            renderer.createPDF(pdfStream);
            pdfStream.close();

            return pdfStream.toByteArray();
        } catch (Exception e) {
            throw new ApiException(ErrorStatus._RECRUIT_EXPORT_ERROR);
        }
    }
}
