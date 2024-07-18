package clovider.clovider_be.domain.common;

import java.util.List;
import org.springframework.data.domain.Page;

public class CustomPage<T> {

    private List<T> content;
    private int totalPage;
    private long totalElements;
    private int size;
    private int currPage;
    private Boolean hasNext;
    private Boolean isFirst;
    private Boolean isLast;

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
