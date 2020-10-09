package felix.client.models;

public enum View
{
    PAGE_NOT_FOUND("404"),

    HOME("home"),
    LOGIN("login"),
    REGISTER("register"),
    PROFILE("profile"),
    FRIENDS("friends"),
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