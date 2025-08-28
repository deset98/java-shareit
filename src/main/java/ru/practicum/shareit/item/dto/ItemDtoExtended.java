package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemDtoExtended {

    private long id;

    @NotBlank(message = "Не введено наименование Item")
    private String name;

    @NotBlank(message = "Не введено описание Item")
    private String description;

    @NotNull
    private Boolean available;

    private LocalDateTime lastBooking;

    private LocalDateTime nextBooking;

    private List<CommentResponseDto> comments;
}
