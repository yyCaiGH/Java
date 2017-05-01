package com.up.infant.controller.article;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.up.common.def.FileType;
import com.up.common.def.ResCode;
import com.up.common.utils.FileUtils;
import com.up.common.utils.TextUtils;
import com.up.infant.model.*;
import com.up.infant.model.res.Alliance;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/9 0009. 17:54
 */
public class ArticleLogic {
    Article dao = new Article();
    Comment commentDao = new Comment();
    Member memberDao = new Member();
    MemberChild memberChildDao = new MemberChild();


    public void addPageView(int id) {
        dao.addPageView(id);
    }

    public ResCode addComment(Comment comm) {
        Article article = dao.findById(comm.getArticleId());
        if (null == article) {//文章不存在
            return ResCode.ARTICLE_NO;
        }
        Member member = memberDao.findById(comm.getMemberId());
        if (null == member) {//用户不存在
            return ResCode.MEMBER_NO;
        }
        if (comm.getType() == 1) {//评论
            if (TextUtils.isEmpty(comm.getContent())) {
                return ResCode.COMMENT_NULL;
            }
        }
        if (comm.getType() == 0) {//点赞
            Comment comment = commentDao.hasLike(comm.getMemberId(), comm.getArticleId());
            if (null != comment) {
                Db.deleteById("t_comment", comment.getId());
                String sql2 = "update t_article set like_count =like_count-1 where id=" + comm.getArticleId();
                Db.update(sql2);
                return ResCode.CANCEL_LIKE;
            }
        }
        comm.setCreateTime(new Date());
        comm.save();
        dao.addComment(comm.getArticleId(), comm.getType());
        return ResCode.SUCCESS;
    }

    public Page<Article> getAlliance(int pageSize, int pageNo, Integer memberId) {
        Page<Article> page = dao.getArticles(pageSize, pageNo, memberId + "");
        List<Article> articles = page.getList();
        for (int i = 0; i < articles.size(); i++) {
            List<Comment> commentList = commentDao.getSimpleComment(articles.get(i).getId());
            List<MemberChild> children = memberChildDao.getChild(articles.get(i).getMemberId());
            articles.get(i).put("commentList", commentList);
            articles.get(i).put("children", children);
        }
        Page<Article> articlePage = new Page<Article>(articles, page.getPageNumber(), page.getPageSize(), page.getTotalPage(), page.getTotalRow());
        return articlePage;
    }

    public Page<Article> getMemberArticle(int pageNo, int pageSize, int memberId, int userId) {
        Page<Article> page = dao.getMemberArticles(pageSize, pageNo, memberId, userId);
        List<Article> articles = page.getList();
        for (int i = 0; i < articles.size(); i++) {
            List<Comment> commentList = commentDao.getSimpleComment(articles.get(i).getId());
            articles.get(i).put("commentList", commentList);
        }
        Page<Article> articlePage = new Page<Article>(articles, page.getPageNumber(), page.getPageSize(), page.getTotalPage(), page.getTotalRow());
        return articlePage;
    }


    public Page<Article> getArticles(int pageSize, int pageNo, Integer memberId) {
        Page<Article> articlePage = dao.getArticles(pageSize, pageNo, memberId);
        return articlePage;

    }

    public ResCode updateArt(String type, int id) {
        if (type.equals("0")) {//置顶
            dao.top(id);//如果已置顶则取消，否则置顶
        } else if (type.equals("1")) {//推荐
            dao.recommend(id);
        } else if (type.equals("2")) {//隐藏
            dao.updateSta(id);
        } else {
            return ResCode.PARAM_ERROR;
        }
        return ResCode.SUCCESS;
    }

    public ResCode publish(Article article,String base64){
        try {
            String  path = FileType.ARTICLE.getPath() + article.getMemberId() + "-" + article.getId() + ".png";
            if (!FileUtils.base64ToImage(base64, PathKit.getWebRootPath() + "/" + path)) {//上传失败
                article.delete();
                return ResCode.ERROR_IMAGE;
            }
            article.setImgUrl(path);
            article.update();
            return ResCode.PIC_SUCCESS;
        }catch (Exception e){
            article.delete();
            return ResCode.ERROR;
        }

    }


    public ResCode publishPic(int type, int articleId, String base64, int tagId, int memberId) {

        Article article = dao.findById(articleId);
        if (null == article) {
            return ResCode.ARTICLE_NO;
        }
        String path;
        if (type == 0) {//文章图片
            path = FileType.ARTICLE.getPath() + memberId + "-" + articleId + ".png";
            if (!FileUtils.base64ToImage(base64, PathKit.getWebRootPath() + "/" + path)) {//上传失败
                article.delete();
                return ResCode.ERROR_IMAGE;
            }
            article.setImgUrl(path);
//            article.update();
//            Db.update("t_article",article);
            Db.update("update  t_article SET img_url ='" + path + "' where id=" + article.getId());
            return ResCode.PIC_SUCCESS;
        } else {//tag图片
            path = FileType.ARTICLE.getPath() + memberId + "-" + articleId + "-" + tagId + ".png";
            if (!FileUtils.base64ToImage(base64, PathKit.getWebRootPath() + "/" + path)) {//上传失败
                return ResCode.ERROR_IMAGE;
            }


            JSONArray tagsJson = (JSONArray) JSONValue.parse(article.getTags());
            JSONArray newJsonArray = new JSONArray();


            for (int i = 0; i < tagsJson.size(); i++) {
                Long id = (Long) ((JSONObject) tagsJson.get(i)).get("id");
                if (tagId == id) {
                    ((JSONObject) tagsJson.get(i)).put("img", path);
                }
            }
            article.setTags(tagsJson.toJSONString());
            Db.update("update  t_article SET tags ='" + article.getTags() + "' where id=" + article.getId());
            JSONArray newJson = (JSONArray) JSONValue.parse(article.getTags());
            int tagSuccessSuccess = 0;
            for (int i = 0; i < newJson.size(); i++) {
                String img = (String) ((JSONObject) tagsJson.get(i)).get("img");
                if (img.contains(FileType.ARTICLE.getPath() + memberId + "-" + articleId)) {
                    tagSuccessSuccess++;
                }
            }
            if (tagSuccessSuccess == tagsJson.size()) {//标签全部上传完毕
                return ResCode.PUBLISH_SUCCESS;
            } else {
                return ResCode.TAG_SUCCESS;
            }

        }

    }

    public Article getArticleInfo(int id, int memberId) {

//        List<MemberChild> children=memberChildDao.getChild(articles.get(i).getMemberId());
        Article article = Article.dao.article(id, memberId);
        article.setPageView(article.getPageView()+1);
        article.update();
        List<Comment> commentList = commentDao.getArticleComment(id, 1);//评论
        List<Comment> likeList = commentDao.getArticleComment(id, 0);//点赞
        article.put("commentList", commentList);
        article.put("likeList", likeList);
        return article;
    }


    public class ArticleTag {
        private int id;
        private String imgUrl;
        private String tag;
        private double relX;
        private double relY;
        private String type;
        private Goods goods;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public double getRelX() {
            return relX;
        }

        public void setRelX(double relX) {
            this.relX = relX;
        }

        public double getRelY() {
            return relY;
        }

        public void setRelY(double relY) {
            this.relY = relY;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Goods getGoods() {
            return goods;
        }

        public void setGoods(Goods goods) {
            this.goods = goods;
        }
    }

}
