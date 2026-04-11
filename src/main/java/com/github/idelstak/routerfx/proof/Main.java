package com.github.idelstak.routerfx.proof;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java ... <routerBaseUrl> <username> <password>");
            System.err.println("Example: http://192.168.1.1 admin mypassword");
            System.exit(2);
        }

        String baseUrl = args[0];
        String username = args[1];
        String password = args[2];

        try {
            RouterApi api = new HttpRouterApi(baseUrl);
            Credentials credentials = new Credentials(username, password);

            Challenge challenge = api.fetchChallenge();
            Session session = api.login(credentials, challenge);
            RadioState radio = api.fetchRadioState(session);

            printSummary(baseUrl, radio);
        } catch (Exception e) {
            System.err.println("Failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printSummary(String baseUrl, RadioState radio) {
        System.out.println("Airtel Router Radio Status");
        System.out.println("Router          : " + baseUrl);
        System.out.println("Operator        : " + radio.networkOperator());
        System.out.println("Network Type    : " + radio.networkTypeStr());
        System.out.println("RSRP            : " + radio.rsrp());
        System.out.println("RSSI            : " + radio.rssi());
        System.out.println("RSRQ            : " + radio.rsrq());
        System.out.println("SINR            : " + radio.sinr());
        System.out.println("Current Band    : " + radio.currentBand());
        System.out.println("Bandwidth       : " + radio.bandwidth());
        System.out.println("Downlink Flow   : " + radio.flowDl());
        System.out.println("Uplink Flow     : " + radio.flowUl());
        System.out.println("Online Time     : " + radio.onlineTime());
        System.out.println("Online Duration : " + radio.onlineDuration());
    }
}
