package clovider.clovider_be.domain.common;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class CustomPage<T> {

    private final List<T> content;
    private final int totalPage;
    private final long totalElements;
    private final int size;
    private final int currPage;
    private final Boolean hasNext;
    private final Boolean isFirst;
    private final Boolean isLast;

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.totalPage = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.currPage = page.getNumber();
        this.hasNext = page.hasNext();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
    }
}
