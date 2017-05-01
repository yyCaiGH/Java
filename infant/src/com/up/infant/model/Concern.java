package com.up.infant.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.up.common.utils.Sql;
import com.up.infant.model.base.BaseConcern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关注
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Concern extends BaseConcern<Concern> {
    public static final Concern dao = new Concern();

    public static final String table = "t_concern t_concern";
    public static final String id = "t_concern.id";
    public static final String fromId = "t_concern.from_member_id";
    public static final String toId = "t_concern.to_member_id";
    public static final String createTime = "t_concern.create_time";

    /**
     * 获取用户关注列表
     *
     * @param pageSize
     * @param pageNo
     * @param memberId
     * @return
     */
    public Page<Concern> getFollowList(int pageSize, int pageNo, int memberId) {
        String select = "Select  t_concern.id,t_concern.from_member_id,t_member.nickname,t_member.heart_img_url,t_concern.create_time";
        String sql = " FROM t_concern t_concern\n" +
                "LEFT JOIN t_member t_member ON t_member.id=t_concern.to_member_id\n" +
                "WHERE t_concern.from_member_id=" + memberId;
        return paginate(pageNo, pageSize, select, sql);

    }

    /**
     * 获取用户被关注列表
     *
     * @param pageSize
     * @param pageNo
     * @param memberId
     * @return
     */
    public Page<Concern> getBeFollowList(int pageSize, int pageNo, int memberId) {
        String select = "Select  t_concern.id,t_concern.from_member_id,t_member.nickname,t_member.heart_img_url,t_concern.create_time";
        String sql = " FROM t_concern t_concern\n" +
                "LEFT JOIN t_member t_member ON t_member.id=t_concern.from_member_id\n" +
                "WHERE t_concern.to_member_id=" + memberId;
        return paginate(pageNo, pageSize, select, sql);
    }

    /**
     * 是否关注过
     *
     * @param concern
     * @return
     */
    public boolean idFollow(Concern concern) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(fromId, concern.getFromMemberId() + "");
        map.put(toId, concern.getToMemberId() + "");
        String sql = Sql.select("count(*) AS count") + Sql.from(table) + Sql.where(map);
        Concern res = findFirst(sql);
        if (null == res) {
            return false;
        }
        if (res.getLong("count") < 1) {
            return false;
        } else {
            return true;
        }
    }

    public void cancelFollow(Concern concern) {
        Db.update("delete from t_concern where from_member_id=? and to_member_id =? ", concern.getFromMemberId(), concern.getToMemberId());
    }

    /**
     * 获取关注数量
     * @param fromMemberId
     * @return
     */
    public Long getFollowCount(int fromMemberId) {
        String sql = "SELECT COUNT(to_member_id) AS f_count FROM t_concern WHERE from_member_id=" + fromMemberId;
        return findFirst(sql).getLong("f_count");
    }

    /**
     * 获取粉丝数量
     * @param toMemberId
     * @return
     */
    public Long getFansCount(int toMemberId) {
        String sql = "SELECT COUNT(from_member_id) AS t_count FROM t_concern WHERE to_member_id=" + toMemberId;
        return findFirst(sql).getLong("t_count");
    }


}