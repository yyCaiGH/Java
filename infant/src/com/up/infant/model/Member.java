package com.up.infant.model;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.Verify;
import com.up.common.def.ResCode;
import com.up.common.utils.EncUtils;
import com.up.common.utils.Sql;
import com.up.common.utils.TextUtils;
import com.up.infant.model.base.BaseMember;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Member extends BaseMember<Member> {
    public static final Member dao = new Member();

    public static final String table = "t_member t_member";
    public static final String id = "t_member.id";
    public static final String nickname = "t_member.nickname";//昵称
    public static final String phone = "t_member.phone";//手机号
    public static final String password = "t_member.password";//密码
    public static final String face = "t_member.heart_img_url";//头像
    public static final String openId = "t_member.open_id";//微信OpenId
    public static final String consumeCount = "t_member.consume_count";//消费次数
    public static final String consumeAmount = "t_member.consume_amount";//消费总金额
    public static final String rebateCount = "t_member.rebate_count";//剩余返点数
    public static final String rTime = "t_member.register_time";//注册时间
    public static final String rFrom = "t_member.register_from";//注册来源
    public static final String vip = "t_member.vip_grade_id";//会员等级id
    public static final String uTime = "t_member.update_time";//更新信息时间
    //	public static final String rebate="t_member.rebate";//返点余额
//	public static final String oCount="t_member.order_count";//消费数量
    public static final String refundCount = "t_member.refund_count";//退货次数
    public static final String status = "t_member.status";//0:正常，1:拉黑

    private String baseResMap() {
        return Sql.select(id, nickname, phone, face, openId, consumeCount, consumeAmount, rebateCount, rTime, rFrom, vip, uTime, status);
    }

    /**
     * 普通注册
     *
     * @param phone    手机号
     * @param password 密码
     * @param from     注册来源
     * @return
     */
    public Verify<Member> register(String phone, String password, int from) {
        Verify<Member> verify = new Verify<Member>();
        Member member = findFirst("SELECT COUNT(*) AS count FROM t_member WHERE phone=" + phone);
        Long count = member.get("count");
        if (count > 0) {
            verify.setResCode(ResCode.MEMBER_HAS);
            return verify;
        }
        member = new Member();
        member.setPhone(phone);
        member.setPassword(EncUtils.MD5(password));
        member.setRegisterTime(new Date());
        member.setRegisterFrom(from);
        member.save();
        //关注数和粉丝数
        Concern concern = new Concern();
        member.put("f_count", 0);
        member.put("t_count", 0);
        verify.setData(member);
        verify.setResCode(ResCode.SUCCESS);
        return verify;
    }

    public Verify<Member> login(String mobile, String pwd) {
        Verify<Member> verify = new Verify<Member>();
        Map<String, String> map = new HashMap<String, String>();
        map.put(phone, mobile);
        map.put(password, EncUtils.MD5(pwd));
        String sql = baseResMap() + Sql.from(table) + Sql.where(map);
        Member member = findFirst(sql);
        if (null != member.getId()) {
            verify.setResCode(ResCode.LOGIN_SUCCESS);
            //关注数和粉丝数
            Concern concern = new Concern();
            member.put("f_count", concern.getFollowCount(member.getId()));
            member.put("t_count", concern.getFansCount(member.getId()));
            verify.setData(member);
        } else {
            verify.setResCode(ResCode.PP_ERROR);
        }
        return verify;
    }

    /**
     * 分页获取会员列表
     *
     * @param pageNo
     * @param pageSize
     * @param isNormal
     * @return
     */
    public Page<Member> getMembers(int pageNo, int pageSize, boolean isNormal) {
        String select = Sql.select(id, nickname, rFrom, vip, rebateCount, consumeCount, refundCount, rTime);
        String from = Sql.from(table);
        Map<String, String> map = new HashMap<String, String>();
        if (isNormal) {
            map.put(status, "0");
        } else {
            map.put(status, "1");
        }
        String where = Sql.where(map);
        return paginate(pageNo, pageSize, select, from + where + Sql.orderBy(rTime, false));
    }

    /**
     * 获取会员列表
     *
     * @param status
     * @return
     */
    public List<Member> getAllMembers(int status) {
        String sql = "select * from t_member where status = "+status;
        return  dao.find(sql);
    }

    public Member info(int id) {
        Member member = findById(id);
        //关注数和粉丝数
        Concern concern = new Concern();
        member.put("f_count", concern.getFollowCount(member.getId()));
        member.put("t_count", concern.getFansCount(member.getId()));

        //订单数
        Order order=new Order();
        member.put("o_sta1",order.getCountBySta(member.getId(),1));//待付款
        member.put("o_sta2",order.getCountBySta(member.getId(),2));//待发货
        member.put("o_sta3",order.getCountBySta(member.getId(),3));//待收货

        return member;
    }

    public Member otherMember(int memberId,int userId) {
        String sql="SELECT t_member.`id`,t_member.`nickname`,t_member.`heart_img_url`,t_concern.`id` AS is_follow\n" +
                " FROM t_member\n" +
                " LEFT JOIN t_concern t_concern ON \n" +
                " t_concern.`from_member_id`="+userId +
                " AND t_concern.`to_member_id`=t_member.`id`\n" +
                " WHERE t_member.`id`="+memberId;
        Member member = findFirst(sql);
        //关注数和粉丝数
        Concern concern = new Concern();
        member.put("f_count", concern.getFollowCount(member.getId()));
        member.put("t_count", concern.getFansCount(member.getId()));
        return member;
    }

    /**
     * 后台
     * @return
     */
    public Member getById(int id){
        String sql = "SELECT m.*,vg.name AS grade_name"
                +" from t_member AS m"
                +" LEFT JOIN t_vip_grade AS vg ON vg.id = m.vip_grade_id"
                +" WHERE m.id="+id;
        return dao.findFirst(sql);
    }
    public Page<Member> getList(Member member, int pageNo, int pageSize) {
        String select = "SELECT m.*,vg.name AS grade_name";
        String sqlExceptSelect = "from t_member AS m"+
                " LEFT JOIN t_vip_grade AS vg ON vg.id = m.vip_grade_id"+
                " WHERE 1=1";

        if (member.getStatus()!=null){
            sqlExceptSelect +=" AND m.status="+member.getStatus();
        }
        if (member.getVipGradeId()!=null){
            sqlExceptSelect +=" AND m.vip_grade_id="+member.getVipGradeId();
        }
        if(!TextUtils.isEmpty(member.getNickname())){
            sqlExceptSelect +=" AND (m.nickname like '%"+member.getNickname()+"%' OR m.phone='"+member.getNickname()+"')";
        }
        sqlExceptSelect +=" order by m.id asc";
        return paginate(pageNo, pageSize, select, sqlExceptSelect);
    }

    /**
     * 联想搜索会员
     * @param member
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<Member> getMemberList(Member member, int pageNo, int pageSize) {
        String select = "SELECT m.*,vg.name AS grade_name,vg.discount";
        String sqlExceptSelect = "from t_member AS m"+
                " LEFT JOIN t_vip_grade AS vg ON vg.id = m.vip_grade_id"+
                " WHERE 1=1";

        if (member.getStatus()!=null){
            sqlExceptSelect +=" AND m.status="+member.getStatus();
        }
        if(!TextUtils.isEmpty(member.getNickname())){
            sqlExceptSelect +=" AND (m.nickname like '"+member.getNickname()+"%' OR m.phone like'"+member.getNickname()+"%' OR m.id like'"+member.getNickname()+"%')";
        }
        sqlExceptSelect +=" order by m.id asc";
        return paginate(pageNo, pageSize, select, sqlExceptSelect);
    }

    public Member findMember(int id) {
        String select = "SELECT m.*,vg.name AS grade_name,vg.discount";
        String sqlExceptSelect = " from t_member AS m"+
                " LEFT JOIN t_vip_grade AS vg ON vg.id = m.vip_grade_id"+
                " WHERE m.id="+id;
        String sql = select+sqlExceptSelect;
        return findFirst(sql);
    }
}