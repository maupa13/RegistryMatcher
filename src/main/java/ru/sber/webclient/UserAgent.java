package ru.sber.webclient;

/**
 * Enum of useragent.
 */
public enum UserAgent {

    one("Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.6) "
            + "Gecko/20040924 Epiphany/1.4.4 (Ubuntu)"),
    two("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
            + "(KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36"),
    three("Mozilla/5.2 (X113; U; Linux i586; en-US; rv:1.8.7) "
            + "Gecko/20040924 Epiphany/1.4.4 (Ubuntu)"),
    four("Mozilla/5.3 (X11; U; Linux i586; en-US; rv:1.7.6) "
                  + "Gecko/20040924 Epiphany/1.4.4 (Ubuntu)"),
    five("Mozilla/5.4 (X10; U; Linux i586; en-US; rv:1.7.3) "
            + "Gecko/20040924 Epiphany/1.4.4 (Ubuntu)"),
    six("Mozilla/5.5 (X11; U; Linux i586; en-US; rv:1.6.7) "
                   + "Gecko/20040924 Epiphany/1.4.4 (Ubuntu)"),
    seven("Mozilla/5.6 (X11; U; Linux i586; en-US; rv:1.8.7) "
                   + "Gecko/20040924 Epiphany/1.3.4 (Ubuntu)"),
    eight("Mozilla/5.6 (X12; U; Linux i546; en-US; rv:1.1.7) "
                  + "Gecko/20040924 Epiphany/1.3.4 (Ubuntu)"),
    nine("Mozilla/5.1 (X12; U; Linux i547; en-US; rv:1.1.7) "
                  + "Gecko/20030924 Epiphany/1.5.4 (Ubuntu)"),
    ten("Mozilla/5.9 (X15; U; Linux i547; en-US; rv:1.1.8) "
                 + "Gecko/20030934 Epiphany/1.5.8 (Ubuntu)");

    private String set;

    UserAgent(String set) {
        this.set = set;
    }

    public String getSet() {
        return set;
    }

    /**
     * Different types of agents.
     *
     * @return random useragent
     */
    public static UserAgent getRandomUserAgent() {
        return values()[(int) (Math.random() * values().length)];
    }
}
