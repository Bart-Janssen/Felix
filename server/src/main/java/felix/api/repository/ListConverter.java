package felix.api.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import felix.api.configuration.AesEncryptionManager;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.GeneralSecurityException;
import java.util.List;

@Converter
public class ListConverter<T> implements AttributeConverter<List<T>, String>
{
    private static final String BASE64 = "piqtjOVwpb6U8/u7uxTMYalj9t4wZFDwYuAhpST8Jyc=";

    @Override
    public String convertToDatabaseColumn(final List<T> list)
    {
        try
        {
            return AesEncryptionManager.encrypt(BASE64, new Gson().toJson(list));
        }
        catch (GeneralSecurityException e)
        {
            return new Gson().toJson(list);
        }
    }

    @Override
    public List<T> convertToEntityAttribute(final String json)
    {
        try
        {
            return new Gson().fromJson(AesEncryptionManager.decrypt(BASE64, json), new TypeToken<List<T>>(){}.getType());
        }
        catch (GeneralSecurityException e)
        {
            return new Gson().fromJson(json, new TypeToken<List<T>>(){}.getType());
        }
    }
}