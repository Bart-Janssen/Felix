package felix.service.chat;

import felix.models.Chat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IChatService
{
    List<Chat> getAll(String displayName) throws GeneralSecurityException, IOException, URISyntaxException;
}