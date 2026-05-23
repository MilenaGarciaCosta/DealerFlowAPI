package com.example.DealerFlow.Dto;

import com.example.DealerFlow.Model.User;

public class UserDto {

    private Integer id;
    private String name;
    private String email;
    private String category;
    private String dealer;
    private String role;

    public UserDto() {
    }

    public UserDto(Integer id, String name, String email, String category, String dealer, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.category = category;
        this.dealer = dealer;
        this.role = role;
    }

    public static UserDto from(User user) {
        String roleName = user.getRole() != null ? user.getRole().getName() : null;
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCategory(),
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
