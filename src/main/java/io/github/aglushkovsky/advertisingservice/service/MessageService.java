package io.github.aglushkovsky.advertisingservice.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.github.aglushkovsky.advertisingservice.controller.ScrollDirection;
import io.github.aglushkovsky.advertisingservice.dao.impl.MessageDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.MessageCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.ScrollableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.MessageResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Message;
import io.github.aglushkovsky.advertisingservice.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.aglushkovsky.advertisingservice.controller.ScrollDirection.*;
import static io.github.aglushkovsky.advertisingservice.entity.QMessage.message;
import static io.github.aglushkovsky.advertisingservice.util.SecurityUtils.*;
import static io.github.aglushkovsky.advertisingservice.validator.DaoIdValidator.validateId;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageDao messageDao;
    private final UserDao userDao;
    private final MessageMapper messageMapper;

    public List<MessageResponseDto> findMessages(Long receiverId, ScrollableRequestDto scrollable) {
        log.info("Finding messages for receiverId={}; scrollable={}", receiverId, scrollable);

        validateId(receiverId, userDao::isExists, "Not found receiver with id={}", false);
        validateId(scrollable.startId(), messageDao::isExists, "Not found message with id={}", false);

        List<MessageResponseDto> messageResponseDtoList = messageDao
                .findAll(
                        receiverId,
                        getAuthenticatedUserId(),
                        scrollable.limit(),
                        getKeysetFilter(scrollable.startId(), scrollable.scrollDirection()))
                .stream()
                .map(messageMapper::toDto)
                .toList();

        log.info("Found {} messages", messageResponseDtoList.size());

        return messageResponseDtoList;
    }

    private BooleanExpression getKeysetFilter(Long keysetMessageId, ScrollDirection scrollDirection) {
        return scrollDirection == DOWN ? message.id.gt(keysetMessageId) : message.id.lt(keysetMessageId);
    }

    public MessageResponseDto sendMessage(Long receiverId, MessageCreateRequestDto messageCreateRequestDto) {
        log.info("Sending message to receiverId={}", receiverId);

        validateId(receiverId, userDao::isExists, "Not found receiver with id={}", false);

        Message createdMessage = messageMapper.toEntity(receiverId, messageCreateRequestDto);
        Message sentMessage = messageDao.add(createdMessage);
        MessageResponseDto sentMessageResponseDto = messageMapper.toDto(sentMessage);

        log.info("Sent message to receiverId={} with id={}", receiverId, sentMessageResponseDto.id());

        return sentMessageResponseDto;
    }
}
