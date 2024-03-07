package com.example.ChatApp.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Models.Submodels.MessageGroup_User;
import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Repositories.UsersRepository;
import com.example.ChatApp.dto.UserGroupDto;
 
@Service
public class MessageGroupService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MessageGroupsRepository messageGroupsRepository;
    
    public List<UserGroupDto> getListMessGroupByUserID(String userID) throws Exception {
    	
        ObjectId id = new ObjectId(userID);
        Optional<Users> users = usersRepository.findById(id);
     
        List<UserGroupDto> rs = new ArrayList<>();
        
        if (users.isPresent()) {
        	
            List<MessageGroup_User> listGroupMessIds = users.get().List_message_group;
            
            listGroupMessIds.forEach(user_messGroup ->{
      
                ObjectId msgId = new ObjectId(user_messGroup.messageGroupId);
                Optional<Message_groups> messageGroup = messageGroupsRepository.findById(msgId);
                
                if(messageGroup.isPresent()){
                	Message_groups msGroups = messageGroup.get();
                    rs.add(new UserGroupDto(msGroups._id, msGroups.Message_group_name, msGroups.Message_group_image,
                    						msGroups.Last_message, user_messGroup.isRead,user_messGroup.role));
                }
            });
        }
        return rs;
    }
}
