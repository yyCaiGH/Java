package com.up.infant.controller.article;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.up.common.base.ComParams;
import com.up.common.controller.BaseController;
import com.up.common.def.FileType;
import com.up.common.def.ResCode;
import com.up.common.utils.FileUtils;
import com.up.infant.model.Article;
import com.up.infant.model.Comment;
import com.up.infant.model.res.Alliance;

import java.util.Date;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/9 0009. 17:44
 */
public class ArticleController extends BaseController{

    ArticleLogic logic=new ArticleLogic();

    /**
     * 文章发布，文本消息，要和publishPic结合起来用
     */
    public void publish(){
        Article article=getModel(Article.class,"");
        article.setCreateTime(new Date());
        article.save();
        resSuccess(ResCode.PUBLISH_TEXT_SUCCESS.getMsg(),article.getId());
    }

    /**
     * 文章发布，图片，要和publish结合起来用
     */
    public void publishPic(){
//        int type=getParaToInt("type");
        String base64=getPara("base64");
        int memberId=getParaToInt("memberId");
        int articleId=getParaToInt("articleId");
        int tagId=getParaToInt("tagId");
        ResCode code=logic.publishPic(0,articleId,base64,tagId,memberId);
        res(code.getCode(),code.getMsg(),articleId);
    }

    public void publishArticle(){
        Article article=getModel(Article.class,"");
        String base64=article.getImgUrl();
        article.setImgUrl("");
        article.setCreateTime(new Date());
        article.save();
        ResCode resCode=logic.publish(article,base64);
        res(resCode);
    }



    public void addPageView(){
        Article article=getModel(Article.class,"");
        logic.addPageView(article.getId());
        res(ResCode.SUCCESS);
    }

    public void comment(){
        Comment comment=getModel(Comment.class,"");
        res(logic.addComment(comment));
    }

    public void getArticlesByMember(){
        ComParams pageParams=getBean(ComParams.class,"");
        Page<Article> page=logic.getArticles(pageParams.getPageSize(),pageParams.getPageNo(),pageParams.getMemberId());
        resSuccess(page);
    }

    /**
     * 客户端联盟
     */
    public void alliance(){
        ComParams pageParams=getBean(ComParams.class,"");
        Page<Article> page=logic.getAlliance(pageParams.getPageSize(),pageParams.getPageNo(),pageParams.getMemberId());
        resSuccess(page);
    }

    public void getMemberArticle(){
        int pageNo=getParaToInt("pageNo");
        int pageSize=getParaToInt("pageSize");
        int memberId=getParaToInt("memberId");
        int userId=getParaToInt("userId");
        Page<Article> page=logic.getMemberArticle(pageNo,pageSize,memberId,userId);
        resSuccess(page);

    }


    public void updateArticle(){
        String type=getPara("type");
        String idStr=getPara("artId");
        int id=Integer.parseInt(idStr);
        logic.updateArt(type,id);
        res(ResCode.SUCCESS);
    }

    public void getAll(){
        ComParams params=getBean(ComParams.class,"");
        Page<Article> page=new Article().getArticle(params);
        resSuccess(page);
    }

    public void getComment(){
        ComParams params=getBean(ComParams.class,"");
        Page<Comment> page=new  Comment().all(params);
        resSuccess(page);
    }

    /**
     * 加入搜索
     */
    public void getComments(){
        try{
            int pageNo = Integer.parseInt(getPara("pageNo"));
            int pageSize = Integer.parseInt(getPara("pageSize"));
            Comment comment=getModel(Comment.class,"",true);
            Page<Comment> goodsPage = Comment.dao.getList(comment,pageNo,pageSize);
            resSuccess(goodsPage);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }
    public void updateComment(){
        Comment comment=getModel(Comment.class,"");
        comment.update();
        res(ResCode.SUCCESS);
    }

    public void updateCommentStatus(){
        Comment comment=getModel(Comment.class,"");
        int status = comment.getStatus();
        comment = Comment.dao.findById(comment.getId());
        comment.setStatus(status);
        comment.update();
        res(ResCode.SUCCESS);
    }

    public void info(){
        int id=getParaToInt("id");
        int memberId=getParaToInt("memberId");
        resSuccess(logic.getArticleInfo(id,memberId));
    }






}
