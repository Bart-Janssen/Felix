package felix.client.service.system;

import felix.client.service.MainService;
import java.io.IOException;
import java.net.URISyntaxException;

public class SystemService extends MainService
{
    public String getServerPublic() throws URISyntaxException, IOException
    {
        return super.post("", RsaEncryptionManager.getPubKey(), String.class);
    }
}