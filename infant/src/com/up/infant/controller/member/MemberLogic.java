package com.up.infant.controller.member;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.ComParams;
import com.up.common.def.ResCode;
import com.up.infant.model.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/1 0001. 22:08
 */
public class MemberLogic {

    Member dao=new Member();
    MemberChild memberChildDao=new MemberChild();
    MemberAddr memberAddrDao=new MemberAddr();
    Concern concernDao=new Concern();

    public Page<Member> getPage(int pageNo,int pageSize,boolean isNormal){
        return dao.getMembers(pageNo,pageSize,isNormal);
    }

    public Map<String,Object> getDetInfo(Member mem){
        Member member=dao.findById(mem.getId());
        List<MemberChild> children=memberChildDao.getChild(mem.getId());
        List<MemberAddr> memberAddrs=memberAddrDao.getList(member.getId());
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("member",member);
        map.put("children",children);
        map.put("Addrs",memberAddrs);
        return map;
    }

    public List<MemberAddr> getAddrs(int id){
        List<MemberAddr> memberAddrs=memberAddrDao.getList(id);
        return memberAddrs;
    }

    public List<MemberChild> getChildren(int id){
        List<MemberChild> children=memberChildDao.getChild(id);
        return children;
    }


    public void addressDef(MemberAddr addr){
        if (addr.getDef()==1){
            List<MemberAddr> addrs=memberAddrDao.getList(addr.getMemberId());
            for (int i=0;i<addrs.size();i++){
                addrs.get(i).setDef(0);
            }
            Db.batchUpdate(addrs,addrs.size());
        }
        addr.save();
    }

    public Page<Concern> concernPage(ComParams params){
        if (params.is()){
            return concernDao.getFollowList(params.getPageSize(),params.getPageNo(),params.getMemberId());
        }else {
            return concernDao.getBeFollowList(params.getPageSize(),params.getPageNo(),params.getMemberId());
        }
    }

    /**
     * 关注
     * @param concern
     * @return
     */
    public ResCode follow(Concern concern){
        if (concernDao.idFollow(concern)){//已关注，取消关注
            concernDao.cancelFollow(concern);
            return ResCode.FOLLOW_CANCEL;
        }else {//未关注，关注
            concern.setCreateTime(new Date());
            concern.save();
            return ResCode.FOLLOW_SUCCESS;
        }
    }

    public Page<Collect> getCollectList(ComParams params) {
        return Collect.dao.getPageCollect(params.getPageSize(),params.getPageNo(),params.getMemberId());
    }

    public Member getMember(int memberId,int userId){
        Member member=Member.dao.otherMember(memberId,userId);
        if (member!=null){
            List<MemberChild> children = memberChildDao.getChild(memberId);
            member.put("children",children);
        }
        return member;
    }
    public List<Member> getFans(int memberId,int userId){
        String sql="SELECT t_member.`id`,t_member.`heart_img_url`,t_member.`nickname`,t_concern2.`id` AS is_follow\n" +
                " FROM  t_concern t_concern\n" +
                " LEFT JOIN t_member t_member ON t_member.`id`=t_concern.`from_member_id`\n" +
                " LEFT JOIN t_concern t_concern2 ON t_concern2.`to_member_id`=t_member.`id` AND t_concern2.`from_member_id`="+userId +
                " WHERE t_concern.`to_member_id`="+memberId;
        List<Member> list=Member.dao.find(sql);
        for (int i=0;i<list.size();i++){
            List<MemberChild> children = memberChildDao.getChild(list.get(i).getId());
            list.get(i).put("children",children);
        }
        return list;
    }

    public MemberAddr getMemberAddr(int memberId){
        String sql="SELECT * FROM t_member_addr  WHERE def=1 AND member_id="+memberId;
        return memberAddrDao.findFirst(sql);
    }

    public HashMap<String,Object> getMemberCount() {
        String sql = "SELECT COUNT(*) AS num FROM t_member t ";
        long totalMember = Member.dao.findFirst(sql).get("num");

        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
        Date dateTomrrow = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = formatter.format(dateTomrrow);
        String today = formatter.format(date);

        String sql1 ="SELECT COUNT(*) AS num FROM t_member t WHERE t.register_time> '"+yesterday+"' AND t.register_time< '"+today+"' ";
        long yesterdayMember = Member.dao.findFirst(sql1).get("num");

        String sql2 ="SELECT COUNT(*) AS num FROM t_member t WHERE t.register_time> '"+today+"'";
        long todayMember = Member.dao.findFirst(sql2).get("num");

        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("totalMember",totalMember);
        map.put("yesterdayMember",yesterdayMember);
        map.put("todayMember",todayMember);
        return  map;
    }


}
