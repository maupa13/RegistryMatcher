package ru.sber.Connection;

public class SetIp {

    public int setVariantIp(int changeIp) {

        changeIp = 0;

        if ((changeIp & 2) == 0) {
            System.setProperty("http.proxyHost", "192.168.5.1");
            System.setProperty("http.proxyPort", "8181");
        } else if ((changeIp & 3) == 0) {
            System.setProperty("http.proxyHost", "192.168.5.2");
            System.setProperty("http.proxyPort", "8182");
        } else if ((changeIp & 5) == 0) {
            System.setProperty("http.proxyHost", "192.168.5.3");
            System.setProperty("http.proxyPort", "8183");
        } else if ((changeIp & 7) == 0) {
            System.setProperty("http.proxyHost", "192.168.5.4");
            System.setProperty("http.proxyPort", "8184");
        }
        return changeIp;
    }
}
