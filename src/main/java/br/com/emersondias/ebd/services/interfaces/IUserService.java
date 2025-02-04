package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.UserDTO;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    UserDTO create(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);

    void delete(UUID id);

    UserDTO findById(UUID id);

    List<UserDTO> findAll();

}
