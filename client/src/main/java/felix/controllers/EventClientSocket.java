package felix.controllers;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.*;
import felix.main.FelixSession;
import felix.models.InitWebSocketMessage;
import felix.models.View;
import felix.models.WebSocketMessage;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EventClientSocket extends MainController implements WebSocketListener
{
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    private void initializeFelixSession(String encrypted)
    {
        InitWebSocketMessage initWebSocketMessage = new Gson().fromJson(encrypted, InitWebSocketMessage.class);
        FelixSession.getInstance().initialize(initWebSocketMessage);
    }

    @Override
    public void onStateChanged(WebSocket webSocket, WebSocketState webSocketState) throws Exception {

    }

    @Override
    public void onConnected(WebSocket webSocket, Map<String, List<String>> map) throws Exception {
        System.out.println("[Connected]");
    }

    @Override
    public void onConnectError(WebSocket webSocket, WebSocketException e) throws Exception {

    }

    @Override
    public void onDisconnected(WebSocket webSocket, WebSocketFrame webSocketFrame, WebSocketFrame webSocketFrame1, boolean b) throws Exception
    {
        System.out.println("[Closed]: " + new Date() + "; " + webSocketFrame.getCloseReason());
        FelixSession.getInstance().stopHeartBeat();
        super.openNewView(View.LOGIN);
    }

    @Override
    public void onFrame(WebSocket webSocket, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onContinuationFrame(WebSocket webSocket, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onTextFrame(WebSocket webSocket, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onBinaryFrame(WebSocket webSocket, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onCloseFrame(WebSocket webSocket, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onPingFrame(WebSocket webSocket, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onPongFrame(WebSocket webSocket, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onTextMessage(WebSocket webSocket, String message) throws Exception
    {
        if (!FelixSession.getInstance().isInitialized())
        {
            this.initializeFelixSession(message);
            return;
        }
        System.out.println("AES Encrypted msg: " + message);
        message = super.refreshJwtTokenAndDecrypt(message);
        System.out.println("AES Decrypted msg: " + message);
        WebSocketMessage webSocketMessage = new Gson().fromJson(message, WebSocketMessage.class);
        super.handleMessage(webSocketMessage);
    }

    @Override
    public void onBinaryMessage(WebSocket webSocket, byte[] bytes) throws Exception {

    }

    @Override
    public void onFrameSent(WebSocket webSocket, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onFrameUnsent(WebSocket webSocket, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onError(WebSocket webSocket, WebSocketException e) throws Exception
    {
//        cause.printStackTrace();
        System.out.println("[ERROR]: " + e.getMessage());
        System.out.println("Error");
    }

    @Override
    public void onFrameError(WebSocket webSocket, WebSocketException e, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onMessageError(WebSocket webSocket, WebSocketException e, List<WebSocketFrame> list) throws Exception {

    }

    @Override
    public void onTextMessageError(WebSocket webSocket, WebSocketException e, byte[] bytes) throws Exception {

    }

    @Override
    public void onSendError(WebSocket webSocket, WebSocketException e, WebSocketFrame webSocketFrame) throws Exception {

    }

    @Override
    public void onUnexpectedError(WebSocket webSocket, WebSocketException e) throws Exception {

    }

    @Override
    public void handleCallbackError(WebSocket webSocket, Throwable throwable) throws Exception {

    }
}