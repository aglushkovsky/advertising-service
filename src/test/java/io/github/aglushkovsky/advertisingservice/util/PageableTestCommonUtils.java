package io.github.aglushkovsky.advertisingservice.util;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PageableTestCommonUtils {

    private static final Long DEFAULT_LIMIT = 50L;

    private static final Long DEFAULT_PAGE = 1L;

    public static PageableRequestDto createPageableRequestDto() {
        return PageableRequestDto.builder()
                .page(DEFAULT_PAGE)
                .limit(DEFAULT_LIMIT)
                .build();
    }

    public static <T> PageEntity<T> createPageEntityStubWithSingleRecord(T entity) {
        return PageEntity.<T>builder()
                .body(List.of(entity))
                .metadata(
                        PageEntity.Metadata.builder()
                                .currentPage(1L)
                                .totalPages(1L)
                                .totalRecords(1L)
                                .isLastPage(true)
                                .build()
                )
                .build();
    }
}
