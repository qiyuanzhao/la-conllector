package com.lavector.collector.crawler.project.gengmei.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.gengmei.entity.question.AnswerInfo;
import com.lavector.collector.crawler.project.gengmei.entity.question.Question;
import com.lavector.collector.crawler.project.gengmei.entity.question.QuestionInfo;
import com.lavector.collector.crawler.util.RegexUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.List;

public class QuestionPage implements PageParse {


    @Override
    public boolean handleUrl(String url) {

        return RegexUtil.isMatch("https://www.soyoung.com/question/list.*", url);
    }

    @Override
    public Result parse(Page page) {
        String text = page.getRawText();
        System.out.println(text);

        List<Question> questionList = new ArrayList<>();
        JSONObject jsonObject = JSONObject.fromObject(text);
        JSONArray jsonArray = jsonObject.getJSONObject("responseData").getJSONArray("question");
        for (Object o : jsonArray) {
            JSONObject object = JSONObject.fromObject(o);
            JSONObject answerInfo = object.getJSONObject("answer_info");
            answerInfo.remove("cover_img");
            answerInfo.remove("imgs");
            answerInfo.remove("user");
            answerInfo.remove("video");
            answerInfo.remove("audio");
            answerInfo.remove("video_cover");
            answerInfo.remove("video_gif");

            AnswerInfo answerInfo1 = (AnswerInfo) JSONObject.toBean(answerInfo, AnswerInfo.class);

            JSONObject questionInfo = object.getJSONObject("question_info");
            questionInfo.remove("tags");
            questionInfo.remove("cover_img");
            questionInfo.remove("imgs");

            QuestionInfo questionInfo1 = (QuestionInfo) JSONObject.toBean(questionInfo, QuestionInfo.class);

            questionInfo1.setUrl("https://y.soyoung.com/question/getQuestionInfo?question_id=" + questionInfo1.getQuestion_id());

            Question question = new Question();

            String content = answerInfo1.getContent();
            String question_content = questionInfo1.getQuestion_content();
            if (!StringUtils.isEmpty(content)) {
                String replace = content.replace("\n", "");
                String newContent = replace.replace(",", "，");
                String cont = newContent.replace("\t", "");
                answerInfo1.setContent(cont);
            }
            if (!StringUtils.isEmpty(question_content)) {
                String replace = question_content.replace("\n", "");
                String newContent = replace.replace(",", "，");
                String cont = newContent.replace("\t", "");
                questionInfo1.setQuestion_content(cont);
            }
            question.setAnswer_info(answerInfo1);
            question.setQuestion_info(questionInfo1);
            questionList.add(question);
        }
        if (jsonArray.size() > 0) {
            String url = page.getUrl().get();
            String number = url.substring(url.indexOf("page=") + 5);
            int anInt = Integer.parseInt(number) + 1;
            if (anInt <= 100) {
                Request request = new Request("https://www.soyoung.com/question/list?keyword=%E6%B6%A6%E5%88%B6&cityId=&page_size=100&_json=1&page=" + anInt);
                page.addTargetRequest(request);
            }
        }

        page.getRequest().putExtra("questionList", questionList);
        page.putField("questionList", questionList);


        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
