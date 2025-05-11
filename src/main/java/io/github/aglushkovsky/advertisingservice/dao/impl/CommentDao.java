package io.github.aglushkovsky.advertisingservice.dao.impl;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dao.PageableAbstractDao;
import io.github.aglushkovsky.advertisingservice.entity.Comment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static io.github.aglushkovsky.advertisingservice.entity.QComment.*;

@Repository
@Transactional
public class CommentDao extends PageableAbstractDao<Comment, Long> {

    @Override
    public void delete(Long id) {
        delete(Comment.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        return findAll(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(Long id) {
        return findById(Comment.class, id);
    }

    public PageEntity<Comment> findAllByAdId(Long adId, Long limit, Long page) {
        return findAll(limit, page, comment, comment.ad.id.eq(adId), comment.createdAt.desc());
    }

    @Override
    protected JPAQuery<Comment> createFindAllQuery(EntityPathBase<Comment> fromEntityPath) {
        return new JPAQuery<>(entityManager)
                .select(comment)
                .from(comment).join(comment.author).fetchJoin();
    }
}
