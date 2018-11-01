package Command;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by benle on 7/6/2018.
 */

public class Command implements ICommand, Serializable {

    private String className;
    private String methodName;
    private String[] paramTypes;
    private String[] params;
    private String historyMessage;

    public Command (String className, String methodName, Class<?>[] paramTypes, Object[] params){
        this.className = className;
        this.methodName = methodName;
        this.paramTypes = new String[paramTypes.length];
        for(int i = 0; i < paramTypes.length; i++) {
            this.paramTypes[i] = paramTypes[i].getName();
        }

        Gson gson = new Gson();
        this.params = new String[params.length];
        for(int i = 0; i < params.length; i++) {
            this.params[i] = gson.toJson(params[i], paramTypes[i]);
        }
    }

    public List<ICommand> execute() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException{

        Class<?>[] classParamTypes = new Class[paramTypes.length];
        for(int i = 0; i < paramTypes.length; i++) {
            classParamTypes[i] = Class.forName(paramTypes[i]);
        }

        Gson gson = new Gson();
        Object[] jsonParams = new Object[params.length];
        for(int i = 0; i < params.length; i++) {
            jsonParams[i] = gson.fromJson(params[i], classParamTypes[i]);
        }

        Class commandClass = Class.forName(className);

        Method getInstanceMethod = commandClass.getMethod("getInstance", null);
        Object commandObject = getInstanceMethod.invoke(null, null);

        Method method = commandClass.getMethod(methodName, classParamTypes);
        return (List<ICommand>) method.invoke(commandObject, jsonParams);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        try {
            Class<?>[] classParamTypes = new Class[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                classParamTypes[i] = Class.forName(paramTypes[i]);
            }

            Gson gson = new Gson();
            Object[] jsonParams = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                jsonParams[i] = gson.fromJson(params[i], classParamTypes[i]);
            }
            return jsonParams;
        } catch (Exception e) {
            System.out.println("Exception in command getParams:" + e.toString());
        }
//        return params;
        return null;
    }

//    public void setParams(Object[] params) {
//        this.params = params;
//    }


    @Override
    public void setHistoryMessage(String historyMessage) {
        this.historyMessage = historyMessage;
    }

    @Override
    public String getHistoryMessage() {
        return historyMessage;
    }
}
