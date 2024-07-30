package clovider.clovider_be.domain.admin.dto;

public record SearchVO(String filter, String value) {

    public static SearchVO of(String filter, String value) {
        return new SearchVO(filter, value);
    }

}
