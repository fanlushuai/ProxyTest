package name.auh.tool;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    final static Gson gson = new Gson();

    public static <T> List<T> readJsonStream(Class<T> tClass, String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        List<T> result = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            T proxy = gson.fromJson(reader, tClass);
            result.add(proxy);
        }
        reader.endArray();
        reader.close();
        return result;
    }


}
