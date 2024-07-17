package clovider.clovider_be.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.domain.Page;

public class CustomPage<T> {
    private List<T> content;
    private boolean isFirst;
    private boolean isLast;
    private int totalPage;
    private long totalElements;
    private int size;
    private int currPage;
    private boolean hasNext;

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.totalPage = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.currPage = page.getNumber();
        this.hasNext = page.hasNext();
    }

    @JsonProperty("content")
    public List<T> getContent() {
        return content;
    }

    @JsonProperty("isFirst")
    public boolean isFirst() {
        return isFirst;
    }

    @JsonProperty("isLast")
    public boolean isLast() {
        return isLast;
    }

    @JsonProperty("totalPage")
    public int getTotalPage() {
        return totalPage;
    }

    @JsonProperty("totalElements")
    public long getTotalElements() {
        return totalElements;
    }

    @JsonProperty("size")
    public int getSize() {
        return size;
    }

    @JsonProperty("currPage")
    public int getCurrPage() {
        return currPage;
    }

    @JsonProperty("hasNext")
    public boolean isHasNext() {
        return hasNext;
    }
}
