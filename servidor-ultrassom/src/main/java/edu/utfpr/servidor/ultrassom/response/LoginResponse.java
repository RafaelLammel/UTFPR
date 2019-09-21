package edu.utfpr.servidor.ultrassom.response;

public class LoginResponse {
    
    int usuarioId;
    boolean status;

    public LoginResponse(int usuarioId, boolean status) {
        this.usuarioId = usuarioId;
        this.status = status;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
