package com.example.ChatApp.Repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ChatApp.Models.Message_groups;


public interface MessageGroupsRepository extends MongoRepository<Message_groups, ObjectId>{
    Optional<Message_groups> findById(ObjectId id);
}
