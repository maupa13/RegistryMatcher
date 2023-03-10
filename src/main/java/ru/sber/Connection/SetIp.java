package ru.sber.Connection;

public class SetIp {

    public int setVariantIp(int changeIp) {

        changeIp = 0;

        if ((changeIp & 2) == 0) {
            System.setProperty("http.proxyHost", "192.168.5.1");
            System.setProperty("http.proxyPort", "8182");
        } else {
            System.setProperty("http.proxyHost", "192.168.5.4");
            System.setProperty("http.proxyPort", "8181");
        }

        return changeIp;
    }
}
