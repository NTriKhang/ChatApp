package com.example.ChatApp.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Models.Users;

import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Repositories.UsersRepository;


// lấy cái id -> đưa vào cái Obj rồi tìm nó sau đó tạo cái list(messagegr để trả về )
// sau đó add hết cái idgr mà thằng user có vô rồi đem 
@Service
public class UserService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    
    MessageGroupsRepository messageGroupsRepository;

    public List<Message_groups> getListMessGroupByUserID(String userID) throws Exception {
        ObjectId id = new ObjectId(userID);
        Optional<Users> users = usersRepository.findById(id);
        List<Message_groups> rs = new ArrayList<>();
        if (users.isPresent()) {
        
            List<ObjectId> listGroupMessIds = users.get().List_message_group;

            listGroupMessIds.forEach(messGroupID ->{
                System.out.println(messGroupID);
                    
                Optional<Message_groups> messageGroup = messageGroupsRepository.findById(messGroupID);
                if(messageGroup.isPresent()){
                    rs.add(messageGroup.get());
                }
            });
        }
        return rs;
    }
}
