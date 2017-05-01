package com.up.infant.model.res;

import com.up.infant.model.Comment;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/10 0010. 9:33
 */
public class Alliance implements Serializable{

    private int id;
    private int memberId;
    private String memberName;
    private String memberFace;
    private int isFollow;
    private String content;
    private String imgUrl;
    private int isLike;
    private String brand1;
    private String brand2;
    private String brand3;
    private String brand4;
    private String brand5;
    private String brand6;
    private int likeCount;
    private String tags;
    private List<Comment> comments;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberFace() {
        return memberFace;
    }

    public void setMemberFace(String memberFace) {
        this.memberFace = memberFace;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public String getBrand1() {
        return brand1;
    }

    public void setBrand1(String brand1) {
        this.brand1 = brand1;
    }

    public String getBrand2() {
        return brand2;
    }

    public void setBrand2(String brand2) {
        this.brand2 = brand2;
    }

    public String getBrand3() {
        return brand3;
    }

    public void setBrand3(String brand3) {
        this.brand3 = brand3;
    }

    public String getBrand4() {
        return brand4;
    }

    public void setBrand4(String brand4) {
        this.brand4 = brand4;
    }

    public String getBrand5() {
        return brand5;
    }

    public void setBrand5(String brand5) {
        this.brand5 = brand5;
    }

    public String getBrand6() {
        return brand6;
    }

    public void setBrand6(String brand6) {
        this.brand6 = brand6;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
