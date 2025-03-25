package com.tcs.catalog.domain.category;

import com.tcs.catalog.domain.validation.Error;
import com.tcs.catalog.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category {

    private final String id;
    private final String name;
    private final String description;
    private final boolean active;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    private Category(
            final String anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeletionDate
    ) {
        this.id = anId;
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreationDate;
        this.updatedAt = anUpdateDate;
        this.deletedAt = aDeletionDate;
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.id(),
                aCategory.name(),
                aCategory.description(),
                aCategory.active(),
                aCategory.createdAt(),
                aCategory.updatedAt(),
                aCategory.deletedAt()
        );
    }

    public static Category with(
            final String anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeletionDate
    ) {
        return new Category(
                anId,
                aName,
                aDescription,
                isActive,
                aCreationDate,
                anUpdateDate,
                aDeletionDate
        );
    }

    public Category validate(final ValidationHandler handler) {
        if (id == null || id.isBlank()) {
            handler.append(new Error("'id' should not be empty"));
        }

        if (name == null || name.isBlank()) {
            handler.append(new Error("'name' should not be empty"));
        }

        return this;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public boolean active() {
        return active;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }
}
