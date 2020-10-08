package felix.api.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class ListConverter<T> implements AttributeConverter<List<T>, String>
{
    @Override
    public String convertToDatabaseColumn(final List<T> list)
    {
        return new Gson().toJson(list);
    }

    @Override
    public List<T> convertToEntityAttribute(final String json)
    {
        return new Gson().fromJson(json, new TypeToken<List<T>>(){}.getType());
    }
}