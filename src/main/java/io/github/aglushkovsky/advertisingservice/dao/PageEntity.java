package io.github.aglushkovsky.advertisingservice.dao;

import lombok.Builder;

import java.util.List;

@Builder
public record PageEntity<T>(List<T> body, Metadata metadata) {

    @Builder
    public record Metadata(Long currentPage, Long totalPages, Long totalRecords, Boolean isLastPage) {
    }
}
