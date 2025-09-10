package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {
    private long id;

    @NotBlank(message = "Не введено наименование Item")
    private String name;

    @NotBlank(message = "Не введено описание Item")
    private String description;

    @NotNull
    private Boolean available;

    private Long requestId;
}
