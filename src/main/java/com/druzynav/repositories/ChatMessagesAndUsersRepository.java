package com.druzynav.repositories;

import com.druzynav.models.message.ChatMessagesAndUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessagesAndUsersRepository extends JpaRepository<ChatMessagesAndUsers, Integer> {

    List<ChatMessagesAndUsers> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(Integer senderId, Integer receiverId, Integer receiverId1, Integer senderId1);
}
