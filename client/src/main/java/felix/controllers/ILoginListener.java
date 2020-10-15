package felix.controllers;

import felix.models.WebSocketMessage;

public interface ILoginListener extends IListener
{
    void onLogin(WebSocketMessage webSocketMessage);
    void onLogout(WebSocketMessage webSocketMessage);
}