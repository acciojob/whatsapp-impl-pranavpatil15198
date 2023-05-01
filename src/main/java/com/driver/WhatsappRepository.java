package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;

    private HashMap<Integer, Message> messageMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.messageMap = new HashMap<>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }


    public Boolean createUser(String name, String mobile) {
        User user = new User(name, mobile);
        if( userMobile.contains(mobile) ){
            return false;
        }
        userMobile.add(mobile);
        return true;
    }


    public Group createGroup(List<User> users) {
        Group group;
        if( users.size() == 2 ){
            group = new Group(users.get(1).getName(), 2);
        }else{
            customGroupCount++;
            Integer participants = users.size();
            String name = "Group" + customGroupCount + "";
            group = new Group(name, participants);
        }

        if( adminMap.containsKey(group) == false ){
            adminMap.put(group, users.get(0));
        }
        groupUserMap.put(group, users);
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId, content, new Date());
        messageMap.put(messageId, message);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) {
        //groupMessageMap:
        List<Message> messageList = new ArrayList<>();
        if( groupMessageMap.containsKey(group) ){
            messageList = groupMessageMap.get(group);
        }
        messageList.add(message);
        int ans = messageList.size();
        groupMessageMap.put(group, messageList);

        //senderMap:
        senderMap.put(message, sender);
        return ans;
    }

    public List<Group> getAllGroups(){
        return groupUserMap.keySet().stream().toList();
    }
    public User getAdminOfGroup(Group group){
        return adminMap.get(group);
    }

    public List<User> getUsersOfGroup(Group group){
        return groupUserMap.get(group);
    }

    public List<Message> getMessagesOfGroup(Group group){
        return groupMessageMap.get(group);
    }

    public void changeAdmin(User approver, User user, Group group) {
        adminMap.put(group, user);
    }
}
