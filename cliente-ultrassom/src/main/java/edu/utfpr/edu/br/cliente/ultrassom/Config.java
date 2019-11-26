package edu.utfpr.edu.br.cliente.ultrassom;

public class Config {
    private static String server_address = "http://localhost:8080";

    public static String getServer_address() {
        return server_address;
    }

    public static void setServer_address(String server_address) {
        Config.server_address = server_address;
    }
}
