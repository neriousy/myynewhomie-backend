package com.druzynav.controllers;

import com.druzynav.models.activity.Activity;
import com.druzynav.models.message.ChatChannel;
import com.druzynav.models.message.ChatMessagesAndUsers;
import com.druzynav.models.message.CorrespondentDTO;
import com.druzynav.models.message.dto.DataAboutUserDTO;
import com.druzynav.models.message.dto.DataBetweenUsersDTO;
import com.druzynav.models.message.dto.MessageOtherDTO;
import com.druzynav.models.photo.Photo;
import com.druzynav.models.user.User;
import com.druzynav.repositories.*;
import com.druzynav.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatChannelRepository chatChannelRepository;

    @Autowired
    private ChatMessagesAndUsersRepository chatMessagesAndUsersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserService userService;

    /**
     *  Logika wysyłania wiadomości do wszystkich użytkowników:
     *
     *  1) Użytkownik znajduje profil użytkownika, któremu chce wysłać wiadomość
     *  2) Użytkownik klika przycisk "Wyślij wiadomość"
     *  3) Przesyłane są informacje do forntu
     *     - id użytkownika, do którego chcemy wysłać wiadomość
     *     - id użytkownika, który wysyła wiadomość
     *  4) Front tworzy 'pokój' dla użytkowników
     *  5) Użytkownik wysyła wiadomość do użytkownika, do którego chce wysłać wiadomość
     *  6) Wiadomość idze do backendu i jest wysyłana do użytkownika, do którego chcemy wysłać wiadomość
     *      6.1) Wiadomość jest zapisywana w bazie danych (w tabeli messages)
     *  7) Wiadomość jest wysyłana do frontu użytkownika, do którego chcemy wysłać wiadomość
     *  8) Wiadomość jest wyświetlana użytkownikowi, do którego chcemy wysłać wiadomość
     *
     */

    @MessageMapping("/private-chat")
    public MessageOtherDTO recMessage(@Payload MessageOtherDTO message){
        System.out.println(message);
        ChatChannel channel = new ChatChannel();
        ChatMessagesAndUsers chatMessagesAndUsers = new ChatMessagesAndUsers();
        Integer senderId = message.getSenderId();
        Integer receiverId = message.getReceiverId();

        simpMessagingTemplate.convertAndSend("/user/" + message.getReceiverId() + "/private", message);
        //simpMessagingTemplate.convertAndSend("/user/" + message.getSenderId() + "/private", message);
        System.out.println("Wysyłam wiadomość do: " + message.getReceiverId());

        if(checkIfChatChannelExists(senderId, receiverId, chatChannelRepository)){
            System.out.println("Kanał istnieje");
        } else {
            System.out.println("Kanał nie istnieje");
            channel.setSenderId(senderId);
            channel.setReceiverId(receiverId);
            chatChannelRepository.save(channel);
        }

        chatMessagesAndUsers.setSenderId(senderId);
        chatMessagesAndUsers.setReceiverId(receiverId);
        chatMessagesAndUsers.setMessage(message.getMessage());
        chatMessagesAndUsers.setDate(LocalDateTime.now());
        chatMessagesAndUsersRepository.save(chatMessagesAndUsers);

        return message;
    }

    //To bedzie do wyrzucenia po przeksztalceniu forntu
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public MessageOtherDTO receiveMessage(@Payload MessageOtherDTO message){
        System.out.println("Message: " + message);
        return message;
    }

    // Pobieranie kanalow z bazy danych
    /*
    * Nie jest tutaj potrzebne wysyłanie id wiadomosci, ponieważ wiadomości są wysyłane do konkretnego kanału
    * */

    //Receiver Name, Receiver Age, Sender Name, Sender Age
    @GetMapping("/api/v1/messages/{Id}")
    public ResponseEntity<List<CorrespondentDTO>> getChannels(@PathVariable Integer Id){
        List<DataAboutUserDTO> data = new ArrayList<>();
        // Powinna zostac zwrocona lista kanałów, w których bierze udział użytkownik o podanym id
        List<ChatChannel> channels = chatChannelRepository.findBySenderIdOrReceiverId(Id, Id);
        List<CorrespondentDTO> correspondentDTOList =  new ArrayList<>();




        Optional<Activity> activity = null;
        Boolean online = false;
        Optional<User> sender = null;
        Optional<User> recv = null;
        String senderName = "";
        String receiverName = "";
        String name = "";
        Optional<Photo> photo = null;
        byte[] photoByte = null;
        List<DataBetweenUsersDTO> messageList = new ArrayList<>();



        for (ChatChannel channel: channels) {
            Integer senderId = channel.getSenderId();
            Integer receiverId = channel.getReceiverId();
            System.out.println("sender:" + senderId);
            System.out.println("receiver:" + receiverId);
            System.out.println("id:" + Id);
            if(Objects.equals(senderId, Id)){
                recv = userRepository.findById(receiverId);
                if(recv.isPresent()){
                    name = recv.get().getFirstname() + " " +  recv.get().getLastname();

                    activity = activityRepository.findByEmail(recv.get().getEmail());
                    if (activity.isPresent()) {
                        online = userService.isActive(activity.get());
                    }
                }
                photo = photoRepository.findByUserId(receiverId);
                if(photo.isPresent()){
                    photoByte = photo.get().getData();
                }else{
                    photoByte = null;
                }
                messageList = getMessagesBetweenTwo(senderId, receiverId);
                correspondentDTOList.add(new CorrespondentDTO(receiverId, name, photoByte,messageList,online));
            }else if(Objects.equals(receiverId, Id)){

                sender = userRepository.findById(senderId);
                if(sender.isPresent()){
                    name = sender.get().getFirstname() +  " " + sender.get().getLastname();

                    activity = activityRepository.findByEmail(sender.get().getEmail());
                    if (activity.isPresent()) {
                        online = userService.isActive(activity.get());
                    }
                }
                photo = photoRepository.findByUserId(senderId);
                if(photo.isPresent()) {
                    photoByte = photo.get().getData();
                }else{
                    photoByte = null;
                }

                messageList = getMessagesBetweenTwo(senderId, receiverId);
                correspondentDTOList.add(new CorrespondentDTO(senderId, name, photoByte,messageList,online));
            }

        }

        return ResponseEntity.ok(correspondentDTOList);
    }

    // Pobieranie wiadomosci z bazy danych
    /*
     * Nie jest tutaj potrzebne wysyłanie id wiadomosci
     * */
    @GetMapping("/api/v1/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<DataBetweenUsersDTO>> getMessages(@PathVariable Integer senderId, @PathVariable Integer receiverId){
        // Powinna zostac zwrocona lista wiadomości, które zostały wysłane między użytkownikami o podanych id
        List<DataBetweenUsersDTO> data = new ArrayList<>();
        List<ChatMessagesAndUsers> messages = chatMessagesAndUsersRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(senderId, receiverId, receiverId, senderId);

        for (ChatMessagesAndUsers message: messages) {
            Integer message_id = message.getId();
            Integer sender_id = message.getSenderId();
            Integer receiver_id = message.getReceiverId();
            String senderName = userRepository.findById(message.getSenderId()).get().getFirstname();
            String receiverName = userRepository.findById(message.getReceiverId()).get().getFirstname();

            data.add(new DataBetweenUsersDTO(message_id, sender_id, senderName, receiver_id, receiverName, message.getMessage(), message.getDate()));

        }
        return ResponseEntity.ok(data);
    }


    public boolean checkIfChatChannelExists(Integer senderId, Integer receiverId, ChatChannelRepository chatChannelRepository) {
        List<ChatChannel> existingChannels = chatChannelRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(senderId, receiverId, receiverId, senderId);
        return existingChannels != null && existingChannels.size() > 0;
    }

    public List<DataBetweenUsersDTO> getMessagesBetweenTwo(Integer senderId, Integer receiverId){
        List<DataBetweenUsersDTO> data = new ArrayList<>();
        List<ChatMessagesAndUsers> messages = chatMessagesAndUsersRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(senderId, receiverId, receiverId, senderId);

        for (ChatMessagesAndUsers message: messages) {
            Integer message_id = message.getId();
            Integer sender_id = message.getSenderId();
            Integer receiver_id = message.getReceiverId();
            String senderName = userRepository.findById(message.getSenderId()).get().getFirstname();
            String receiverName = userRepository.findById(message.getReceiverId()).get().getFirstname();
            data.add(new DataBetweenUsersDTO(message_id, sender_id, senderName, receiver_id, receiverName, message.getMessage(), message.getDate()));

        }
        Collections.sort(data, Comparator.comparing(DataBetweenUsersDTO::getDate));

        return data;

    }


}
