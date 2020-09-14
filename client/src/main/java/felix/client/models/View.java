package felix.client.models;

public enum View
{
    PAGE_NOT_FOUND("404"),
    CRITICAL_ERROR("criticalError"),

    HOME("home"),
    CHAT("chat");

    private final String page;

    View(String page)
    {
        this.page = page;
    }

    public String getPage()
    {
        return page;
    }
}