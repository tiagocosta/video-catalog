package com.tcs.catalog.infrastructure.configuration.usecases;

import com.tcs.catalog.application.category.delete.DeleteCategoryUseCase;
import com.tcs.catalog.application.category.list.ListCategoryUseCase;
import com.tcs.catalog.application.category.save.SaveCategoryUseCase;
import com.tcs.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CategoryUseCasesConfig {

    private final CategoryGateway categoryGateway;


    public CategoryUseCasesConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    SaveCategoryUseCase saveCategoryUseCase() {
        return new SaveCategoryUseCase(categoryGateway);
    }

    @Bean
    ListCategoryUseCase listCategoryUseCase() {
        return new ListCategoryUseCase(categoryGateway);
    }
}
