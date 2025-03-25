package com.tcs.catalog.application.category.save;

import com.tcs.catalog.application.UseCaseTest;
import com.tcs.catalog.domain.Fixture;
import com.tcs.catalog.domain.category.Category;
import com.tcs.catalog.domain.category.CategoryGateway;
import com.tcs.catalog.domain.exceptions.DomainException;
import com.tcs.catalog.domain.utils.IdUtils;
import com.tcs.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SaveCategoryUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private SaveCategoryUseCase useCase;

    @Test
    public void givenValidCategory_whenCallsSave_thenPersistIt() {
        final var aCategory = Fixture.Categories.prime();

        when(categoryGateway.save(any()))
                .thenAnswer(returnsFirstArg());

        this.useCase.execute(aCategory);

        verify(categoryGateway, times(1)).save(eq(aCategory));
    }

    @Test
    public void givenInvalidCategoryName_whenCallsSave_thenReturnError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCategory = Category.with(
                IdUtils.uuid(),
                " ",
                "Amazon Prime Videos",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        final var actualError = assertThrows(
                DomainException.class,
                () -> this.useCase.execute(aCategory)
        );

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(categoryGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidCategoryId_whenCallsSave_thenReturnError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var aCategory = Category.with(
                " ",
                "Prime",
                "Amazon Prime Videos",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        final var actualError = assertThrows(
                DomainException.class,
                () -> this.useCase.execute(aCategory)
        );

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(categoryGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidNullCategory_whenCallsSave_thenReturnError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'aCategory' should not be null";

        final Category aCategory = null;

        final var actualError = assertThrows(
                DomainException.class,
                () -> this.useCase.execute(aCategory)
        );

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(categoryGateway, times(0)).save(any());
    }
}
