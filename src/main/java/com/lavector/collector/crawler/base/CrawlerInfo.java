package com.lavector.collector.crawler.base;

import com.lavector.collector.crawler.util.JsonMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created on 04/01/2017.
 *
 * @author seveniu
 */
@Component
public class CrawlerInfo {
    private String crawlerType;
    private String projectName;
    private Date defaultStartTime;
    private Date defaultEndTime;
    private Date startTime;
    private Date endTime;
    private List<String> urls;
    private List<Words> keywords;
    private List<Words> keywordFilters;

    public String getCrawlerType() {
        return crawlerType;
    }

    public void setCrawlerType(String crawlerType) {
        this.crawlerType = crawlerType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getDefaultStartTime() {
        return defaultStartTime;
    }

    public void setDefaultStartTime(Date defaultStartTime) {
        this.defaultStartTime = defaultStartTime;
    }

    public Date getDefaultEndTime() {
        return defaultEndTime;
    }

    public void setDefaultEndTime(Date defaultEndTime) {
        this.defaultEndTime = defaultEndTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<Words> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Words> keywords) {
        this.keywords = keywords;
    }

    public List<Words> getKeywordFilters() {
        return keywordFilters;
    }

    public void setKeywordFilters(List<Words> keywordFilters) {
        this.keywordFilters = keywordFilters;
    }

    @Override
    public String toString() {
        return JsonMapper.buildNormalBinder().toJson(this);
    }


    public static class Words {
        private Set<String> words; // 必含词
        private Operation operation;
        private WordType type;

        public Set<String> getWords() {
            return words;
        }

        public void setWords(Set<String> words) {
            this.words = words;
        }

        public Operation getOperation() {
            return operation;
        }

        public void setOperation(Operation operation) {
            this.operation = operation;
        }

        public WordType getType() {
            return type;
        }

        public void setType(WordType type) {
            this.type = type;
        }

        public long size() {
            if (words == null) {
                return 0;
            }
            return words.size();
        }

        public boolean filter(String content) {
            if (this.type == WordType.CONTAIN) {
                return isContentInclude(content);
            } else if (this.type == WordType.EXCLUDE) {
                return !isContentInclude(content);
            } else {
                throw new IllegalArgumentException("unknow word type : " + this.type);
            }
        }

        public boolean isContentInclude(String content) {
            if (content == null) {
                return false;
            }
            if (this.size() == 0) {
                return true;
            }
            if (this.getOperation() == CrawlerInfo.Operation.AND) {
                for (String contain : this.getWords()) {
                    if (!content.contains(contain)) {
                        return false;
                    }
                }
                return true;
            } else if (this.getOperation() == CrawlerInfo.Operation.OR) {
                for (String contain : this.getWords()) {
                    if (content.contains(contain)) {
                        return true;
                    }
                }
                return false;
            } else {
                throw new IllegalArgumentException("unknow crawlerInfo Operation " + this.getOperation());
            }
        }
    }

    public enum WordType {
        CONTAIN, EXCLUDE
    }

    public enum Operation {
        AND, OR
    }
}
