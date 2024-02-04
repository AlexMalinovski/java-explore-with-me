package ru.practicum.explorewithme.basic.configs.postgres;

import org.hibernate.dialect.PostgreSQLDialect;

public class CustomPostgreSQLDialect extends PostgreSQLDialect {

    public CustomPostgreSQLDialect() {
        registerFunction("earth_distance", new PostgreSqlEarthDistanceFunction());
    }
}
