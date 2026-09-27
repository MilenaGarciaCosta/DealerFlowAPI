package com.example.DealerFlow.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginRequest", description = "Credenciais usadas para iniciar uma sessão na API.")
public class LoginRequest {

    @Schema(description = "E-mail cadastrado.", example = "ana.souza@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Schema(description = "Senha da conta.", example = "SenhaSegura123", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    @NotBlank(message = "Password is required")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
