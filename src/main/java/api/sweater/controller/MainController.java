package api.sweater.controller;

import api.sweater.model.Message;
import api.sweater.model.User;
import api.sweater.repository.MessageRepository;
import api.sweater.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public MainController(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal UserDetails userDetails,
                           Model model){
        User user = userRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("username", user.getUsername());
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false) String filter, Model model){
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()){
            messages = messageRepository.findByTag(filter);
        }else {
            messages = messageRepository.findAll();
        }

        model.addAttribute("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String text,
            @RequestParam String tag
    ){
        User user = userRepository.findByUsername(userDetails.getUsername());
        messageRepository.save(new Message(text, tag, user));
        return "redirect:/main";
    }

}
