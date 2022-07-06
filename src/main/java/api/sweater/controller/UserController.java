package api.sweater.controller;

import api.sweater.model.Role;
import api.sweater.model.User;
import api.sweater.repository.UserRepository;
import api.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", user);
        System.out.println(user.getId());
        return "users";
    }

    @GetMapping("{user}")
    public String editUser(@PathVariable User user,
                           Model model
    ) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.allRoles);
        return "edit-user";
    }

    @GetMapping("/delete/{user}")
    public String removeUser(@PathVariable User user) {
        userRepository.deleteById(user.getId());
        return "redirect:/user";
    }

    @PostMapping()
    public String saveUser(@RequestParam Map<String, String> form,
                           @RequestParam("id") Long id,
                           @RequestParam("username") String username,
                           @RequestParam("active") boolean active
    ) {
        userService.editUser(form, id, username, active);
        return "redirect:/user";
    }
}
