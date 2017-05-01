package com.up.infant.task;

import com.up.infant.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/10 0010. 17:04
 */
public class RecommendTask extends TimerTask {

    public static final long delay = 24 * 60 * 60 * 1000;//24小时
    public static List<Map<String,Boolean>> task=new ArrayList<Map<String, Boolean>>();
    private int aId;

    public RecommendTask(int id) {
        this.aId = id;
    }

    public void run() {
        Article article = new Article().findById(aId);
        article.setIsRecommend(false);
        article.setRecommendTime(null);
        article.update();
    }
}
