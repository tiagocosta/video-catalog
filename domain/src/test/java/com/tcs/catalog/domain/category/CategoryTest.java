package com.tcs.catalog.domain.category;

import com.tcs.catalog.domain.DomainTest;
import com.tcs.catalog.domain.exceptions.DomainException;
import com.tcs.catalog.domain.utils.IdUtils;
import com.tcs.catalog.domain.utils.InstantUtils;
import com.tcs.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest extends DomainTest {

    @Test
    public void givenValidParams_whenCallWith_thenInstantiateCategory() {
        final var expectedId = IdUtils.uuid();
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        final var actualCategory = Category.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedDates,
                expectedDates,
                null
        );

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertEquals(expectedDates, actualCategory.createdAt());
        Assertions.assertEquals(expectedDates, actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());
    }

    @Test
    public void givenValidCategory_whenCallWith_thenInstantiateCategory() {
        final var expectedId = IdUtils.uuid();
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        final var aCategory = Category.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedDates,
                expectedDates,
                null
        );

        final var actualCategory = Category.with(aCategory);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(aCategory.id(), actualCategory.id());
        Assertions.assertEquals(aCategory.name(), actualCategory.name());
        Assertions.assertEquals(aCategory.description(), actualCategory.description());
        Assertions.assertEquals(aCategory.active(), actualCategory.active());
        Assertions.assertEquals(aCategory.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.deletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenNullName_whenCallNewCategory_thenReceiveError() {
        final String expectedName = null;

        final var expectedId = IdUtils.uuid();
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualCategory = Category.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedDates,
                expectedDates,
                null
        );

        final var actualException =
                Assertions.assertThrows(
                        DomainException.class,
                        () -> actualCategory.validate(new ThrowsValidationHandler())
                );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyName_whenCallNewCategory_thenReceiveError() {
        final String expectedName = " ";
        final var expectedId = IdUtils.uuid();
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualCategory = Category.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedDates,
                expectedDates,
                null
        );

        final var actualException =
                Assertions.assertThrows(
                        DomainException.class,
                        () -> actualCategory.validate(new ThrowsValidationHandler())
                );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenNullId_whenCallNewCategory_thenReceiveError() {
        final String expectedId = null;

        final var expectedName = "Prime";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var actualCategory = Category.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedDates,
                expectedDates,
                null
        );

        final var actualException =
                Assertions.assertThrows(
                        DomainException.class,
                        () -> actualCategory.validate(new ThrowsValidationHandler())
                );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyId_whenCallNewCategory_thenReceiveError() {
        final var expectedId = " ";

        final var expectedName = "Prime";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var actualCategory = Category.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedDates,
                expectedDates,
                null
        );

        final var actualException =
                Assertions.assertThrows(
                        DomainException.class,
                        () -> actualCategory.validate(new ThrowsValidationHandler())
                );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
