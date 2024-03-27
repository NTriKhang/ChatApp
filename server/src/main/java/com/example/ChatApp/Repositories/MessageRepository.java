package com.example.ChatApp.Repositories;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.example.ChatApp.Models.Messages;

public interface MessageRepository extends MongoRepository<Messages, String>  {

	@Aggregation(pipeline = {
			"{ '$match': { 'Message_group_id': ?0, 'Unseen': { $nin: [?1] } } }",
			"{ '$sort': { 'Created_date': -1 } }",
			"{ '$skip': ?2 }",
			"{ '$limit': ?3 }"
	})
	List<Messages> findMessagesByGroupId(ObjectId groupId, String userId, int skip, int limit);
	
	@Query(value = "{ '_id': ?0 }", delete = true)
	Messages deleteMessageByIdOfOwner(ObjectId messageObjectId);
	
	@Query(value = "{'_id': ?0}")
	Messages findMessagesById(ObjectId messageId);
}
