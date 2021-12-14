package team8.kaiji.controller;

import java.util.ArrayList;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import team8.kaiji.model.Room;
import team8.kaiji.model.User;
import team8.kaiji.model.UserMapper;
import team8.kaiji.model.Match;
import team8.kaiji.model.MatchMapper;

@Controller
@RequestMapping("/game")
public class GameController {

  @Autowired
  public Room room;

  @Autowired
  public UserMapper userMapper;

  @Autowired
  public MatchMapper matchMapper;

  @GetMapping("step1")
  public String step1(ModelMap model, Principal prin) {
    int cnt = 0;
    User userlist = new User();
    ArrayList<String> namelist = userMapper.selectAllUserName();
    for (String username : namelist) {
      if (prin.getName() == username) {
        cnt++;
      }
    }
    if (cnt == 0) {
      userlist.setName(prin.getName());
      userMapper.insertUser(userlist);
    }
    ArrayList<User> user = userMapper.selectAllUser();
    model.addAttribute("user", user);

    int user2id = userMapper.selectIdByName(prin.getName());
    ArrayList<Integer> match = matchMapper.selectMatchIdByUserid(user2id);

    model.addAttribute("match_wait", match);
   
    return "main.html";

  }

  @GetMapping("step2")
  public String step2(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.room.addUser(loginUser);
    model.addAttribute("room", this.room);

    return "main.html";
  }

  @GetMapping("match")
  public String match(Principal prin, ModelMap model, @RequestParam Integer id) {
    String username = userMapper.selectNameById(id);
    model.addAttribute("enemyname", username);
    model.addAttribute("enemyid", id);
    return "match.html";
  }

  @GetMapping("janken/{hand}")
  public String janken(ModelMap model, Principal prin, @RequestParam int id, @PathVariable String hand) {
    Match match = new Match();
    match.setUser1hand(hand);
    match.setUser1id(userMapper.selectIdByName(prin.getName()));
    match.setUser2id(id);
    matchMapper.insertMatchPlayer1(match);
    return "match.html";
  }
}
