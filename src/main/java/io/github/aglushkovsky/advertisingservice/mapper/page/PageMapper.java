package io.github.aglushkovsky.advertisingservice.mapper.page;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;

public interface PageMapper<E, D> {

    PageEntity<D> toDtoPage(PageEntity<E> pageEntity);
}
