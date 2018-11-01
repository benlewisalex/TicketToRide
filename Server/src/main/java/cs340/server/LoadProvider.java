package cs340.server;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IPersistanceProvider;

public class LoadProvider {

    public IPersistanceProvider getProvider(String providerName) {

        try {
            String json = new String(Files.readAllBytes(Paths.get("server_config.json")));
            Gson gson = new Gson();
            ConfigInfo[] configArray = gson.fromJson(json, ConfigInfo[].class);

            for(ConfigInfo info: configArray) {
                if(info.plugin_name.equals(providerName)) {
                    return createProvider(info);
                }
            }
            return null;

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public IPersistanceProvider createProvider(ConfigInfo info) throws MalformedURLException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        // Getting the jar URL which contains target class
        URL[] classLoaderUrls = new URL[]{new URL(info.jar_file_path)};

        // Create a new URLClassLoader
        URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);

        // Load the target class
        return (IPersistanceProvider) urlClassLoader.loadClass(info.class_path).getConstructor().newInstance();
    }

    public static class ConfigInfo{
        public String plugin_name;
        public String jar_file_path;
        public String class_path;

        public ConfigInfo(String plugin_name, String jar_file_path, String class_path) {
            this.plugin_name = plugin_name;
            this.jar_file_path = jar_file_path;
            this.class_path = class_path;
        }
    }
}
