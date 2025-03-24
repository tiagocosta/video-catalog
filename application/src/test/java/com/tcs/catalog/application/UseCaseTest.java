package com.tcs.catalog.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

@Tag("unitTest")
public abstract class UseCaseTest {

    @BeforeEach
    abstract protected void cleanUp();
}
