package felix.api.server.models;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Pair<T, E>
{
    private T item1;
    private E item2;

    public Pair(T item1, E item2)
    {
        this.item1 = item1;
        this.item2 = item2;
    }

    public T getItem1()
    {
        return item1;
    }

    public E getItem2()
    {
        return item2;
    }
}