package com.example.ChatApp.Repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ChatApp.Models.Users;

@Repository
public interface UsersRepository extends MongoRepository<Users, ObjectId>{
    Optional<Users> findById(ObjectId id);
}
