package ru.sber.connection;

/**
 * Host, ip settings.
 */
public enum HostIp {

    setHost("http.proxyHost"),
    setPort("http.proxyPort"),

    setIp1("192.168.5.1"),
    setIp2("192.168.5.2"),
    setIp3("192.168.5.3"),
    setIp4("192.168.5.4"),

    setPort1("8180"),
    setPort2("8181"),
    setPort3("8182"),
    setPort4("8183");

    private String settings;

    HostIp(String settings) {
        this.settings = settings;
    }

    public String getSettings() {
        return settings;
    }

    @Override
    public String toString() {
        return "HostIp {"
                + "settings: " + settings + "\""
                + "}";
    }
}
