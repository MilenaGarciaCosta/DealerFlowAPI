package com.example.DealerFlow.Dto;

import com.example.DealerFlow.Model.User;

public class UserDto {

    private Integer id;
    private String name;
    private String email;

    public UserDto() {
    }

    public UserDto(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
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
}
