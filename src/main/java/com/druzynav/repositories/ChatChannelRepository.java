package com.druzynav.repositories;

import com.druzynav.models.message.ChatChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {

    List<ChatChannel> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(Integer sender_id1, Integer receiver_id1, Integer sender_id2, Integer receiver_id2);

    List<ChatChannel> findBySenderId(Integer senderId);

    List<ChatChannel> findBySenderIdOrReceiverId(Integer senderId, Integer receiverId);
}
