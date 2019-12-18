package edu.utfpr.cliente.ultrassom.view.session;

public class Session {
    
    private static int id;
    private static String nome;
    private static String login;
    private static String email;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Session.id = id;
    }

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        Session.nome = nome;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Session.login = login;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Session.email = email;
    }
    
}
