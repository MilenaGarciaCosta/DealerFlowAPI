package com.example.DealerFlow.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RespostaAutenticacao", description = "Token JWT e dados públicos do usuário autenticado.")
public class AuthResponse {

    @Schema(description = "Token de acesso JWT. Envie-o no cabeçalho Authorization como Bearer.", example = "eyJhbGciOiJIUzI1NiJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @Schema(description = "Dados públicos do usuário associado ao token.", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserDto user;

    public AuthResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
