package ru.sber.connection;

/**
 * Set ip, host.
 */
public class SetIp {

    static String setHost = "http.proxyHost";
    static String setPort = "http.proxyPort";

    static String setIp1 = "192.168.5.1";
    static String setIp2 = "192.168.5.2";
    static String setIp3 = "192.168.5.3";
    static String setIp4 = "192.168.5.4";

    static String setPort1 = "8180";
    static String setPort2 = "8181";
    static String setPort3 = "8182";
    static String setPort4 = "8183";

    /**
     * Set variable ip, host to bypass blocks from registries.
     *
     * @param changeIp Set settings according to cell index.
     * @return Cell index.
     */
    public int setVariantIp(int changeIp) {
        changeIp = 0;

        if ((changeIp & 2) == 0) {
            System.setProperty(setHost, setIp1);
            System.setProperty(setPort, setPort1);
        } else if ((changeIp & 3) == 0) {
            System.setProperty(setHost, setIp2);
            System.setProperty(setPort, setPort2);
        } else if ((changeIp & 5) == 0) {
            System.setProperty(setHost, setIp3);
            System.setProperty(setPort, setPort3);
        } else if ((changeIp & 7) == 0) {
            System.setProperty(setHost, setIp4);
            System.setProperty(setPort, setPort4);
        }
        return changeIp;
    }
}
