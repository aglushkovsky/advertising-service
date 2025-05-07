package io.github.aglushkovsky.advertisingservice.dto.response;

import java.util.List;

public record PageResponseDto<T>(List<T> body, Metadata metadata) {

    public record Metadata(Long currentPage, Long totalPages, Long totalRecords, Boolean isLastPage) {
    }
}
