package com.concungshop.service.impl;

import com.concungshop.dto.ProductDto;
import com.concungshop.dto.RoleDto;
import com.concungshop.dto.UserDto;
import com.concungshop.entity.Product;
import com.concungshop.entity.Role;
import com.concungshop.entity.User;
import com.concungshop.repository.UserRepository;
import com.concungshop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepositoty;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepositoty, ModelMapper modelMapper) {
        this.userRepositoty = userRepositoty;
        this.modelMapper = modelMapper;
    }

    @Override
    public Iterable<UserDto> findAll() {
        Iterable<User> users = userRepositoty.findByActivated(true);
        return StreamSupport.stream(users.spliterator(), true)
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        Optional<User> user = userRepositoty.findById(id);
        return user.map(u -> modelMapper.map(u, UserDto.class));
    }


    @Override
    public void save(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        if (!userDto.getPassword().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
            user.setPassword(hashedPassword);
        }
        userRepositoty.save(user);
    }

    @Override
    public void remove(Long id) {
        User user = userRepositoty.findById(id).get();
        user.setActivated(false);;
        userRepositoty.save(user);
    }


    @Override
    public Optional<UserDto> findByUsername(String username) {
        Optional<User> user = userRepositoty.findByUsername(username);
        return user.map(u -> modelMapper.map(u, UserDto.class));
    }

    @Override
    public Iterable<UserDto> findByActivatedAndRole(Boolean isActivated, RoleDto roleDto) {
        Role role = modelMapper.map(roleDto, Role.class);
        Iterable<User> users = userRepositoty.findByActivatedAndRole(true, role);
        return StreamSupport.stream(users.spliterator(), true)
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updatePassword(UserDto userDto) {
        Optional<User> user = userRepositoty.findById(userDto.getId());
        if (user.isPresent()){
            String hashedPassword = BCrypt.hashpw(user.get().getPassword(), BCrypt.gensalt(10));
            user.get().setPassword(hashedPassword);
        }
    }

    @Override
    public void updateRole(UserDto userDto) {
        Role role = userDto.getRole();
        User user = userRepositoty.findById(userDto.getId()).get();
        user.setRole(role);
        userRepositoty.save(user);
    }

    @Override
    public Iterable<UserDto> findByFullNameContainingAndActivated(String fullname, Boolean isActivated) {
        Iterable<User> users = userRepositoty.findByFullNameContainingAndActivated(fullname, true);
        return StreamSupport.stream(users.spliterator(), true)
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> createAndGetUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        userRepositoty.save(user);
        User newUser = userRepositoty.findById(user.getId()).get();
        UserDto newUserDto = modelMapper.map(newUser, UserDto.class);
        return Optional.ofNullable(newUserDto);
    }

    @Override
    public void updateAllData(UserDto userDto) {
        User user = userRepositoty.findById(userDto.getId()).get();
        user.setAvatar(userDto.getAvatar());
        user.setFullName(userDto.getFullname());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        userRepositoty.save(user);
    }

}
