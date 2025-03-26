package com.tcs.catalog.infrastructure.graphql;

import com.tcs.catalog.application.category.list.ListCategoryOutput;
import com.tcs.catalog.application.category.list.ListCategoryUseCase;
import com.tcs.catalog.domain.Fixture;
import com.tcs.catalog.domain.category.CategorySearchQuery;
import com.tcs.catalog.domain.pagination.Pagination;
import com.tcs.catalog.infrastructure.GraphQLControllerTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQLControllerTest(controllers = CategoryGraphQLController.class)
public class CategoryGraphQLControllerTest {

    @MockitoBean
    private ListCategoryUseCase listCategoryUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgs_whenCallsListCategories_thenReturnAllCategories() {
        final var expectedCategories = List.of(
                ListCategoryOutput.from(Fixture.Categories.prime()),
                ListCategoryOutput.from(Fixture.Categories.netflix())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        when(listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedCategories.size(), expectedCategories));

        final var aQuery = """
                {
                    categories {
                        id
                        name
                    }
                }
                """;

        final var aResult = this.graphql.document(aQuery).execute();

        final var actualCategories = aResult.path("categories")
                .entityList(ListCategoryOutput.class)
                .get();

        assertTrue(
                actualCategories.size() == expectedCategories.size()
                        && actualCategories.containsAll(expectedCategories)
        );

        final var captor = ArgumentCaptor.forClass(CategorySearchQuery.class);
        verify(listCategoryUseCase, times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        assertEquals(expectedPage, actualQuery.page());
        assertEquals(expectedPerPage, actualQuery.perPage());
        assertEquals(expectedTerms, actualQuery.terms());
        assertEquals(expectedSort, actualQuery.sort());
        assertEquals(expectedDirection, actualQuery.direction());
    }

    @Test
    public void givenCustomArgs_whenCallsListCategories_thenReturnAllCategories() {
        final var expectedCategories = List.of(
                ListCategoryOutput.from(Fixture.Categories.prime()),
                ListCategoryOutput.from(Fixture.Categories.netflix())
        );

        final var expectedPage = 2;
        final var expectedPerPage = 15;
        final var expectedTerms = "aaa";
        final var expectedSort = "id";
        final var expectedDirection = "desc";

        when(listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedCategories.size(), expectedCategories));

        final var aQuery = """
                query AllCategories($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String) {
                
                            categories(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction) {
                                id
                                name
                            }
                
                }
                """;

        final var aResult = this.graphql.document(aQuery)
                .variable("search", expectedTerms)
                .variable("page", expectedPage)
                .variable("perPage", expectedPerPage)
                .variable("sort", expectedSort)
                .variable("direction", expectedDirection)
                .execute();

        final var actualCategories = aResult.path("categories")
                .entityList(ListCategoryOutput.class)
                .get();

        assertTrue(
                actualCategories.size() == expectedCategories.size()
                        && actualCategories.containsAll(expectedCategories)
        );

        final var captor = ArgumentCaptor.forClass(CategorySearchQuery.class);
        verify(listCategoryUseCase, times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        assertEquals(expectedPage, actualQuery.page());
        assertEquals(expectedPerPage, actualQuery.perPage());
        assertEquals(expectedTerms, actualQuery.terms());
        assertEquals(expectedSort, actualQuery.sort());
        assertEquals(expectedDirection, actualQuery.direction());
    }
}
