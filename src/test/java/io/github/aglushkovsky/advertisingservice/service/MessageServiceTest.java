package io.github.aglushkovsky.advertisingservice.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.github.aglushkovsky.advertisingservice.controller.ScrollDirection;
import io.github.aglushkovsky.advertisingservice.dao.impl.MessageDao;
import io.github.aglushkovsky.advertisingservice.dao.impl.UserDao;
import io.github.aglushkovsky.advertisingservice.dto.request.MessageCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.ScrollableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.MessageResponseDto;
import io.github.aglushkovsky.advertisingservice.entity.Message;
import io.github.aglushkovsky.advertisingservice.exception.NotFoundException;
import io.github.aglushkovsky.advertisingservice.mapper.MessageMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.github.aglushkovsky.advertisingservice.entity.QMessage.message;
import static io.github.aglushkovsky.advertisingservice.util.MessageTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.SecurityTestUtils.*;
import static io.github.aglushkovsky.advertisingservice.util.SecurityUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageDao messageDao;

    @Mock
    private UserDao userDao;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;

    @Nested
    class FindMessages {

        @ParameterizedTest
        @MethodSource("provideScrollDirectionWithCorrespondingExpressions")
        void findMessagesShouldReturnResultWhenAllParametersAreValid(ScrollDirection scrollDirection,
                                                                     Function<Long, BooleanExpression> expectedKeysetFilter) {
            Long receiverId = 1L;
            Long senderId = 2L;
            Long keysetMessageId = 1L;
            Long limit = 10L;
            ScrollableRequestDto scrollable = ScrollableRequestDto.builder()
                    .startId(keysetMessageId)
                    .scrollDirection(scrollDirection)
                    .limit(limit)
                    .build();

            setMockUserInSecurityContext(senderId);
            doReturn(true).when(userDao).isExists(receiverId);
            doReturn(true).when(messageDao).isExists(keysetMessageId);
            Message mockMessage = mock(Message.class);
            ArgumentCaptor<BooleanExpression> predicateCaptor = ArgumentCaptor.forClass(BooleanExpression.class);
            doReturn(List.of(mockMessage)).when(messageDao)
                    .findAll(eq(receiverId), eq(getAuthenticatedUserId()), eq(limit), predicateCaptor.capture());
            MessageResponseDto mockMessageResponseDto = mock(MessageResponseDto.class);
            doReturn(mockMessageResponseDto).when(messageMapper).toDto(mockMessage);

            List<MessageResponseDto> result = messageService.findMessages(receiverId, scrollable);

            assertThat(result)
                    .isNotEmpty()
                    .isEqualTo(List.of(mockMessageResponseDto));
            assertThat(predicateCaptor.getValue()).isEqualTo(expectedKeysetFilter.apply(keysetMessageId));
            verify(userDao).isExists(receiverId);
            verify(messageDao).isExists(keysetMessageId);
            verify(messageDao).findAll(eq(receiverId), eq(getAuthenticatedUserId()), eq(limit), predicateCaptor.capture());
            verify(messageMapper).toDto(mockMessage);
        }

        private static Stream<Arguments> provideScrollDirectionWithCorrespondingExpressions() {
            Function<Long, BooleanExpression> expressionForDownScrollDirection = message.id::gt;
            Function<Long, BooleanExpression> expressionForUpScrollDirection = message.id::lt;

            return Stream.of(
                    Arguments.of(ScrollDirection.DOWN, expressionForDownScrollDirection),
                    Arguments.of(ScrollDirection.UP, expressionForUpScrollDirection)
            );
        }

        @ParameterizedTest
        @EnumSource(ScrollDirection.class)
        void findMessagesShouldThrowExceptionWhenReceiverIdIsInvalid(ScrollDirection scrollDirection) {
            Long invalidReceiverId = 1L;
            Long keysetMessageId = 1L;
            Long limit = 10L;
            ScrollableRequestDto scrollable = ScrollableRequestDto.builder()
                    .startId(keysetMessageId)
                    .scrollDirection(scrollDirection)
                    .limit(limit)
                    .build();
            doReturn(false).when(userDao).isExists(invalidReceiverId);

            assertThatThrownBy(() -> messageService
                    .findMessages(invalidReceiverId, scrollable))
                    .isInstanceOf(NotFoundException.class);
            verify(userDao).isExists(invalidReceiverId);
            verifyNoInteractions(messageDao);
            verifyNoInteractions(messageMapper);
        }

        @ParameterizedTest
        @EnumSource(ScrollDirection.class)
        void findMessagesShouldThrowExceptionWhenKeysetMessageIdIsInvalid(ScrollDirection scrollDirection) {
            Long receiverId = 1L;
            Long invalidKeysetMessageId = 1L;
            Long limit = 10L;
            ScrollableRequestDto scrollable = ScrollableRequestDto.builder()
                    .startId(invalidKeysetMessageId)
                    .scrollDirection(scrollDirection)
                    .limit(limit)
                    .build();
            doReturn(true).when(userDao).isExists(receiverId);
            doReturn(false).when(messageDao).isExists(invalidKeysetMessageId);

            assertThatThrownBy(() -> messageService
                    .findMessages(receiverId, scrollable))
                    .isInstanceOf(NotFoundException.class);
            verify(userDao).isExists(receiverId);
            verify(messageDao).isExists(invalidKeysetMessageId);
            verifyNoMoreInteractions(messageDao);
            verifyNoInteractions(messageMapper);
        }
    }

    @Nested
    class SendMessage {

        @Test
        void sendMessageShouldCreateAndSendMessageWhenReceiverIdIsValid() {
            Long receiverId = 1L;
            MessageCreateRequestDto messageCreateRequestDto = createMessageRequestDtoStub();
            Message createdMessageStub = createMessageStub(null);
            Message sentMessageStub = createMessageStub(1L);
            MessageResponseDto messageResponseDtoStub = createMessageResponseDtoStub(sentMessageStub.getId());
            doReturn(true).when(userDao).isExists(receiverId);
            doReturn(createdMessageStub).when(messageMapper).toEntity(receiverId, messageCreateRequestDto);
            doReturn(sentMessageStub).when(messageDao).add(createdMessageStub);
            doReturn(messageResponseDtoStub).when(messageMapper).toDto(sentMessageStub);

            MessageResponseDto result = messageService.sendMessage(receiverId, messageCreateRequestDto);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(messageResponseDtoStub);
            verify(userDao).isExists(receiverId);
            verify(messageMapper).toEntity(receiverId, messageCreateRequestDto);
            verify(messageDao).add(createdMessageStub);
            verify(messageMapper).toDto(sentMessageStub);
            verifyNoMoreInteractions(messageDao);
            verifyNoMoreInteractions(messageMapper);
            verifyNoMoreInteractions(userDao);
        }

        @Test
        void sendMessageShouldCreateAndSendMessageWhenReceiverIdIsInvalid() {
            Long receiverId = 1L;
            MessageCreateRequestDto messageCreateRequestDto = createMessageRequestDtoStub();
            doReturn(false).when(userDao).isExists(receiverId);

            assertThatThrownBy(() -> messageService.sendMessage(receiverId, messageCreateRequestDto))
                    .isInstanceOf(NotFoundException.class);
            verify(userDao).isExists(receiverId);
            verifyNoMoreInteractions(userDao);
            verifyNoInteractions(messageDao);
            verifyNoMoreInteractions(messageMapper);
        }
    }
}