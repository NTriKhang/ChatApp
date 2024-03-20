package com.example.ChatApp.Services;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Models.Submodels.MessageGroup_User;
import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Repositories.UsersRepository;
import com.example.ChatApp.dto.UserGroupDto;

import io.jsonwebtoken.io.IOException;
 
@Service
public class MessageGroupService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MessageGroupsRepository messageGroupsRepository;
    
    private final String FOLDER_PATH ="D:/HK6/JAVA/ChatApp4/ChatApp/server/src/main/resources/Images/GroupImage/";


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
    public Message_groups update_NAME_Message_groups(String GroupID, Message_groups message_groups) {
        System.out.println("ID:  " + GroupID);
        ObjectId id = new ObjectId(GroupID);
        Optional<Message_groups> messgrs = messageGroupsRepository.findById(id);
        if (messgrs.isPresent()) {
            Message_groups todoToSave = messgrs.get();
            System.out.println("Value "+todoToSave);
            todoToSave.setMessage_group_name(message_groups.getMessage_group_name() != null ? message_groups.getMessage_group_name() : todoToSave.getMessage_group_name());
            //messageGroupsRepository.save(todoToSave);
            return (messageGroupsRepository.save(todoToSave));
        }
        return null;
    } 


    public String uploadImageToFileSystem(String MessGR_ID,MultipartFile file) throws IOException, IllegalStateException, java.io.IOException {
        // convert String to Object
        ObjectId id = new ObjectId(MessGR_ID);
        String filePath = FOLDER_PATH+file.getOriginalFilename();
        Optional<Message_groups> messgrs = messageGroupsRepository.findById(id);
        if (messgrs.isPresent()) {
            Message_groups todoToSave = messgrs.get();
            todoToSave.setMessage_group_image(file.getOriginalFilename());
            
            //other test build
            // Message_groups group = messageGroupsRepository.save(Message_groups.builder()
            // .Message_group_image(file.getOriginalFilename()).build());
            
            Message_groups savedGroup = messageGroupsRepository.save(todoToSave);
            //chuyển vào local
            file.transferTo(new File(filePath));
            if(todoToSave!= null)
            {
                return "file uploaded successfully !"+ filePath;
            }
        }
        return null;
        
    }


    public byte[] downloadImageFromFileSystem(String fileName) throws IOException, java.io.IOException {
        ObjectId id = new ObjectId(fileName);
        Optional<Message_groups> fileData = messageGroupsRepository.findById(id);
        String filePath=fileData.get().getMessage_group_image();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }


}
