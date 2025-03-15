package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.UserDTO;
import br.com.emersondias.ebd.entities.User;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.UserMapper;
import br.com.emersondias.ebd.repositories.UserRepository;
import br.com.emersondias.ebd.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDTO create(UserDTO userDTO) {
        requireNonNull(userDTO);
        userDTO.setId(null);
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setActive(true);
        User userEntity = repository.save(UserMapper.toEntity(userDTO));
        return UserMapper.toDTO(userEntity);
    }

    @Transactional
    @Override
    public UserDTO update(UserDTO userDTO) {
        requireNonNull(userDTO);
        requireNonNull(userDTO.getId());
        User userEntity = findEntityById(userDTO.getId());
        updateData(userEntity, userDTO);
        userEntity = repository.save(userEntity);
        return UserMapper.toDTO(userEntity);
    }

    private void updateData(User userEntity, UserDTO userDTO) {
        userEntity.setName(userDTO.getName());
        userEntity.setEmail(userDTO.getEmail());
        if (nonNull(userDTO.getPassword()) && !userDTO.getPassword().isBlank()) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userEntity.setActive(userDTO.isActive());
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDTO findById(UUID id) {
        requireNonNull(id);
        return UserMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<UserDTO> findAll() {
        return repository.findAll().stream().map(UserMapper::toDTO).toList();
    }

    private User findEntityById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, User.class));
    }
}
