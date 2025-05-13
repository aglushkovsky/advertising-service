package io.github.aglushkovsky.advertisingservice.dao.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import io.github.aglushkovsky.advertisingservice.dao.AbstractDao;
import io.github.aglushkovsky.advertisingservice.entity.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QMessage.*;

@Repository
public class MessageDao extends AbstractDao<Message, Long> {

    @Override
    public List<Message> findAll() {
        return findAll(message);
    }

    @Override
    public Optional<Message> findById(Long id) {
        return findById(Message.class, id);
    }

    @Transactional(readOnly = true)
    public boolean isExists(Long messageId) {
        return isExists(message, message.id.eq(messageId));
    }

    public List<Message> findAll(Long receiverId, Long senderId, Long limit, BooleanExpression keysetWhereFilter) {
        return new JPAQuery<>(entityManager)
                .select(message)
                .from(message)
                .where(keysetWhereFilter
                        .and(message.sender.id.in(senderId, receiverId))
                        .and(message.receiver.id.in(senderId, receiverId)))
                .orderBy(message.sentAt.asc())
                .limit(limit)
                .fetch();
    }
}
