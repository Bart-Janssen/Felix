package felix.models;

import java.util.Date;

public class Chat
{
    private String displayNameFrom;
    private Long date;
    private String displayNameTo;
    private String message;

    public Chat() {}

    public String getDisplayNameFrom()
    {
        return displayNameFrom;
    }

    public void setDisplayNameFrom(String displayNameFrom)
    {
        this.displayNameFrom = displayNameFrom;
    }

    public Long getDate()
    {
        return date;
    }

    public void setDate(Long date)
    {
        this.date = date;
    }

    public String getDisplayNameTo()
    {
        return displayNameTo;
    }

    public void setDisplayNameTo(String displayNameTo)
    {
        this.displayNameTo = displayNameTo;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}