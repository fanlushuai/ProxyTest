package name.auh.tool;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String bigJsonPath = classPath + "\\proxy.json";

        long startTimeMillis = System.currentTimeMillis();
        List<Proxy> proxyList = Proxy.getSpeedOrderedProxies(bigJsonPath, 100);
        System.out.println(String.format("耗时：%s millis", System.currentTimeMillis() - startTimeMillis));

        proxyList.forEach(proxy -> {
            System.out.println(String.format("server : %s speed : %d", proxy.getServer(), proxy.getSpeed()));
        });

    }

}
