package com.example.ChatApp.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.ChatApp.Models.Message_participants;


public interface MessageParticipantsRepository extends MongoRepository<Message_participants, String> {
    @Query(value = "db.Message_participants.find()")
    List<Message_participants> findAll();
}

// truyền lên id get ra group tin nhắn
// input _id text primary key message_groups