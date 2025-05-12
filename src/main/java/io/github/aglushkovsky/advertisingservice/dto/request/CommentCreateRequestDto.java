package io.github.aglushkovsky.advertisingservice.dto.request;

import jakarta.validation.constraints.Size;

public record CommentCreateRequestDto(@Size(min = 5) String text) {
}
