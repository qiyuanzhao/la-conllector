package com.lavector.collector.entity.newsAggregation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 28/09/2017.
 *
 * @author seveniu
 */
@Entity
public class NewsAggregation extends BaseAuditableSqlEntity {
    @Lob
    private String aggregation;
    @Enumerated(EnumType.STRING)
    private NewsAggregationType type;
    private Long cid;
    @Transient
    private List<SortAggregationData> aggregationData;

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public NewsAggregationType getType() {
        return type;
    }

    public void setType(NewsAggregationType type) {
        this.type = type;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public List<SortAggregationData> getAggregationData() {
        if (aggregationData == null) {
            try {
                List<List<NewsAggregationData>> aggregationsDataListList = JsonMapper.buildNonNullBinder().getMapper().readValue(this.aggregation, new TypeReference<List<List<NewsAggregationData>>>() {
                });
                this.aggregationData = aggregationsDataListList.stream().map(
                        a -> {
                            Double score = 0.0d;
                            List<Long> ids = new LinkedList<>();
                            for (NewsAggregation.NewsAggregationData newsAggregationData : a) {
                                score += newsAggregationData.getScore();
                                ids.add(newsAggregationData.getNewId());
                            }
                            return new SortAggregationData(score, ids);
                        }
                ).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aggregationData;
    }

//    public void setAggregationData(List<List<NewsAggregationData>> aggregationData) {
//        this.aggregationData = aggregationData;
//    }

    private static class NewsAggregationData {
        private Double score;
        private Long newId;

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public Long getNewId() {
            return newId;
        }

        public void setNewId(Long newId) {
            this.newId = newId;
        }
    }

    public static class SortAggregationData {
        private Double score;
        private List<Long> ids;

        public SortAggregationData(Double score, List<Long> ids) {
            this.score = score;
            this.ids = ids;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public List<Long> getIds() {
            return ids;
        }

        public void setIds(List<Long> ids) {
            this.ids = ids;
        }
    }
}
