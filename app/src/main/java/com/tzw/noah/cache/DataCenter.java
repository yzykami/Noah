package com.tzw.noah.cache;

import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/7/24.
 */

public class DataCenter {
    static DataCenter instance;

    private DataCenter() {
    }

    public static synchronized DataCenter getInstance() {
        if (instance == null) {
            instance = new DataCenter();
        }
        return instance;
    }

    List<User> friendList;
    List<User> followList;
    List<User> fansList;
    List<User> blackList;

    List<Group> groupList;
    List<Group> discussList;

    Group group;
    List<GroupMember> groupMemberList;


    List<GroupMember> ownerList;
    List<GroupMember> managerList;
    List<GroupMember> memberList;

    public List<User> getFriendList() {
        if (friendList == null)
            return new ArrayList<>();
        return friendList;
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    public List<User> getFollowList() {
        if (followList == null)
            return new ArrayList<>();
        return followList;
    }

    public void setFollowList(List<User> followList) {
        this.followList = followList;
    }

    public List<User> getFansList() {
        if (fansList == null)
            return new ArrayList<>();
        return fansList;
    }

    public void setFansList(List<User> fansList) {
        this.fansList = fansList;
    }

    public List<User> getBlackList() {
        if (blackList == null)
            return new ArrayList<>();
        return blackList;
    }

    public void setBlackList(List<User> blackList) {
        this.blackList = blackList;
    }

    public List<Group> getGroupList() {
        if (groupList == null)
            return new ArrayList<>();
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<Group> getDiscussList() {
        if (discussList == null)
            return new ArrayList<>();
        return discussList;
    }

    public void setDiscussList(List<Group> discussList) {
        this.discussList = discussList;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<GroupMember> getGroupMemberList() {
        if (groupMemberList == null)
            return new ArrayList<>();
        return groupMemberList;
    }

    public void setGroupMemberList(List<GroupMember> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }

    public List<GroupMember> getOwnerList() {
        if (ownerList == null)
            return new ArrayList<>();
        return ownerList;
    }

    public void setOwnerList(List<GroupMember> ownerList) {
        this.ownerList = ownerList;
    }

    public List<GroupMember> getManagerList() {
        if (managerList == null)
            return new ArrayList<>();
        return managerList;
    }

    public void setManagerList(List<GroupMember> managerList) {
        this.managerList = managerList;
    }

    public List<GroupMember> getMemberList() {
        if (memberList == null)
            return new ArrayList<>();
        return memberList;
    }

    public void setMemberList(List<GroupMember> menberList) {
        this.memberList = menberList;
    }
}
