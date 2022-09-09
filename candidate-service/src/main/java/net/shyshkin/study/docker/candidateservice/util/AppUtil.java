package net.shyshkin.study.docker.candidateservice.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AppUtil {

    private static final String hostName;

    static {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHostName() {
        return hostName;
    }

}
