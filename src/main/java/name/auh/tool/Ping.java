package name.auh.tool;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ping {

    private static PingResultParser pingResultParser;

    public Ping() {
        String charSet = "UTF-8";
        try {
            Process sysProcess = Runtime.getRuntime().exec("ping");
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(sysProcess.getInputStream()));
            String line = "";
            while ((line = streamReader.readLine()) != null) {
                if (line.length() > 0) {
                    charSet = codeString(line);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (charSet.equals("UTF-8")) {
            pingResultParser = new UTF8PingResultParser();
        } else if (charSet.equals("GBK")) {
            pingResultParser = new GBKPingResultParser();
        } else {
            pingResultParser = new UTF8PingResultParser();
        }
    }

    public int pingDelay(String host) throws IOException {
        Process sysProcess = Runtime.getRuntime().exec("ping " + host);
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(sysProcess.getInputStream(), pingResultParser.getCharset()));
        return pingResultParser.parseDelay(streamReader);
    }

    interface PingResultParser {

        public String getCharset();

        public int parseDelay(BufferedReader bufferedReader) throws IOException;
    }

    class GBKPingResultParser implements PingResultParser {

        @Override
        public String getCharset() {
            return "GBK";
        }

        /**
         * 正在 Ping www.a.shifen.com [110.242.68.4] 具有 32 字节的数据:
         * 来自 110.242.68.4 的回复: 字节=32 时间=11ms TTL=53
         * 来自 110.242.68.4 的回复: 字节=32 时间=23ms TTL=53
         * 来自 110.242.68.4 的回复: 字节=32 时间=38ms TTL=53
         * 来自 110.242.68.4 的回复: 字节=32 时间=57ms TTL=53
         * 110.242.68.4 的 Ping 统计信息:
         * 数据包: 已发送 = 4，已接收 = 4，丢失 = 0 (0% 丢失)，
         * 往返行程的估计时间(以毫秒为单位):
         * 最短 = 11ms，最长 = 57ms，平均 = 32ms
         */
        @Override
        public int parseDelay(BufferedReader bufferedReader) throws IOException {
            String line;

            int count = 11;
            while ((line = bufferedReader.readLine()) != null) {
                if (count-- == 0) {
                    break;
                }
                if (line.contains("超时")) {
                    break;
                }
                if (line.contains("平均")) {
                    return Integer.valueOf(line.split("=")[3].replaceFirst("ms", "").trim()).intValue();
                }
            }

            return Integer.MAX_VALUE;
        }
    }

    class UTF8PingResultParser implements PingResultParser {

        @Override
        public String getCharset() {
            return "UTF-8";
        }

        @Override
        public int parseDelay(BufferedReader bufferedReader) throws IOException {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("Reply from") || line.startsWith("64 bytes from")) {

                }
            }

            return 0;
        }
    }

    private static String codeString(String str) {
        ByteArrayInputStream bin = new ByteArrayInputStream(str.getBytes());
        int p = (bin.read() << 8) + bin.read();
        String code = null;
        //其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            case 0x5c75:
                code = "ANSI|ASCII";
                break;
            default:
                code = "GBK";
        }

        return code;
    }

}
