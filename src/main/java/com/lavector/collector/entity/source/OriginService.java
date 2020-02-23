package com.lavector.collector.entity.source;

import java.util.List;

/**
 * Created on 18/10/2017.
 *
 * @author seveniu
 */
public interface OriginService {

    List<Origin> getByCategoryId(Long cid);

    Origin getById(Long id);

    Origin add(Origin origin);

    Origin update(Origin origin);

    void delete(Long id);
}
