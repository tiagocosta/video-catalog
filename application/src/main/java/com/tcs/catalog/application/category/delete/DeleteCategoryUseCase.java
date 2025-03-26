package com.tcs.catalog.application.category.delete;

import com.tcs.catalog.application.UnitUseCase;
import com.tcs.catalog.domain.category.CategoryGateway;

import java.util.Objects;

public class DeleteCategoryUseCase extends UnitUseCase<String> {

    private final CategoryGateway categoryGateway;

    public DeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(final String anId) {
        if (anId == null) {
            return;
        }

        this.categoryGateway.deleteById(anId);
    }
}
