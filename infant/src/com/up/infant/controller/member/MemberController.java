package com.up.infant.controller.member;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.up.common.base.ComParams;
import com.up.common.base.Verify;
import com.up.common.controller.BaseController;
import com.up.common.def.FileType;
import com.up.common.def.ResCode;
import com.up.common.utils.EncUtils;
import com.up.common.utils.FileUtils;
import com.up.common.utils.MobileUtils;
import com.up.common.utils.TextUtils;
import com.up.infant.model.*;

import java.io.File;
import java.util.*;

/**
 * TODO:会员控制器
 * <p>
 * Created by 王剑洪 on 2016/10/31.
 */
public class MemberController extends BaseController {

    private MemberLogic logic = new MemberLogic();


    /**
     * 会员普通注册
     */
    public void register() {
        String[] params = {"phone", "sms", "password", "from"};
        Verify<Map<String, String>> verify = strVerify(params);
        if (verify.getCode() < 0) {
            resFailure(verify.getMsg());
            return;
        }
        if (!MobileUtils.is(verify.getData().get(params[0]))) {
            verify.setResCode(ResCode.PHONE_FORMAT);
            resFailure(ResCode.PHONE_FORMAT.getMsg());
            return;
        }
        //验证码验证
        String smsCode = getSession().getAttribute("smsCode").toString();
        String sessionPhone = getSession().getAttribute("phone").toString();
        if (!smsCode.equals(verify.getData().get(params[1])) || !sessionPhone.equals(verify.getData().get(params[0]))) {
            resFailure("验证码不正确");
            return;
        }
        //验证码验证
        String password = verify.getData().get(params[2]);
        if (password.length() < 6 || password.length() > 12) {
            resFailure(ResCode.PWD_FORMAT.getMsg());
            return;
        }
        try {
            Verify<Member> memberVerify = new Member().register(verify.getData().get(params[0]), verify.getData().get(params[2]),
                    Integer.parseInt(verify.getData().get(params[3])));
            if (memberVerify.getCode() < 0) {
                resFailure(memberVerify.getMsg());
            } else {
                resSuccess(ResCode.REGISTER_SUCCESS.getMsg(), memberVerify.getData());
            }
        } catch (Exception e) {
            logger.error(e);
            resFailure(ResCode.PARAM_FORMAT.getMsg());
        }
    }

    /**
     * 会员普通登陆
     */
    public void login() {
        String[] params = {"phone", "password"};
        Verify<Map<String, String>> verify = strVerify(params);
        if (verify.getCode() < 0) {
            resFailure(verify.getMsg());
            return;
        }
        try {
            Verify<Member> memberVerify = new Member().login(verify.getData().get(params[0]), verify.getData().get(params[1]));
            if (memberVerify.getCode() < 0) {
                resFailure(memberVerify.getMsg());
            } else {
                Member member = memberVerify.getData();
                if (member.getStatus() > 0) {
                    res(ResCode.STATUS_ERROR);
                } else {
                    resSuccess(ResCode.LOGIN_SUCCESS.getMsg(), memberVerify.getData());
                }
            }
        } catch (Exception e) {
            logger.error(e);
            resFailure(ResCode.PARAM_FORMAT.getMsg());
        }
    }

    /**
     * 会员个人信息，地址列表，孩子列表
     */
    public void memberDetail() {
        Member member = getModel(Member.class, "");
        HashMap<String, Object> map = new HashMap<String, Object>();
        member = member.getById(member.getId());
        map.put("member", member);
        List<MemberChild> childrenList = logic.getChildren(member.getId());
        map.put("childrenList", childrenList);
        List<MemberAddr> addressList = logic.getAddrs(member.getId());
        map.put("addressList", addressList);
        resSuccess(map);
    }

    public void memberInfo() {
        Member member = getModel(Member.class, "");

        resSuccess(member.info(member.getId()));
    }


    /**
     * 获取会员列表
     */
    public void getList() {
        ComParams params = getBean(ComParams.class, "");
        Page<Member> page = logic.getPage(params.getPageNo(), params.getPageSize(), params.is());
        resSuccess(page);
    }

    /**
     * 获取会员总人数（包括黑名单），今日新增人数，昨日新增人数
     */
    public void getMemberCount() {
        try {
            resSuccess(logic.getMemberCount());
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 加入搜索功能
     */
    public void getPageList() {
        try {
            int pageNo = Integer.parseInt(getPara("pageNo"));
            int pageSize = Integer.parseInt(getPara("pageSize"));
            Member member = getModel(Member.class, "", true);
            Page<Member> page = Member.dao.getList(member, pageNo, pageSize);
            resSuccess(page);
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 联想搜索会员
     */
    public void getMemberList() {
        try {
            int pageNo = Integer.parseInt(getPara("pageNo"));
            int pageSize = Integer.parseInt(getPara("pageSize"));
            Member member = getModel(Member.class, "", true);
            Page<Member> page = Member.dao.getMemberList(member, pageNo, pageSize);
            resSuccess(page);
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 拉黑，取消拉黑
     */
    public void updateStatus() {
        try {
            String idStr = getPara("id");
            int id = Integer.parseInt(idStr);
            int status = Integer.parseInt(getPara("status"));
            Member member = new Member().findById(id);
            if (null == member) {
                res(ResCode.MEMBER_NO);
                return;
            }
            member.setStatus(status);
            member.update();
            res(ResCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 包括会员打折信息
     */
    public void getMemberDesById() {
        int id = Integer.parseInt(getPara("id"));
        Member member = Member.dao.findMember(id);
        resSuccess(member);
    }

    /**
     * 获取会员详情
     */
    public void getDetInfo() {
        Member member = getModel(Member.class, "");
        Map<String, Object> map = logic.getDetInfo(member);
        resSuccess(map);
    }

    public void addChild() {
        MemberChild child = getModel(MemberChild.class, "");
        child.save();
        res(ResCode.SAVE_SUCCESS);
    }

    public void editChild() {
        MemberChild child = getModel(MemberChild.class, "");
        if (null == child.getId()) {
            child.save();
            res(ResCode.SAVE_SUCCESS);
        } else {
            child.update();
            res(ResCode.UPDATE_SUCCESS);
        }

    }

    public void getChildren() {
        String idStr = getPara("id");
        int id = Integer.parseInt(idStr);
        resSuccess(logic.getChildren(id));
    }

    public void updateAddress() {
        MemberAddr addr = getModel(MemberAddr.class, "");
        if (addr.getId() != null) {
            addr.update();
            res(ResCode.UPDATE_SUCCESS);
        } else {
            logic.addressDef(addr);
            res(ResCode.SAVE_SUCCESS);
        }
    }

    public void addAddress() {
        MemberAddr addr = getModel(MemberAddr.class, "");
        logic.addressDef(addr);
        res(ResCode.SAVE_SUCCESS);
    }

    public void getAddrs() {
        String idStr = getPara("id");
        int id = Integer.parseInt(idStr);
        resSuccess(logic.getAddrs(id));
    }

    public void concern() {
        ComParams params = getBean(ComParams.class, "");
        Page<Concern> page = logic.concernPage(params);
        resSuccess(page);
    }

    public void follow() {
        Concern concern = getModel(Concern.class, "");
        ResCode code = logic.follow(concern);
        res(code);
    }


    /**
     * 更新头像
     */
    public void updateFace() {
        String face = getPara("face");
        String idStr = getPara("id");
        int id = Integer.parseInt(idStr);
        Member member = new Member().findById(id);
        if (null == member) {
            res(ResCode.MEMBER_NO);
            return;
        }
        String path = FileType.FACE.getPath() + member.getId() + ".png";
        if (FileUtils.base64ToImage(face, rootPath + path)) {
            member.setHeartImgUrl(path);
            member.update();
            resSuccess(ResCode.SUCCESS.getMsg(), member.getHeartImgUrl());
        } else {
            res(ResCode.ERROR);
        }
    }

    public void update() {
        String nickname = getPara("nickname");
        String idStr = getPara("id");
        int id = Integer.parseInt(idStr);
        Member member = new Member().findById(id);
        if (null == member) {
            res(ResCode.MEMBER_NO);
            return;
        }
        if (!TextUtils.isEmpty(nickname)) {
            member.setNickname(nickname);
        }
        member.update();
        res(ResCode.SUCCESS);
    }

    public void getCollectList() {
        ComParams comParams = getBean(ComParams.class, "");
        Page<Collect> collectList = logic.getCollectList(comParams);
        resSuccess(collectList);
    }

    public void getMemberInfo() {
        int memberId = getParaToInt("memberId");
        int userId = getParaToInt("userId");
        resSuccess(logic.getMember(memberId, userId));
    }

    public void getFans() {
        int memberId = getParaToInt("memberId");
        int userId = getParaToInt("userId");
        resSuccess(logic.getFans(memberId, userId));
    }

    public void getDefAddress() {
        int memberId = getParaToInt("memberId");
        resSuccess(logic.getMemberAddr(memberId));
    }

    /**
     * openId
     */
    public void wxLogin() {
        String openId = getPara("openId");
        int  form=getParaToInt("form");
        String sql = "SELECT *  FROM t_member WHERE open_id='" + openId+"'";
        Member member = Member.dao.findFirst(sql);
        if (null != member) {
            Concern concern = new Concern();
            member.put("f_count", concern.getFollowCount(member.getId()));
            member.put("t_count", concern.getFansCount(member.getId()));
            resSuccess(member);
            return;
        } else {
            member = new Member();
            member.setOpenId(openId);
            member.setPhone("");
            member.setPassword("");
            member.setRegisterTime(new Date());
            member.setRegisterFrom(form);
            member.setConsumeAmount(0.00);
            member.setConsumeCount(0);
            member.setRebateCount(0.00);
            member.setRefundCount(0);
            VipGrade vipGrade=VipGrade.dao.getFistGrade();
            if (null!=vipGrade){
                member.setVipGradeId(vipGrade.getId());
            }
            member.save();
            member.put("f_count", 0);
            member.put("t_count", 0);
            resSuccess(member);
        }
    }

    /**
     * 绑定手机号
     */
    public void bindPhone() {
        String phone = getPara("phone");
        String openId = getPara("openId");
        String code = getPara("sms");
        String password = getPara("password");
        if (TextUtils.isEmpty(openId) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(code) || TextUtils.isEmpty(password)) {
            resFailure("请传递完整参数！");
            return;
        }
        if (!MobileUtils.is(phone)) {
            resFailure(ResCode.PHONE_FORMAT.getMsg());
            return;
        }
        //验证码验证
        String smsCode = getSession().getAttribute("smsCode").toString();
        String sessionPhone = getSession().getAttribute("phone").toString();
        if (!smsCode.equals(code) || !sessionPhone.equals(phone)) {
            resFailure("验证码不正确");
            return;
        }
        String sql = "SELECT *  FROM t_member WHERE open_id='" + openId+"'";
        Member member = Member.dao.findFirst(sql);
        if (null == member) {
            resFailure("无此微信账号！");
            return;
        }
        member.setPhone(phone);
        member.setPassword(EncUtils.MD5(password));
        member.update();
        Concern concern = new Concern();
        member.put("f_count", concern.getFollowCount(member.getId()));
        member.put("t_count", concern.getFansCount(member.getId()));
        resSuccess(member);
    }

    /**
     * 找回密码
     */
    public void findPassword() {
        String phone = getPara("phone");
        String code = getPara("sms");
        String password = getPara("password");
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(code) || TextUtils.isEmpty(password)) {
            resFailure("请传递完整参数！");
            return;
        }
        if (!MobileUtils.is(phone)) {
            resFailure(ResCode.PHONE_FORMAT.getMsg());
            return;
        }
        //验证码验证
        String smsCode = getSession().getAttribute("smsCode").toString();
        String sessionPhone = getSession().getAttribute("phone").toString();
        if (!smsCode.equals(code) || !sessionPhone.equals(phone)) {
            resFailure("验证码不正确");
            return;
        }
        Member member = Member.dao.findFirst("SELECT *  FROM t_member WHERE phone=" + phone);
        member.setPassword(EncUtils.MD5(password));
        member.update();
        Concern concern = new Concern();
        member.put("f_count", concern.getFollowCount(member.getId()));
        member.put("t_count", concern.getFansCount(member.getId()));
        resSuccess(member);

    }

    public void delCollect(){
        String idStr=getPara("ids");
        int memberId=getParaToInt("memberId");
        String[] idTemps=idStr.split(",");
        List<String> sqlList=new ArrayList<String>();
        for (String id:idTemps){
            sqlList.add("   DELETE  FROM t_collect WHERE id="+id+" AND member_id="+memberId);
        }
        Db.batch(sqlList,sqlList.size());
        res(ResCode.SUCCESS);
    }

}
