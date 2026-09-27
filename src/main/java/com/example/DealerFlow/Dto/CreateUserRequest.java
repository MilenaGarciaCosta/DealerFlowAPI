package com.example.DealerFlow.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CriarUsuarioRequest", description = "Dados necessários para cadastrar um usuário.")
public class CreateUserRequest {

    @Schema(description = "Nome completo do usuário.", example = "Ana Souza", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Endereço de e-mail válido, usado para autenticação.", example = "ana.souza@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Schema(description = "Senha da conta. Nunca é incluída nas respostas da API.", example = "SenhaSegura123", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "Nome do perfil de acesso atribuído ao usuário.", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Role name is required")
    private String roleName;

    @Schema(description = "Código da concessionária associada ao usuário, quando aplicável.", example = "DLR-001", nullable = true)
    private String dealer;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String name, String email, String password, String roleName, String dealer) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleName = roleName;
        this.dealer = dealer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }
}
