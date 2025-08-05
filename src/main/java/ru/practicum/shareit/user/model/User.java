package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class User {

    private long id;
    private String name;

    @Email(message = "Поле email неверно заполнено")
    @NotBlank(message = "Поле email не может быть пустым")
    private String email;
}