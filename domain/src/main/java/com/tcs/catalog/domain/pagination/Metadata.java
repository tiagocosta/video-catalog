package com.tcs.catalog.domain.pagination;

public record Metadata(
        int currentPage,
        int perPage,
        long total
) {
}
