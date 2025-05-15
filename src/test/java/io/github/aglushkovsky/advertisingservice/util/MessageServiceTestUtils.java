package io.github.aglushkovsky.advertisingservice.util;

import io.github.aglushkovsky.advertisingservice.dto.request.MessageCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.MessageResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Message;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageServiceTestUtils {

    public static final String SAMPLE_MESSAGE_TEXT = "Sample message text";

    public static Message createMessageStub(Long id) {
        return Message.builder()
                .id(id)
                .text(SAMPLE_MESSAGE_TEXT)
                .build();
    }

    public static MessageCreateRequestDto createMessageRequestDtoStub() {
        return MessageCreateRequestDto.builder()
                .text(SAMPLE_MESSAGE_TEXT)
                .build();
    }

    public static MessageResponseDto createMessageResponseDtoStub(Long id) {
        return MessageResponseDto.builder()
                .id(id)
                .text(SAMPLE_MESSAGE_TEXT)
                .build();
    }
}
