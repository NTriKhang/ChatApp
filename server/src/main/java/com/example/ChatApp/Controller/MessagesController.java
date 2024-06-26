package com.example.ChatApp.Controller;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Models.Messages;
import com.example.ChatApp.Models.Submodels.SenderUser_Msg;
import com.example.ChatApp.Repositories.MessageRepository;
import com.example.ChatApp.Services.MessageGroupService;
import com.example.ChatApp.Services.MessageService;
import com.example.ChatApp.Services.SocketService;
import com.example.ChatApp.SocketDto.IceCandidateMessageDto;
import com.example.ChatApp.SocketDto.MessageTextDto;
import com.example.ChatApp.SocketDto.MessageTextIndDto;
import com.example.ChatApp.SocketDto.ReceiverStringDto;
import com.example.ChatApp.SocketDto.SdpMessageDto;
import com.example.ChatApp.dto.GroupIdRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1/messages")
public class MessagesController {
	@Autowired
	private MessageService messageService;
	@Autowired
	private MessageGroupService messageGroupService;
	@Autowired
	private SocketService socketService;
	
	@MessageMapping("/sendMessage")
	public MessageTextDto receivePublicMessage(@Payload MessageTextDto messageTextDto) {
		try {
			Messages messages = messageService.insertOne(messageTextDto);
			socketService.sendMessageToGroup(messageTextDto.Message_group_id, messages);
			return messageTextDto;
		} catch (Exception e) {
			// TODO: handle exception
			socketService.sendErrorToUser(messageTextDto.Sender_user.user_id);
			return messageTextDto;
		}
	}
	@MessageMapping("/sendIndMessage")
	public MessageTextIndDto sendIndMessage(@Payload MessageTextIndDto messageTextIndDto) {
		try {
			List<Messages> messageList;
			if(messageTextIndDto.MsgGroupSenderId == "") {		
				messageList = messageService.InitPrivateMessage(messageTextIndDto);		
			}
			else if(messageTextIndDto.MsgGroupSenderId != "") {
				Optional<Message_groups> senderGroup = messageGroupService.getMsgGroupById(messageTextIndDto.MsgGroupSenderId);
				if(!senderGroup.isPresent()) {
					System.err.println("Invalid parameter");
					socketService.sendErrorToUser(messageTextIndDto.SenderId);
					return messageTextIndDto;
				}
				if(messageTextIndDto.Type == "Text")
					messageList = messageService.insertBothMessage(new SenderUser_Msg(messageTextIndDto.SenderId, messageTextIndDto.SenderName),
						messageTextIndDto.MsgGroupSenderId, senderGroup.get().MsgConnectedId, messageTextIndDto.Content);
				else
					messageList = messageService.insertBothMessage(new SenderUser_Msg(messageTextIndDto.SenderId, messageTextIndDto.SenderName),
							messageTextIndDto.MsgGroupSenderId, senderGroup.get().MsgConnectedId, messageTextIndDto.Media_path, messageTextIndDto.Type);
			}
			else {
				System.err.println("Invalid parameter");
				socketService.sendErrorToUser(messageTextIndDto.SenderId);
				return messageTextIndDto;
			}
			
			if(messageList == null)
				socketService.sendErrorToUser(messageTextIndDto.SenderId);
			socketService.sendPrivateMessage(messageTextIndDto.SenderId, messageTextIndDto.ReceiverId, messageList.get(0), messageList.get(1));
			return messageTextIndDto;
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.getMessage());

			e.getStackTrace();
			socketService.sendErrorToUser(messageTextIndDto.SenderId);
			return messageTextIndDto;
		}
	}
	@MessageMapping("/offer-private-call")
	public SdpMessageDto offerPrivateCall(@Payload SdpMessageDto offer) {
		socketService.sendOfferPrivateCall(offer);
		return offer;
	}
	@MessageMapping("/private-call")
	public IceCandidateMessageDto callPrivate(IceCandidateMessageDto iceCandidate) {
		socketService.sendIceToUser(iceCandidate);
		return iceCandidate;
	}
	@MessageMapping("/answer-private-call")
	public SdpMessageDto answerPrivateCall(@Payload SdpMessageDto answer) {
		socketService.sendAnswerToUser(answer);
		return answer;
	}
	@MessageMapping("/shutdown-call")
	public ReceiverStringDto shutdownCall(@Payload ReceiverStringDto receiver) {
		socketService.shutdownCall(receiver);
		return receiver;
	}
	@GetMapping()
	public ResponseEntity<List<Messages>> getAll(){

		System.out.println("Get all messages in controller");
			List<Messages> messages = messageService.getAll();
			return new ResponseEntity<List<Messages>>(messages, HttpStatus.OK);
	}
	@GetMapping("/{group_id}")
	public ResponseEntity<List<Messages>> getMessages(@CookieValue(name = "userId",required = false) String userId,
													  @PathVariable("group_id") String group_id,
													  @RequestParam(value = "page", defaultValue = "1") int page) {
			List<Messages> messages = messageService.getMessagesByGroupId(group_id, userId, page);
			return new ResponseEntity<List<Messages>>(messages, HttpStatus.OK);
	}
	@GetMapping("/getAmbigoursMessages/{receiverId}")
	public ResponseEntity<List<Messages>> getMessagesByReceiverId(
													  @PathVariable("receiverId") String receiverId,
													  @RequestParam(value = "userId", defaultValue = "") String userId,
													  @RequestParam(value = "page", defaultValue = "1") int page) {
			List<Messages> messages = messageService.getMessagesByReceiverId(receiverId, userId, page);
			return new ResponseEntity<List<Messages>>(messages, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteMessage/{message_id}")
	public ResponseEntity<Messages> deleteMessage(@CookieValue(name = "userId") String userId,
											     @PathVariable("message_id") String message_id) {
		Messages messageDeleted = messageService.deleteMessagesById(message_id, userId);
		if(messageDeleted._id.isEmpty()) {
			return new ResponseEntity<Messages>(messageDeleted, HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<Messages>(messageDeleted, HttpStatus.OK);
		}
	}
}
