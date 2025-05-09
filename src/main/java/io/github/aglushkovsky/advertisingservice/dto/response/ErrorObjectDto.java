package io.github.aglushkovsky.advertisingservice.dto.response;

import java.util.List;

public record ErrorObjectDto(String parameter,
                             List<String> messages) {
}
