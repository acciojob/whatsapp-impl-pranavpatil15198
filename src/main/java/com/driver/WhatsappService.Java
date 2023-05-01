package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsappService {

    @Autowired
    WhatsappRepository whatsappRepository;

    public void createUser(String name, String mobile) throws Exception {
        Boolean check = whatsappRepository.createUser(name, mobile);
        if( !check ){
            throw new Exception("User Already Exist");
        }

    }

    public Group createGroup(List<User> users) throws Exception {
        if( users.size() < 2 ){
            throw new Exception("Less than 2 users");
        }
        return whatsappRepository.createGroup(users);
    }

    public int createMessage(String content) {
        return whatsappRepository.createMessage(content);
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        List<Group> groups = whatsappRepository.getAllGroups();
        if( groups.contains(group) ){
            List<User> users = whatsappRepository.getUsersOfGroup(group);
            if( users.contains(sender) ){
                return whatsappRepository.sendMessage(message, sender, group);
            }
            else{
                throw new Exception("User is not in the group");
            }
        }
        else{
            throw new Exception("Group does not exist");
        }
    }


    public String changeAdmin(User approver, User user, Group group) throws Exception{
        List<Group> groups = whatsappRepository.getAllGroups();
        if( groups.contains(group) ){
            User admin = whatsappRepository.getAdminOfGroup(group);
            if( admin == approver ){
                List<User> users = whatsappRepository.getUsersOfGroup(group);
                if( users.contains(user) ){
                    whatsappRepository.changeAdmin(approver, user, group);
                    return "SUCCESS";
                }
                else{
                    throw new Exception("User is not in the group");
                }
            }
            else{
                throw new Exception("Admin is not the Approver");
            }
        }
        else{
            throw new Exception("Group does not exist");
        }

    }

//    public int removeUser(User user) {
//
//    }
}
