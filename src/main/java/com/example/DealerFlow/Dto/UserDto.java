package com.example.DealerFlow.Dto;

import com.example.DealerFlow.Model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Usuario", description = "Dados públicos de um usuário. A senha não faz parte deste modelo.")
public class UserDto {

    @Schema(description = "Identificador do usuário.", example = "42")
    private Integer id;

    @Schema(description = "Nome do usuário.", example = "Ana Souza")
    private String name;

    @Schema(description = "E-mail do usuário.", example = "ana.souza@exemplo.com")
    private String email;

    @Schema(description = "Código da concessionária associada, quando houver.", example = "DLR-001", nullable = true)
    private String dealer;

    @Schema(description = "Nome do perfil de acesso.", example = "ADMIN", nullable = true)
    private String role;

    public UserDto() {
    }

    public UserDto(Integer id, String name, String email, String dealer, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dealer = dealer;
        this.role = role;
    }

    public static UserDto from(User user) {
        String roleName = user.getRole() != null ? user.getRole().getName() : null;
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getDealer(),
                roleName
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
