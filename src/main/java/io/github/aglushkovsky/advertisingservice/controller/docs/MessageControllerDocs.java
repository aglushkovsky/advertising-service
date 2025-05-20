package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dto.request.MessageCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.ScrollableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.MessageResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@Tag(name = "Message", description = "Create-read operations for messages")
public interface MessageControllerDocs {

    MessageResponseDto sendMessage(Long receiverId, MessageCreateRequestDto messageCreateRequestDto);

    List<MessageResponseDto> findMessages(Long receiverId, @ParameterObject ScrollableRequestDto scrollableRequestDto);
}
