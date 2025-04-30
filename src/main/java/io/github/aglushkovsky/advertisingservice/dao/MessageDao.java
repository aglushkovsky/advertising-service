package io.github.aglushkovsky.advertisingservice.dao;

import io.github.aglushkovsky.advertisingservice.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QMessage.*;

@Repository
public class MessageDao extends AbstractDao<Message, Long> {
    @Override
    public void delete(Long id) {
        delete(Message.class, id);
    }

    @Override
    public List<Message> findAll() {
        return findAll(message);
    }

    @Override
    public Optional<Message> findById(Long id) {
        return findById(Message.class, id);
    }
}
