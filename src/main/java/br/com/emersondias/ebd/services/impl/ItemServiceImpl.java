package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.ItemDTO;
import br.com.emersondias.ebd.entities.Item;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.ItemMapper;
import br.com.emersondias.ebd.repositories.ItemRepository;
import br.com.emersondias.ebd.services.interfaces.IItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements IItemService {

    private final ItemRepository repository;

    @Transactional
    @Override
    public ItemDTO create(ItemDTO itemDTO) {
        requireNonNull(itemDTO);
        itemDTO.setId(null);
        itemDTO.setActive(true);
        Item itemEntity = repository.save(ItemMapper.toEntity(itemDTO));
        return ItemMapper.toDTO(itemEntity);
    }

    @Transactional
    @Override
    public ItemDTO update(ItemDTO itemDTO) {
        requireNonNull(itemDTO);
        requireNonNull(itemDTO.getId());
        Item itemEntity = findEntityById(itemDTO.getId());
        updateData(itemEntity, itemDTO);
        itemEntity = repository.save(itemEntity);
        return ItemMapper.toDTO(itemEntity);
    }

    private void updateData(Item itemEntity, ItemDTO itemDTO) {
        itemEntity.setName(itemDTO.getName());
        itemEntity.setIcon(itemDTO.getIcon());
        itemEntity.setActive(itemDTO.isActive());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDTO findById(Long id) {
        requireNonNull(id);
        return ItemMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<ItemDTO> findAll() {
        return repository.findByActiveTrue().stream().map(ItemMapper::toDTO).toList();
    }

    private Item findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Item.class));
    }
}
