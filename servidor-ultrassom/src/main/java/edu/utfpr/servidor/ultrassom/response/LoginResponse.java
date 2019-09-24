package edu.utfpr.servidor.ultrassom.response;

public class LoginResponse {
    
    private int id;
    private String nome;
    private String login;
    private String email;

    public LoginResponse(int id, String nome, String login, String email) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
