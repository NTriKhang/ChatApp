package com.example.ChatApp.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Services.MessageGroupService;
import com.example.ChatApp.dto.UserGroupDto;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/v1/message_group")
public class MessageGroupController {
	@Autowired
	private MessageGroupService messageGroupService;
	
    @Autowired
    private MessageGroupsRepository messageGroupsRepository;

	@GetMapping("/{userID}")
	public ResponseEntity<List<UserGroupDto>> getListMessGroupByUserID(@PathVariable("userID") String userID) {
        try {
            System.out.println("in get list");
            return new ResponseEntity<>(messageGroupService.getListMessGroupByUserID(userID), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());
            return new ResponseEntity<>(new ArrayList<UserGroupDto>(), HttpStatus.BAD_REQUEST);
        }
	}

    @PutMapping("raname_groups/{GroupID}")
    public ResponseEntity<Message_groups> Update_NAME_GR(@PathVariable String GroupID, @RequestBody Message_groups new_message_groups) {   
        System.out.println("ID:  " + GroupID);
            ObjectId id = new ObjectId(GroupID);
            Optional<Message_groups> messgrs = messageGroupsRepository.findById(id);
            if(messgrs.isPresent())
            {
                return new ResponseEntity<Message_groups>(messageGroupService.update_NAME_Message_groups(GroupID,new_message_groups), HttpStatus.OK);
            }
            System.out.println("Exception:");
            return new ResponseEntity<Message_groups>(HttpStatus.BAD_REQUEST);
    }
    // /api/v1/message_groupUpload_Images/
    @PostMapping("Upload_Images/{GroupID}")
    public ResponseEntity<?> UploadIMG(@PathVariable String GroupID,@RequestParam("image")MultipartFile file)throws IOException {
        try {
            System.out.println("Controller ID:  " + GroupID);
            String uploadImage = messageGroupService.uploadImageToFileSystem(GroupID,file);
            return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
        } catch (Exception e) {
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/Dowload/{fileName}")
	public ResponseEntity<?> downloadIMG(@PathVariable String fileName) throws IOException {
		byte[] imageData=messageGroupService.downloadImageFromFileSystem(fileName);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf("image/png"))
				.body(imageData);
	}


}
