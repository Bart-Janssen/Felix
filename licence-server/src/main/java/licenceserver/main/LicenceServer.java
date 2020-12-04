package licenceserver.main;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URISyntaxException;

public class LicenceServer extends Application
{
    @Override
    public void start(Stage primaryStage)
    {

    }

    private static final String HTTP = "http";
    private static final String CONTENT_TYPE  = "content-type";
    private static final String APPLICATION_JSON = "application/json";

    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            System.out.println("Need argument");
            System.exit(0);
            return;
        }
        OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\licence.lic"));
        Licence licence = getLicence(args[0]);
        outputStream.write(licence.toByteArray());
        outputStream.close();
        System.out.println("Done");
    }

    private static Licence getLicence(String arg) throws IOException, URISyntaxException
    {
        return get("/authentication/activation/generate/" + arg + "/", Licence.class);
    }

    private static <T> T get(String path, Type type) throws URISyntaxException, IOException
    {
        return execute(new HttpGet(parseBuilder(path).build()), type);
    }

    private static URIBuilder parseBuilder(String path)
    {
        URIBuilder builder = new URIBuilder(); //10.10.2.125
        builder.setScheme(HTTP).setHost("127.0.0.1").setPath(path).setPort(29805);
        return builder;
    }

    private static <T extends HttpRequestBase, E> E execute(T http, Type type) throws IOException
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        http.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        CloseableHttpResponse response = httpClient.execute(http);
        String json = EntityUtils.toString(response.getEntity());
        httpClient.close();
        response.close();
        return getJson(type, json);
    }

    private static <T> T getJson(Type type, String jsonResponse)
    {
        LinkedTreeMap<?, ?> treeMap =  new Gson().fromJson(jsonResponse, new TypeToken<T>(){}.getType());
        return new Gson().fromJson(new Gson().toJsonTree(treeMap), type);
    }
}