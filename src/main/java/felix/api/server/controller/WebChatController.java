package felix.api.server.controller;

import felix.api.server.service.chat.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import felix.api.server.models.Chat;
import felix.api.server.models.WebSocketItem;

@Controller
@CrossOrigin
@RequiredArgsConstructor
public class WebChatController extends WebSocket
{
    private final IChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send/chat")
    public void onReceivedMessage(Chat chat) throws Exception
    {
        if (chat.getTo() == null || chat.getTo().isEmpty())
        {
            chat.setGuiMessage(sessions.get(chat.getFrom()).getDisplayName() + ": " + chat.getMessage());
            chat.setColor("black");
            this.simpMessagingTemplate.convertAndSend("/socket-receiver/global", new WebSocketItem<>(null, chat));
            return;
        }
        if (sessions.get(chat.getTo()) == null)
        {
            chat.setGuiMessage(chat.getTo() + " is offline.");
            chat.setColor("red");
            this.simpMessagingTemplate.convertAndSend("/socket-receiver/" + sessions.get(chat.getFrom()).getDisplayName() + "/chat-private", new WebSocketItem<>(null, chat));
            return;
        }
        chat.setGuiMessage("From " + chat.getFrom() + ": " + chat.getMessage());
        chat.setColor("darkgreen");
        this.simpMessagingTemplate.convertAndSend("/socket-receiver/" + sessions.get(chat.getTo()).getDisplayName() + "/chat-private", new WebSocketItem<>(null, chat));
        chat.setGuiMessage("To " + chat.getTo() + ": " + chat.getMessage());
        chat.setColor("darkgreen");
        this.simpMessagingTemplate.convertAndSend("/socket-receiver/" + sessions.get(chat.getFrom()).getDisplayName() + "/chat-private", new WebSocketItem<>(null, chat));
        this.chatService.addNew(chat);
    }
}