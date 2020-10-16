package felix.service.chat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IChatService
{
    List<String> getAll(String displayName) throws GeneralSecurityException, IOException, URISyntaxException;
}