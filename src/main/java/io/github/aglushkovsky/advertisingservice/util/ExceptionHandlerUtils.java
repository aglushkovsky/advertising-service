package io.github.aglushkovsky.advertisingservice.util;

import jakarta.validation.Path;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.StreamSupport;

@UtilityClass
public class ExceptionHandlerUtils {

    public static String getLastItemFromPath(Path path) {
        List<Path.Node> pathNodes = StreamSupport.stream(path.spliterator(), false).toList();
        return pathNodes.get(pathNodes.size() - 1).toString();
    }
}
