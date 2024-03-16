package com.example.ChatApp.Repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.dto.IdDto;

@Repository
public interface UsersRepository extends MongoRepository<Users, ObjectId>{
    Optional<Users> findById(ObjectId id);
    //Optional<Users> findByTag(String Tag);
    @Query(value = "{Email: ?0}", fields = "{_id: 1}")
    Optional<Users> findByEmail(String Email);
    @Query(value = "{$or: [{Email: ?0}, {Tag: ?0}], Password: ?1}", fields = "{_id: 1}")
    Optional<IdDto> authLogin(String account_name, String password);
    @Query(value = "{Tag: ?0}", fields = "{_id: 1}")
    Optional<IdDto> findTag(String Tag);
}
