package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdUserDto {
    private long id;
    private String name;

    @Email(message = "Поле email неверно заполнено")
    private String email;
}