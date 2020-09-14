package felix.api.server.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import felix.api.server.exceptions.BadRequestException;
import felix.api.server.exceptions.ItemNotFoundException;
import felix.api.server.exceptions.NotAuthorizedException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;

@Slf4j
public abstract class MicroService
{
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String CONTENT_TYPE  = "content-type";
    private static final String APPLICATION_JSON = "application/json";

    private final ObjectMapper jacksonObjectMapper = new ObjectMapper();

    protected <T, E> E put(MicroServiceType microServiceType, String path, T body, Type type) throws IOException, URISyntaxException
    {
        HttpPut httpPut = new HttpPut(this.parseBuilder(microServiceType, path).build());
        httpPut.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(body)));
        return this.execute(httpPut, type);
    }

    protected <T, E> E post(MicroServiceType microServiceType, String path, T body, Type type) throws URISyntaxException, IOException
    {
        HttpPost httpPost = new HttpPost(this.parseBuilder(microServiceType, path).build());
        httpPost.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(body)));
        return this.execute(httpPost, type);
    }

    protected <T> T get(MicroServiceType microServiceType, String path, Type type) throws URISyntaxException, IOException
    {
        return this.execute(new HttpGet(this.parseBuilder(microServiceType, path).build()), type);
    }

    protected <T> T delete(MicroServiceType microServiceType, String path, Type type) throws URISyntaxException, IOException
    {
        return this.execute(new HttpDelete(this.parseBuilder(microServiceType, path).build()), type);
    }

    private <T> T getJson(Type type, String jsonResponse) throws IOException
    {
        if (type.equals(Void.TYPE)) return null;
        return jacksonObjectMapper.readValue(jsonResponse, this.getType(type));
    }

    private <T extends HttpRequestBase, E> E execute(T http, Type type) throws IOException
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        http.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        CloseableHttpResponse response = httpClient.execute(http);
        handleStatusCode(response.getStatusLine().getStatusCode());
        String json = EntityUtils.toString(response.getEntity());
        httpClient.close();
        response.close();
        return this.getJson(type, json);
    }

    private <E> TypeReference<E> getType(Type type)
    {
        return new TypeReference<E>()
        {
            @Override
            public Type getType()
            {
                return type;
            }
        };
    }

    private void handleStatusCode(int statusCode)
    {
        log.info(Integer.toString(statusCode));
        switch (statusCode)
        {
            case 404: throw new ItemNotFoundException();
            case 400: throw new BadRequestException();
            case 401: throw new NotAuthorizedException();
            case 200: break;
            case 201: break;
            default: throw new UnsupportedOperationException();
        }
    }

    private URIBuilder parseBuilder(MicroServiceType type, String path)
    {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(HTTP).setHost("127.0.0.1").setPath("some_path" + path).setPort(8800);
        return builder;
    }
}