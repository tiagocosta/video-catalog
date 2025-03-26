package com.tcs.catalog.application.category.delete;

import com.tcs.catalog.application.UseCaseTest;
import com.tcs.catalog.domain.Fixture;
import com.tcs.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DeleteCategoryUseCase useCase;

    @Test
    public void givenValidId_whenCallsDelete_thenOk() {
        final var prime = Fixture.Categories.prime();
        final var expectedId = prime.id();

        doNothing().when(categoryGateway).deleteById(anyString());

        assertDoesNotThrow(
                () -> this.useCase.execute(expectedId)
        );

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDelete_thenOk() {
        final String expectedId = "invalid";

        assertDoesNotThrow(
                () -> this.useCase.execute(expectedId)
        );

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

}
