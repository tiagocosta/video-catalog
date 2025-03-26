package com.tcs.catalog.application.category.list;

import com.tcs.catalog.application.UseCase;
import com.tcs.catalog.domain.category.CategoryGateway;
import com.tcs.catalog.domain.category.CategorySearchQuery;
import com.tcs.catalog.domain.pagination.Pagination;

import java.util.Objects;

public class ListCategoryUseCase extends UseCase<CategorySearchQuery, Pagination<ListCategoryOutput>> {

    private final CategoryGateway categoryGateway;

    public ListCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<ListCategoryOutput> execute(CategorySearchQuery aQuery) {
        return this.categoryGateway.findAll(aQuery)
                .map(ListCategoryOutput::from);
    }
}
