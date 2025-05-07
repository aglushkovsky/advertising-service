package io.github.aglushkovsky.advertisingservice.dao;

import java.util.List;

public record PageEntity<T>(List<T> body, Metadata metadata) {

    public record Metadata(Long currentPage, Long totalPages, Long totalRecords, Boolean isLastPage) {
    }
}
