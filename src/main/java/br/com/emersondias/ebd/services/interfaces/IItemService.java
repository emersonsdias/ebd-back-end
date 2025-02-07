package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.ItemDTO;

import java.util.List;

public interface IItemService {

    ItemDTO create(ItemDTO itemDTO);

    ItemDTO update(ItemDTO itemDTO);

    void delete(Long id);

    ItemDTO findById(Long id);

    List<ItemDTO> findAll();

}
