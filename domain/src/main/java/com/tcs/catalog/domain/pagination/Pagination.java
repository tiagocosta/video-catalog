package com.tcs.catalog.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        Metadata meta,
        List<T> data
) {

    public Pagination(
            final int currentPage,
            final int perPage,
            final long total,
            List<T> data
    ) {
        this(new Metadata(currentPage, perPage, total), data);
    }

    public <R> Pagination<R> map(Function<T, R> mapper) {
        final List<R> aNewList = this.data().stream()
                .map(mapper)
                .toList();
        return new Pagination<>(meta(), aNewList);
    }
}
