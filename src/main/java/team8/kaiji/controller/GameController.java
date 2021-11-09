package team8.kaiji.controller;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import team8.kaiji.model.Room;

@Controller
@RequestMapping("/game")
public class GameController {
  @Autowired
  private Room room;
  @GetMapping("step1")
  public String step1() {
    return "room.html";
  }

  @GetMapping("step2")
  public String step2(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.room.addUser(loginUser);
    model.addAttribute("room", this.room);

    return "room.html";
  }
}
