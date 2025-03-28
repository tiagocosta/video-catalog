package com.tcs.catalog.infrastructure.category;

import com.tcs.catalog.AbstractElasticsearchTest;
import com.tcs.catalog.domain.Fixture;
import com.tcs.catalog.domain.category.CategorySearchQuery;
import com.tcs.catalog.infrastructure.category.persistence.CategoryDocument;
import com.tcs.catalog.infrastructure.category.persistence.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CategoryElasticsearchGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjection() {
        assertNotNull(categoryGateway);
        assertNotNull(categoryRepository);
    }

    @Test
    public void givenValidCategory_whenCallsSave_thenPersistIt() {
        final var mostWatched = Fixture.Categories.mostWatched();

        final var actualOutput = this.categoryGateway.save(mostWatched);

        assertEquals(mostWatched, actualOutput);

        final var actualCategory = categoryRepository.findById(mostWatched.id()).get();

        assertEquals(mostWatched.id(), actualCategory.id());
        assertEquals(mostWatched.name(), actualCategory.name());
        assertEquals(mostWatched.description(), actualCategory.description());
        assertEquals(mostWatched.active(), actualCategory.active());
        assertEquals(mostWatched.createdAt(), actualCategory.createdAt());
        assertEquals(mostWatched.updatedAt(), actualCategory.updatedAt());
        assertEquals(mostWatched.deletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_thenDeleteCategory() {
        final var prime = Fixture.Categories.prime();

        this.categoryRepository.save(CategoryDocument.from(prime));

        final var expectedId = prime.id();
        assertTrue(this.categoryRepository.existsById(expectedId));

        this.categoryGateway.deleteById(expectedId);

        assertFalse(this.categoryRepository.existsById(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteById_thenOk() {
        assertDoesNotThrow(() -> this.categoryGateway.deleteById("invalid"));
    }

    @Test
    public void givenValidId_whenCallsFindById_thenReturnCategory() {
        final var prime = Fixture.Categories.prime();

        this.categoryRepository.save(CategoryDocument.from(prime));

        final var expectedId = prime.id();
        assertTrue(this.categoryRepository.existsById(expectedId));

        final var actualCategory = this.categoryGateway.findById(expectedId).get();

        assertEquals(prime.id(), actualCategory.id());
        assertEquals(prime.name(), actualCategory.name());
        assertEquals(prime.description(), actualCategory.description());
        assertEquals(prime.active(), actualCategory.active());
        assertEquals(prime.createdAt(), actualCategory.createdAt());
        assertEquals(prime.updatedAt(), actualCategory.updatedAt());
        assertEquals(prime.deletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenInvalidId_whenCallsFindById_thenReturnEmpty() {
        final var actualOutput = assertDoesNotThrow(() -> this.categoryGateway.findById("invalid"));

        assertTrue(actualOutput.isEmpty());
    }

    @Test
    public void givenEmptyCategories_whenCallsFindAll_thenReturnEmptyList() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = this.categoryGateway.findAll(aQuery);

        assertEquals(expectedPage, actualOutput.meta().currentPage());
        assertEquals(expectedPerPage, actualOutput.meta().perPage());
        assertEquals(expectedTotal, actualOutput.meta().total());
        assertEquals(expectedTotal, actualOutput.data().size());
    }

    @ParameterizedTest
    @CsvSource({
            "pri,0,10,1,1,Prime",
            "flix,0,10,1,1,Netflix"
    })
    public void givenValidTerm_whenCallsFindAll_thenReturnFilteredCategories(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName
    ) {

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        mockCategories();

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = this.categoryGateway.findAll(aQuery);

        assertEquals(expectedPage, actualOutput.meta().currentPage());
        assertEquals(expectedPerPage, actualOutput.meta().perPage());
        assertEquals(expectedTotal, actualOutput.meta().total());
        assertEquals(expectedItemsCount, actualOutput.data().size());
        assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,Most watched",
            "name,desc,0,10,3,3,Prime",
            "created_at,asc,0,10,3,3,Prime",
            "created_at,desc,0,10,3,3,Most watched"
    })
    public void givenValidSortAndDirection_whenCallsFindAll_thenReturnSortedCategories(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName
    ) {

        final var expectedTerms = "";

        mockCategories();

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = this.categoryGateway.findAll(aQuery);

        assertEquals(expectedPage, actualOutput.meta().currentPage());
        assertEquals(expectedPerPage, actualOutput.meta().perPage());
        assertEquals(expectedTotal, actualOutput.meta().total());
        assertEquals(expectedItemsCount, actualOutput.data().size());
        assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Most watched",
            "1,1,1,3,Netflix",
            "2,1,1,3,Prime",
            "3,1,0,3,"
    })
    public void givenValidPage_whenCallsFindAll_thenReturnPaginatedCategories(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName
    ) {

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        mockCategories();

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = this.categoryGateway.findAll(aQuery);

        assertEquals(expectedPage, actualOutput.meta().currentPage());
        assertEquals(expectedPerPage, actualOutput.meta().perPage());
        assertEquals(expectedTotal, actualOutput.meta().total());
        assertEquals(expectedItemsCount, actualOutput.data().size());

        if (StringUtils.isNotEmpty(expectedName)) {
            assertEquals(expectedName, actualOutput.data().get(0).name());
        }
    }

    private void mockCategories() {
        this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.prime()));
        this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.netflix()));
        this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.mostWatched()));
    }
}
