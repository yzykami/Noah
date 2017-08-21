package com.tzw.noah.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.MyGroups;
import com.tzw.noah.models.MyRelationship;
import com.tzw.noah.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/13.
 */

public class SnsDBManager {
    private SnsDBHelper helper;
    private SQLiteDatabase db;
    Context context;
    String UserTableName = "MemberInfo";
    String FriendListTableName = "MyFriends";
    String FollowListTableName = "MyFollows";
    String FansListTableName = "MyFans";
    String BlackListTableName = "MyBlacklist";
    String MyGroupsTableName = "MyGroups";
    String GroupTableName = "GroupInfo";
    String GroupMemberTableName = "GroupMember";

    public SnsDBManager(Context context) {
        this.context = context;
        helper = new SnsDBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    //获取好友
    public List<User> getSnsFriendList() {
        String sql = "select * from memberInfo where memberNo in(select memberNo from " + FriendListTableName + ")";
        return helper.queryAll(User.class, sql);
    }

    //获取关注
    public List<User> getSnsFollowList() {
        String sql = "select * from memberInfo where memberNo in(select memberNo from " + FollowListTableName + ")";
        return helper.queryAll(User.class, sql);
    }

    //获取粉丝
    public List<User> getSnsFansList() {
        String sql = "select * from memberInfo where memberNo in(select memberNo from " + FansListTableName + ")";
        return helper.queryAll(User.class, sql);
    }

    //获取黑名单
    public List<User> getSnsBlacklist() {
        String sql = "select * from memberInfo where memberNo in(select memberNo from " + BlackListTableName + ")";
        return helper.queryAll(User.class, sql);
    }

    //获取所有好友, 给NIM提供映射
    public List<com.netease.nim.uikit.tzw_relative.User> getSnsAllUserList() {
        String sql = "select * from memberInfo";
        return helper.queryAllNoMyField(com.netease.nim.uikit.tzw_relative.User.class, sql);
    }
    //获取所有群组, 给NIM提供映射
    public List<com.netease.nim.uikit.tzw_relative.Group> getSnsAllGroupList() {
        String sql = "select * from groupInfo";
        return helper.queryAllNoMyField(com.netease.nim.uikit.tzw_relative.Group.class, sql);
    }

    //更新好友列表
    public void UpdateFriendList(List<User> list) {
        UpdateUserList(list, FriendListTableName);
    }

    //更新关注列表
    public void UpdateFollowList(List<User> list) {
        UpdateUserList(list, FollowListTableName);
    }

    //更新粉丝列表
    public void UpdateFansList(List<User> list) {
        UpdateUserList(list, FansListTableName);
    }

    //更新黑名单列表
    public void UpdateBlacklist(List<User> list) {
        UpdateUserList(list, BlackListTableName);
    }

    //更新列表, 抽取共性, 只有表名不同
    private void UpdateUserList(List<User> list, String tableName) {
        updateUserListByColumns(list);
        helper.insertDeleteMode(MyRelationship.createList(list), tableName);
    }

    //将获取到的列表,存入本地数据库中, 只更新获取的列
    private void updateUserListByColumns(List<User> list) {
        List<String> columns = new ArrayList<>();
        columns.add("memberNickName");
        columns.add("memberHeadPic");
        columns.add("memberIntroduce");
        columns.add("remarkName");
        columns.add("IfStat");
//        columns.add("");
//        columns.add("");
        for (User user : list)
            updateUser(user, columns);
    }

    //更新用户部分属性, 指定列名称
    private void updateUser(User user, List<String> columns) {
        helper.insertOrUpdate(user, columns, UserTableName);
    }

    //更新用户全部属性
    public void updateUser(User user) {
        helper.insertOrUpdate(user, UserTableName);
    }

    public boolean isInBlacklist(int id) {
        String sql = "select * from MyBlacklist where memberNo=" + id;
        MyRelationship mr = helper.query(MyRelationship.class, sql);
        if (mr != null)
            return true;
        else
            return false;
    }

    //获取用户详情:MemberInfo
    public User getUserDetail(int id) {
        String sql = "select * from " + UserTableName + " where memberNo=" + id;
        User user = helper.query(User.class, sql);
        if (user == null)
            user = new User();
        return user;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////// 群  会话////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////


    //更新 群,会话 列表
    public void UpdateGroupList(List<Group> list) {
        updateGroupList(list);
        helper.insertDeleteMode(MyGroups.createList(list), MyGroupsTableName);
    }

    //将获取到的列表,存入本地数据库中, 只更新获取的列
    private void updateGroupList(List<Group> list) {
        List<String> columns = new ArrayList<>();
//        columns.add("");
//        columns.add("");
        for (Group group : list)
            updateGroup(group);
    }

    //更新 群 会话  部分属性, 指定列名称
    public void updateGroup(Group group, List<String> columns) {
        helper.insertOrUpdate(group, columns, GroupTableName);
    }

    //更新 群 会话 全部属性
    public void updateGroup(Group group) {
        helper.insertOrUpdate(group, GroupTableName);
    }

    //获取群列表
    public List<Group> getGroupList() {
        String sql = "select * from groupInfo where groupAttribute ='" + Group.Type.GROUP + "'";
        return helper.queryAll(Group.class, sql);
    }

    //获取会话列表
    public List<Group> getDiscussList() {
        String sql = "select * from groupInfo where groupAttribute ='" + Group.Type.DISCUSS + "'";
        return helper.queryAll(Group.class, sql);
    }

    //更新 GroupMember
    public void UpdateGroupMemberlist(List<GroupMember> list) {
        updateGroupMemberUserListByColumns(GroupMember.createUserList(list));
        helper.insertDeleteMode(list, GroupMemberTableName);
    }

    //讲群成员中的用户信息更新到MemberInfo表中
    private void updateGroupMemberUserListByColumns(List<User> list) {
        List<String> columns = new ArrayList<>();

        columns.add("memberNo");
        columns.add("netEaseId");
        columns.add("memberNickName");
        columns.add("memberIntroduce");
        columns.add("memberHeadPic");
        for (User user : list)
            updateUser(user, columns);
    }

    public List<GroupMember> getOwnerList(int groupId) {
        String sql = "select * from groupMember where groupId ='" + groupId + "' and memberType ='" + Group.MemberType.OWNER + "'";
        return helper.queryAll(GroupMember.class, sql);
    }

    public List<GroupMember> getManagerList(int groupId) {
        String sql = "select * from groupMember where groupId ='" + groupId + "' and memberType ='" + Group.MemberType.MANAGER + "'";
        return helper.queryAll(GroupMember.class, sql);
    }

    public List<GroupMember> getMemberList(int groupId) {
        String sql = "select * from groupMember where groupId ='" + groupId + "' and memberType ='" + Group.MemberType.MEMBER + "'";
        return helper.queryAll(GroupMember.class, sql);
    }

    public Group getGroup(int groupId) {
        String sql = "select * from groupInfo where groupId ='" + groupId + "'";
        return helper.query(Group.class, sql);
    }
}
