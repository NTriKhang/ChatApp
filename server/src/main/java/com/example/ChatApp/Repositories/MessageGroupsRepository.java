package com.example.ChatApp.Repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ChatApp.Models.Message_groups;

public interface MessageGroupsRepository  extends MongoRepository<Message_groups, ObjectId>{
    
}
