package com.example.ChatApp.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.ChatApp.Models.Messages;

public interface MessageRepository extends MongoRepository<Messages, String>  {
	/*
	 * @Query(fields="{'Content' : 1, 'Created_date' : 1}") List<Messages>
	 * findAll();
	 */
}
