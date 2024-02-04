package ru.practicum.explorewithme.basic.configs.postgres;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import java.util.List;

public class PostgreSqlEarthDistanceFunction extends StandardSQLFunction {

    public PostgreSqlEarthDistanceFunction() {
        super("earth_distance", StandardBasicTypes.DOUBLE);
    }

    @Override
    public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
        StringBuilder buf = new StringBuilder();
        buf.append(this.getRenderedName(arguments))
                .append("(ll_to_earth(")
                .append(arguments.get(0)) //lat1
                .append(", ")
                .append(arguments.get(1)) //lon1
                .append("), ll_to_earth(")
                .append(arguments.get(2)) //lat2
                .append(", ")
                .append(arguments.get(3)) //lon2
                .append("))");

        return buf.toString();
    }
}
