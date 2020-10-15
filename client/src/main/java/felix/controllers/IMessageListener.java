package felix.controllers;

import felix.models.WebSocketMessage;

public interface IMessageListener extends IListener
{
    void onMessage(WebSocketMessage webSocketMessage);
}