package io.github.aglushkovsky.advertisingservice.service;

import io.github.aglushkovsky.advertisingservice.dao.impl.MessageDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.MessageCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.MessageResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Message;
import io.github.aglushkovsky.advertisingservice.jwt.JwtAuthentication;
import io.github.aglushkovsky.advertisingservice.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.aglushkovsky.advertisingservice.validator.DaoIdValidator.validateId;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageDao messageDao;
    private final UserDao userDao;
    private final MessageMapper messageMapper;

    public List<MessageResponseDto> findMessage(Long receiverId, Long lastMessageId, Long limit) {
        log.info("Finding messages for receiverId={}; lastMessageId={}", receiverId, lastMessageId);

        validateId(receiverId, messageDao::isExists, "Not found message with id={}", false);
        validateId(lastMessageId, userDao::isExists, "Not found receiver with id={}", false);

        List<MessageResponseDto> messageResponseDtoList = messageDao
                .findAfterId(lastMessageId, limit, getSenderIdFromAuthenticatedUser(), receiverId)
                .stream()
                .map(messageMapper::toDto)
                .toList();

        log.info("Found {} messages", messageResponseDtoList.size());

        return messageResponseDtoList;
    }

    // TODO похожая функциональность используется много где, поэтому можно вынести в какой-нибудь утилитный класс
    private Long getSenderIdFromAuthenticatedUser() {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getId();
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
