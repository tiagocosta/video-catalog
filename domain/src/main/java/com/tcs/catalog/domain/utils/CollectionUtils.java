package com.tcs.catalog.domain.utils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtils {

    private CollectionUtils() {}

    public static <IN, OUT> List<OUT> mapTo(final List<IN> list, final Function<IN, OUT> mapper) {
        if (list == null ) return null;
        return list.stream()
                .map(mapper)
                .toList();
    }

    public static <IN, OUT> Set<OUT> mapTo(final Set<IN> set, final Function<IN, OUT> mapper) {
        if (set == null) return null;
        return set.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

    public static <T> List<T> nullIfEmpty(final List<T> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values;
    }

    public static <T> Set<T> nullIfEmpty(final Set<T> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values;
    }
}
