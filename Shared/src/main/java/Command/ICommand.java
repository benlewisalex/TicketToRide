package Command;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by benle on 7/6/2018.
 */

public interface ICommand{
    public List<ICommand> execute() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException;
    public void setHistoryMessage(String message);
    public String getHistoryMessage();
    public String getMethodName();
}
