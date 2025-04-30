package io.github.aglushkovsky.advertisingservice.dao;

import io.github.aglushkovsky.advertisingservice.entity.Comment;
import io.github.aglushkovsky.advertisingservice.entity.QComment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QComment.*;

@Repository
public class CommentDao extends AbstractDao<Comment, Long> {
    @Override
    public void delete(Long id) {
        delete(Comment.class, id);
    }

    @Override
    public List<Comment> findAll() {
        return findAll(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return findById(Comment.class, id);
    }
}
