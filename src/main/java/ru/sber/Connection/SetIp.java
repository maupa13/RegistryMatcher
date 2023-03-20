package ru.sber.connection;

/**
 * Set ip, host.
 */
public class SetIp {

    /**
     * Set ip, host to bypass blocks from registries.
     *
     * @param changeIp Set settings according to cell index.
     * @return Cell index.
     */
    public int setOptionIp(int changeIp) {
        changeIp = 0;

        if ((changeIp & 2) == 0) {
            System.setProperty(String.valueOf(HostIp.setHost), String.valueOf(HostIp.setIp1));
            System.setProperty(String.valueOf(HostIp.setPort), String.valueOf(HostIp.setPort1));
        } else if ((changeIp & 3) == 0) {
            System.setProperty(String.valueOf(HostIp.setHost), String.valueOf(HostIp.setIp2));
            System.setProperty(String.valueOf(HostIp.setPort), String.valueOf(HostIp.setPort2));
        } else if ((changeIp & 5) == 0) {
            System.setProperty(String.valueOf(HostIp.setHost), String.valueOf(HostIp.setIp3));
            System.setProperty(String.valueOf(HostIp.setPort), String.valueOf(HostIp.setPort3));
        } else if ((changeIp & 7) == 0) {
            System.setProperty(String.valueOf(HostIp.setHost), String.valueOf(HostIp.setIp4));
            System.setProperty(String.valueOf(HostIp.setPort), String.valueOf(HostIp.setPort4));
        }
        return changeIp;
    }
}
