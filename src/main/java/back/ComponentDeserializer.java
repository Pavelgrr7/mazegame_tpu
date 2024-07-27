package back;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonDeserializer<Component>, JsonSerializer<Component> {
    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        System.out.println("deserializing");
        try {
            return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown type: " + type);
        }
     }

    @Override
    public JsonElement serialize(Component src, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src
                .getClass()
                .getCanonicalName()
        ));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }
}
