package com.esoft.gascollect.service;

import com.esoft.gascollect.dto.UserDTO;
import com.esoft.gascollect.entity.Role;
import com.esoft.gascollect.entity.User;
import com.esoft.gascollect.repository.RoleRepository;
import com.esoft.gascollect.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserDTO addUser(UserDTO userDTO) {
        log.info("Adding a new user with name: {}", userDTO.getName());
        System.out.println(userDTO);

        Set<Role> roles = new HashSet<>();
        if (userDTO.getRoles() != null) {
            for (String roleName : userDTO.getRoles()) {
                Role role = (Role) roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role); // Use existing roles from the database
            }
        }
        Optional<User> userOptional=userRepository.findByEmail(userDTO.getEmail());
        if (userOptional.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = new User();
        user.setName(userDTO.getName());
        user.setContactNo(userDTO.getContactNo());
        user.setPassword(encodedPassword);
        user.setEmail(userDTO.getEmail());
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        log.debug("User saved with ID: {}", savedUser.getId());
        return new UserDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getContactNo(),
                null, // Do not expose the password
                roles.stream().map(Role::getName).collect(Collectors.toSet())
        );
    }



    public Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Optional<UserDTO> getUser(int id) {
        log.info("Fetching user with ID: {}", id);

        String username = getAuthenticatedUser();
        if (username != null && hasAccessToUser(id, username)) {
            log.warn("User {} does not have permission to access user ID {}", username, id);
            return Optional.empty();
        }

        return userRepository.findById(id).map(user -> {
            log.debug("User found: {}", user);
            Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
            return new UserDTO(user.getId(), user.getName(),user.getEmail(), user.getContactNo(), null, roles);
        });
    }

    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        String username = getAuthenticatedUser();
        System.out.println(isAdmin(username));
        if (username == null || username.isEmpty()) {
            log.warn("User {} does not have admin privileges to access all users", username);
            return List.of();
        }

        List<User> users = userRepository.findAll();
        log.debug("Number of users found: {}", users.size());
        return users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getContactNo(),
                        null,
                        user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> updateUser(int id, UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);

        // Check permissions (if applicable)
        String username = getAuthenticatedUser();
        if (username != null && !hasAccessToUser(id, username)) {
            log.warn("User {} does not have permission to update user ID {}", username, id);
            return Optional.empty();
        }

        // Check if the user exists
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (!existingUserOpt.isPresent()) {
            log.warn("User with ID {} not found. Update aborted.", id);
            return Optional.empty();
        }

        User existingUser = existingUserOpt.get();

        // Update fields only if they are provided in the DTO
        if (userDTO.getName() != null) {
            existingUser.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getContactNo() != 0) {
            existingUser.setContactNo(userDTO.getContactNo());
        }
        if (userDTO.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : userDTO.getRoles()) {
                // Fetch the role from the database or create a new one if it doesn't exist
                Role role = roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(roleName);
                            return roleRepository.save(newRole); // Save the new role to make it persistent
                        });
                roles.add(role);
            }
            existingUser.setRoles(roles);
        }

        // Save the updated user
        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully: {}", updatedUser);

        // Convert the updated user to a DTO and return it
        return Optional.of(new UserDTO(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getContactNo(),
                null, // Password is not returned in the DTO
                updatedUser.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        ));
    }

    public boolean deleteUser(int id) {
        log.info("Deleting user with ID: {}", id);

        String username = getAuthenticatedUser();
        if (username != null && hasAccessToUser(id, username)) {
            log.warn("User {} does not have permission to delete user ID {}", username, id);
            return false;
        }

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User with ID {} deleted successfully.", id);
            return true;
        } else {
            log.warn("User with ID {} not found. Deletion aborted.", id);
            return false;
        }
    }


    private String getAuthenticatedUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            // If the principal is a UserDetails instance
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof User) {
            // If the principal is the custom User entity
            return ((User) principal).getName();
        }

        return null;
    }

    private boolean hasAccessToUser(int id, String username) {
        return (username == null || !username.equals(String.valueOf(id))) && isAdmin(username);
    }

    private boolean isAdmin(String username) {
        Optional<User> user = userRepository.findByName(username);
        return user.isEmpty() || user.get().getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN_ROLE"));
    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }
}
