package br.com.kafka.ecommerce.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;

public class GsonDeserializer<T> implements Deserializer<T> {
    public static final String TYPE_CONFIG = "br.com.kafka.ecommerce.type_config";

    private final Gson gson = new GsonBuilder().create();
    private Class<T> clazz;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String clazzName = String.valueOf(configs.get(TYPE_CONFIG));
        try {
            this.clazz = (Class<T>) Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Type for deserialization does not exist in the classpath");
        }
    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        return gson.fromJson(new String(bytes), clazz);
    }
}
