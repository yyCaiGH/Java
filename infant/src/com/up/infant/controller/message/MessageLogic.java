package com.up.infant.controller.message;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.ComParams;
import com.up.infant.model.Member;
import com.up.infant.model.MemberMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/2 0002. 0:02
 */
public class MessageLogic {
    Member memberDao = new Member();
    MemberMessage memberMessageDao = new MemberMessage();

    public void messageAddToMember(int msgId) {
        List<Member> members = memberDao.find("SELECT id FROM t_member");
        List<MemberMessage> msgList = new ArrayList<MemberMessage>();
        for (Member m : members
                ) {
            MemberMessage message = new MemberMessage();
            message.setMemberId(m.getId());
            message.setMsgId(msgId);
            message.setIsRead(0);//未读
            msgList.add(message);
        }
        Db.batchSave(msgList, msgList.size());
    }

    /**
     * 获取用户的消息
     *
     * @param params
     * @return
     */
    public Page<MemberMessage> getMemberMessage(ComParams params) {
        Page<MemberMessage> page = memberMessageDao.getMessages(params.getPageSize(), params.getPageNo(), params.getMemberId());
        return page;
    }

    /**
     * 对应消息标为已读
     * @param id
     * @return
     */
    public boolean msgIsRead(int id) {
        MemberMessage memberMessage = memberMessageDao.findById(id);
        memberMessage.setIsRead(1);
        return memberMessage.update();
    }

    public void allRead(int id){
        List<MemberMessage> messageList=memberMessageDao.getMessage(id);
        for (int i=0;i<messageList.size();i++) {
            messageList.get(i).setIsRead(1);//标为已读
        }
        Db.batchUpdate(messageList,messageList.size());

    }

}
