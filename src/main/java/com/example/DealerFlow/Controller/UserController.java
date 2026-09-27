package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.CreateUserRequest;
import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Dto.UserDto;
import com.example.DealerFlow.Service.UserService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuários", description = "Criação de conta e consulta do perfil autenticado.")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria uma conta com nome, e-mail, senha e nome do perfil de acesso. O campo dealer é opcional. A senha não é retornada. Esta operação é pública.")
        @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ausentes ou inválidos.")
        })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        User createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.from(createdUser));
    }

    @GetMapping("/me")
    @Operation(summary = "Consultar usuário autenticado", description = "Retorna os dados públicos do usuário associado ao token JWT.", security = @SecurityRequirement(name = "BearerAuth"))
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do usuário retornados."),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou usuário não autenticado.")
        })
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.findByEmail(principal.getUsername())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        return ResponseEntity.ok(UserDto.from(user));
    }
}
