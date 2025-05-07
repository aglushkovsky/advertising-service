package io.github.aglushkovsky.advertisingservice.dao.impl;

import io.github.aglushkovsky.advertisingservice.dao.AbstractDao;
import io.github.aglushkovsky.advertisingservice.entity.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QMessage.*;

@Repository
@Transactional
public class MessageDao extends AbstractDao<Message, Long> {
    @Override
    public void delete(Long id) {
        delete(Message.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findAll() {
        return findAll(message);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Message> findById(Long id) {
        return findById(Message.class, id);
    }
}
