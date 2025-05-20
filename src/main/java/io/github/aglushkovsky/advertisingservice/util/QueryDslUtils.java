package io.github.aglushkovsky.advertisingservice.util;

import com.querydsl.core.types.Path;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QueryDslUtils {

    public static <T> String getName(Path<T> path) {
        return path.getMetadata().getName();
    }
}
