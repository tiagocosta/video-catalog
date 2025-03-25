package com.tcs.catalog.domain;

import com.tcs.catalog.domain.category.Category;
import com.tcs.catalog.domain.utils.IdUtils;
import com.tcs.catalog.domain.utils.InstantUtils;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.Year;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Instant instant() {
        return FAKER.timeAndDate().between(
                Instant.ofEpochSecond(Instant.MIN.getEpochSecond()),
                Instant.ofEpochSecond(Instant.MIN.getEpochSecond())
        );
    }

    public static Year year() {
        return Year.of(FAKER.random().nextInt(2020, 2030));
    }

    public static Double duration() {
        return BigDecimal.valueOf(FAKER.random().nextDouble() * 300)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static String title() {
        return FAKER.options().option(
                "Video Title 1",
                "Video Title 2",
                "Video Title 3"
        );
    }

    public static String location() {
        return FAKER.options().option(
                "/media"
        );
    }

    public static String checksum() {
        return FAKER.random().hex();
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static final class Categories {

        public static Category prime() {
            return Category.with(
                    IdUtils.uuid(),
                    "Prime",
                    "Amazon Prime Videos",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }

        public static Category netflix() {
            return Category.with(
                    IdUtils.uuid(),
                    "Netflix",
                    "Netflix Videos",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }
    }

}
