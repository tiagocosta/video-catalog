package com.tcs.catalog.application.category.save;

import com.tcs.catalog.application.UseCase;
import com.tcs.catalog.domain.category.Category;
import com.tcs.catalog.domain.category.CategoryGateway;
import com.tcs.catalog.domain.exceptions.NotificationException;
import com.tcs.catalog.domain.validation.Error;
import com.tcs.catalog.domain.validation.handler.Notification;

import java.util.Objects;

public class SaveCategoryUseCase extends UseCase<Category, Category> {

    private final CategoryGateway categoryGateway;

    public SaveCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Category execute(Category aCategory) {
        if (aCategory == null) {
            throw NotificationException.with(new Error("'aCategory' should not be null"));
        }

        final var notification = Notification.create();
        aCategory.validate(notification);

        if (notification.hasErrors()) {
            throw NotificationException.with("invalid category", notification);
        }

        return this.categoryGateway.save(aCategory);
    }
}
