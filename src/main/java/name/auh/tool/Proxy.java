package name.auh.tool;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class Proxy implements Comparable<Proxy> {

    String remarks;

    String server;

    String server_port;

    String method;

    String password;

    String plugin;

    Object plugin_opts;

    int speed = 10000000;

    @Override
    public int compareTo(Proxy proxy) {
        return this.speed - proxy.getSpeed();
    }

    public static List<Proxy> getSpeedOrderedProxies(String jsonPath, int count) throws IOException {
        List<Proxy> proxyList = JsonUtil.readJsonStream(Proxy.class, jsonPath);
        Ping ping = new Ping();
        new ForkJoinPool(150).submit(() -> {
                    proxyList.parallelStream().map(proxy -> {
                                try {
                                    proxy.setSpeed(ping.pingDelay(proxy.getServer()));
                                    return proxy;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return proxy;
                            }
                    ).collect(Collectors.toList());
                }
        ).join();

        Collections.sort(proxyList);

        return proxyList.subList(0, Math.min(count, proxyList.size()));
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "remarks='" + remarks + '\'' +
                ", server='" + server + '\'' +
                ", server_port='" + server_port + '\'' +
                ", method='" + method + '\'' +
                ", password='" + password + '\'' +
                ", plugin='" + plugin + '\'' +
                ", plugin_opts=" + plugin_opts +
                ", speed=" + speed +
                '}';
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer_port() {
        return server_port;
    }

    public void setServer_port(String server_port) {
        this.server_port = server_port;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public Object getPlugin_opts() {
        return plugin_opts;
    }

    public void setPlugin_opts(Object plugin_opts) {
        this.plugin_opts = plugin_opts;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


}
