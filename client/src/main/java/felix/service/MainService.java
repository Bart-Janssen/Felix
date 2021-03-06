package felix.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import felix.exceptions.AlreadyLoggedInException;
import felix.exceptions.BadRequestException;
import felix.exceptions.NotAuthorizedException;
import felix.exceptions.PageNotFoundException;
import felix.main.FelixSession;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;

public abstract class MainService extends EncryptionManager
{
    private static final String HTTP = "http";
    private static final String CONTENT_TYPE  = "content-type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";

    protected <T, E> E put(String path, T body, Type type) throws IOException, URISyntaxException
    {
        HttpPut httpPut = new HttpPut(this.parseBuilder(path).build());
        httpPut.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(body)));
        return this.execute(httpPut, type);
    }

    protected <T, E> E post(String path, T body, Type type) throws URISyntaxException, IOException
    {
        HttpPost httpPost = new HttpPost(this.parseBuilder(path).build());
        httpPost.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(body)));
        return this.execute(httpPost, type);
    }

    protected <T> T get(String path, Type type) throws URISyntaxException, IOException
    {
        return this.execute(new HttpGet(this.parseBuilder(path).build()), type);
    }

    protected <T> T delete(String path, Type type) throws URISyntaxException, IOException
    {
        return this.execute(new HttpDelete(this.parseBuilder(path).build()), type);
    }

    private <T> T getJson(Type type, String jsonResponse)
    {
        if (type.equals(Void.TYPE)) return null;
        LinkedTreeMap<?, ?> treeMap =  new Gson().fromJson(jsonResponse, new TypeToken<T>(){}.getType());
        return new Gson().fromJson(new Gson().toJsonTree(treeMap), type);
    }

    private <T extends HttpRequestBase, E> E execute(T http, Type type) throws IOException
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        http.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        if (FelixSession.getInstance().getToken() != null) http.addHeader(AUTHORIZATION, FelixSession.getInstance().getToken());
        CloseableHttpResponse response = httpClient.execute(http);
        handleStatusCode(response.getStatusLine().getStatusCode());
        String json = EntityUtils.toString(response.getEntity());
        httpClient.close();
        response.close();
        return this.getJson(type, json);
    }

    private void handleStatusCode(int statusCode)
    {
        switch (statusCode)
        {
            case 404: throw new PageNotFoundException();
            case 400: throw new BadRequestException();
            case 401: throw new NotAuthorizedException();
            case 208: throw new AlreadyLoggedInException();
            case 200: break;
            case 201: break;
            default: throw new UnsupportedOperationException();
        }
    }

    private URIBuilder parseBuilder(String path)
    {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(HTTP).setHost(FelixSession.getIp()).setPath(path).setPort(29805);
        return builder;
    }
}