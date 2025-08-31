package ru.practicum.shareit.request.ItemRequestService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.dto.RespItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service("ItemRequestServiceImpl")
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;


    public ItemRequestServiceImpl(@Qualifier("UserJpaRepository") final UserRepository userRepository,
                                  @Qualifier("ItemRequestJpaRepository") final ItemRequestRepository itemReqRep,
                                  @Qualifier("ItemJpaRepository") final ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRequestRepository = itemReqRep;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public RespItemRequestDto create(long userId, NewItemRequestDto newItemRequestDto) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id={} не найден", userId));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(newItemRequestDto);
        itemRequest.setRequester(requester);
        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toRespItemRequestDto(itemRequest);
    }

    @Override
    public List<RespItemRequestDto> getAllByUserId(long userId) {
        List<RespItemRequestDto> itemRequests = itemRequestRepository.findAllByUserId(userId)
                .stream()
                .map(ItemRequestMapper::toRespItemRequestDto)
                .toList();
        return itemRequests;
    }

    @Override
    public List<RespItemRequestDto> getAllFromOtherUsers(long userId) {
        List<RespItemRequestDto> itemRequests = itemRequestRepository.findAllFromOtherUsers(userId)
                .stream()
                .map(ItemRequestMapper::toRespItemRequestDto)
                .toList();
        return itemRequests;
    }

    @Override
    public RespItemRequestDto getByRequestId(long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request id={} не найден", requestId));
        RespItemRequestDto respItemRequestDto = ItemRequestMapper.toRespItemRequestDto(itemRequest);
        List<ItemDtoExtended> replies = itemRepository.findAllRepliesByRequestId(requestId)
                .stream()
                .map(ItemMapper::toItemDtoExtended)
                .toList();
        respItemRequestDto.setItems(replies);
        return respItemRequestDto;
    }
}
