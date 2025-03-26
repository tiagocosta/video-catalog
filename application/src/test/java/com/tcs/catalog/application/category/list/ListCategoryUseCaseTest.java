package com.tcs.catalog.application.category.list;

import com.tcs.catalog.application.UseCaseTest;
import com.tcs.catalog.domain.Fixture;
import com.tcs.catalog.domain.category.CategoryGateway;
import com.tcs.catalog.domain.category.CategorySearchQuery;
import com.tcs.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ListCategoryUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private ListCategoryUseCase useCase;

    @Test
    public void givenValidQuery_whenCallsListCategories_thenReturnCategories() {
        final var categories = List.of(
                Fixture.Categories.prime(),
                Fixture.Categories.netflix()
        );

        final var expectedItems = categories.stream()
                .map(ListCategoryOutput::from)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var aPagination =
                new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        when(categoryGateway.findAll(any()))
                .thenReturn(aPagination);

        final var actualOutput = this.useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.meta().currentPage());
        assertEquals(expectedPerPage, actualOutput.meta().perPage());
        assertEquals(expectedItemsCount, actualOutput.meta().total());
        assertTrue(
                expectedItems.size() == actualOutput.data().size()
                && expectedItems.containsAll(actualOutput.data())
        );
    }
}
