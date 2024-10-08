package tranquility_base.clack;

import tranquility_base.clack.endpoint.Client;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("testuser");
        client.start();
    }
}
