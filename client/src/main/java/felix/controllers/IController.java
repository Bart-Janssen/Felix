package felix.controllers;

import felix.models.WebSocketMessage;

public interface IController
{
    void onMessage(WebSocketMessage webSocketMessage);
}