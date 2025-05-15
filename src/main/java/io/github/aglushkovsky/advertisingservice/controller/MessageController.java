package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dto.request.MessageCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.MessageResponseDto;
import io.github.aglushkovsky.advertisingservice.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto sendMessage(@RequestParam @Min(1) Long receiverId,
                                          @RequestBody @Valid MessageCreateRequestDto messageCreateRequestDto) {
        log.info("Start POST /api/v1/messages");
        MessageResponseDto response = messageService.sendMessage(receiverId, messageCreateRequestDto);
        log.info("End POST /api/v1/messages");
        return response;
    }

    @GetMapping
    public List<MessageResponseDto> findMessages(@RequestParam @Min(1) Long receiverId,
                                                 @RequestParam @Min(1) Long lastMessageId,
                                                 @RequestParam(defaultValue = "10") @Min(1) @Max(50) Long limit,
                                                 @RequestParam(defaultValue = "DOWN") ScrollDirection scrollDirection) {
        log.info("Start GET /api/v1/messages; receiverId={}, lastMessageId={}, limit={}",
                receiverId, lastMessageId, limit);
        List<MessageResponseDto> response = messageService.findMessages(receiverId, lastMessageId, limit, scrollDirection);
        log.info("Finished GET /api/v1/messages; found {} messages", response.size());
        return response;
    }
}
