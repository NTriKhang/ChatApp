package com.example.ChatApp.Repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.dto.IdDto;
import com.example.ChatApp.dto.MsgGroupIdDto;

@Repository
public interface UsersRepository extends MongoRepository<Users, ObjectId>{
    //Optional<Users> findById(ObjectId id);
    //Optional<Users> findByTag(String Tag);
    @Query(value="{ '_id' : ?0 }")
    Optional<Users> find_By(String id);
    @Query(value = "{Email: ?0}", fields = "{_id: 1}")
    Optional<Users> findByEmail(String Email);
    @Query(value = "{Email: ?0, _id: {$ne: ?1}}")
    Optional<Users> findByEmailExceptId(String Email, String Id);
    @Query(value = "{$or: [{Email: ?0}, {Tag: ?0}], Password: ?1}")
    Optional<Users> authLogin(String account_name, String password);
    @Query(value = "{Tag: ?0}", fields = "{_id: 1}")
    Optional<IdDto> findTag(String Tag);

    @Query(value="{ '_id' : ?0 }")
    Optional<Users> findById(String id);
    
	@Aggregation(pipeline = {
			"{ '$match': { '_id': ?0, 'List_message_group.receiverId': ?1 } }",
			"{ '$unwind': '$List_message_group' }",
			"{ '$match': {'List_message_group.receiverId': ?1 } }",
			"{ '$limit': 1 }",
			"{ '$project': {'messageGroupId': '$List_message_group.messageGroupId'}}"
	})
    Optional<MsgGroupIdDto> findByReceiverIdAndUserId(ObjectId userId, ObjectId receiverId);

    @Query(value = "{ 'Tag': {$regex:/?0/i} }")
    Optional<List<Users>> findUsersByTag(String tag);
}
