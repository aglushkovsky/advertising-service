package io.github.aglushkovsky.advertisingservice.dao;

import io.github.aglushkovsky.advertisingservice.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentDao extends AbstractDao<Comment, Long> {
    @Override
    public void delete(Long id) {
        delete(Comment.class, id);
    }

    @Override
    public List<Comment> findAll() {
        return findAll(Comment.class);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return findById(Comment.class, id);
    }
}
