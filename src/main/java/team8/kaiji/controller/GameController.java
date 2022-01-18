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
    ArrayList<Integer> match = matchMapper.selectMatchIdByUser2id(user2id);

    model.addAttribute("match_wait", match);

    ArrayList<User> userdb = userMapper.selectAllUser();
    ArrayList<Match> matchdb = matchMapper.selectAllMatch();

    model.addAttribute("userdb", userdb);
    model.addAttribute("matchdb", matchdb);

    int userid = userMapper.selectIdByName(prin.getName()); // mainにカードとスター枚数表示処理

    int gucnt = userMapper.selectGu(userid);
    int chocnt = userMapper.selectCho(userid);
    int pacnt = userMapper.selectPa(userid);
    int starcnt = userMapper.selectStar(userid);

    model.addAttribute("gucnt", gucnt);
    model.addAttribute("chocnt", chocnt);
    model.addAttribute("pacnt", pacnt);
    model.addAttribute("starcnt", starcnt);

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
    int userid = userMapper.selectIdByName(prin.getName()); // mainにカードとスター枚数表示処理

    int gucnt = userMapper.selectGu(userid);
    int chocnt = userMapper.selectCho(userid);
    int pacnt = userMapper.selectPa(userid);
    int starcnt = userMapper.selectStar(userid);

    model.addAttribute("gucnt", gucnt);
    model.addAttribute("chocnt", chocnt);
    model.addAttribute("pacnt", pacnt);
    model.addAttribute("starcnt", starcnt);
    return "match.html";
  }

  @GetMapping("janken/{hand}")
  public String janken(ModelMap model, Principal prin, @RequestParam int id, @PathVariable String hand) {

    int userid = userMapper.selectIdByName(prin.getName()); // mainにカードとスター枚数表示処理

    int gucnt = userMapper.selectGu(userid);
    int chocnt = userMapper.selectCho(userid);
    int pacnt = userMapper.selectPa(userid);
    int starcnt = userMapper.selectStar(userid);

    model.addAttribute("gucnt", gucnt);
    model.addAttribute("chocnt", chocnt);
    model.addAttribute("pacnt", pacnt);
    model.addAttribute("starcnt", starcnt);

    model.addAttribute("enemyid", id);
    int user1id = userMapper.selectIdByName(prin.getName());

    // guchokipa枚数制限処理
    gucnt = userMapper.selectGu(user1id);
    chocnt = userMapper.selectCho(user1id);
    pacnt = userMapper.selectPa(user1id);
    int isAct;
    try {
      int matchid = matchMapper.selectMatchIdByUser1Name(prin.getName());
      isAct = matchMapper.selectIsActByMatchId(matchid);
    } catch (Exception e) {
      isAct = 1;
    }
    String alert;
    if (isAct != 0) {
      if (hand.equals("gu")) {
        if (gucnt == 0) {
          alert = "グーは0枚です";
          model.addAttribute("alert", alert);
          return "match.html";
        }
      } else if (hand.equals("cho")) {
        if (chocnt == 0) {
          alert = "チョキは0枚です";
          model.addAttribute("alert", alert);
          return "match.html";
        }
      } else if (hand.equals("pa")) {
        if (pacnt == 0) {
          alert = "パーは0枚です";
          model.addAttribute("alert", alert);
          return "match.html";
        }
      }
    }
    // guchopa枚数制限処理

    Match match = new Match();
    match.setUser1hand(hand);
    match.setUser1id(user1id);
    match.setUser2id(id);
    match.setIsAct(1);
    String mid = matchMapper.selectIdByUser2idAndUser1name(id, prin.getName());
    if (mid == null) {
      matchMapper.insertMatchPlayer1(match);
    }
    int user1matchid = matchMapper.selectMatchIdByUser1Name(prin.getName());
    int act = matchMapper.selectIsActByMatchId(user1matchid);
    String judge = "a";

    if (act == 0) {
      String user2hand = matchMapper.selectUser2handByMatchId(user1matchid);
      int u1 = 0, u2 = 0;
      if (hand.equals("gu")) {
        u1 = 1;
      } else if (hand.equals("cho")) {
        u1 = 2;
      } else if (hand.equals("pa")) {
        u1 = 3;
      }

      if (user2hand.equals("gu")) {
        u2 = 1;
      } else if (user2hand.equals("cho")) {
        u2 = 2;
      } else if (user2hand.equals("pa")) {
        u2 = 3;
      }

      if (u1 == u2) {// あいこ
        judge = "あいこ";
      } else if ((u2 - u1 == 1) || (u2 - u1 == -2)) {// u1勝利
        judge = "勝ち";
      } else {// u1敗北
        judge = "負け";
      }
      model.addAttribute("judge", judge);
    }
    return "match.html";
  }

  @GetMapping("matchwait")
  public String matchwait(ModelMap model, @RequestParam int id, Principal prin) {
    model.addAttribute("matchid", id);

    int userid = userMapper.selectIdByName(prin.getName()); // mainにカードとスター枚数表示処理

    int gucnt = userMapper.selectGu(userid);
    int chocnt = userMapper.selectCho(userid);
    int pacnt = userMapper.selectPa(userid);
    int starcnt = userMapper.selectStar(userid);

    model.addAttribute("gucnt", gucnt);
    model.addAttribute("chocnt", chocnt);
    model.addAttribute("pacnt", pacnt);
    model.addAttribute("starcnt", starcnt);

    return "wait.html";
  }

  @GetMapping("janken2/{hand}")
  public String janken2(ModelMap model, Principal prin, @RequestParam int id, @PathVariable String hand) {

    int userid = userMapper.selectIdByName(prin.getName()); // mainにカードとスター枚数表示処理

    int gucnt = userMapper.selectGu(userid);
    int chocnt = userMapper.selectCho(userid);
    int pacnt = userMapper.selectPa(userid);
    int starcnt = userMapper.selectStar(userid);

    model.addAttribute("gucnt", gucnt);
    model.addAttribute("chocnt", chocnt);
    model.addAttribute("pacnt", pacnt);
    model.addAttribute("starcnt", starcnt);

    model.addAttribute("matchid", id);
    int user2id = userMapper.selectIdByName(prin.getName());

    // guchokipa枚数制限処理
    gucnt = userMapper.selectGu(user2id);
    chocnt = userMapper.selectCho(user2id);
    pacnt = userMapper.selectPa(user2id);
    int isAct;
    try {
      int matchid = id;
      isAct = matchMapper.selectIsActByMatchId(matchid);
    } catch (Exception e) {
      isAct = 1;
    }
    String alert;
    if (isAct != 0) {
      if (hand.equals("gu")) {
        if (gucnt == 0) {
          alert = "グーは0枚です";
          model.addAttribute("alert", alert);
          return "wait.html";
        }
      } else if (hand.equals("cho")) {
        if (chocnt == 0) {
          alert = "チョキは0枚です";
          model.addAttribute("alert", alert);
          return "wait.html";
        }
      } else if (hand.equals("pa")) {
        if (pacnt == 0) {
          alert = "パーは0枚です";
          model.addAttribute("alert", alert);
          return "wait.html";
        }
      }
    }
    // guchopa枚数制限処理

    Match match = new Match();
    match.setUser2hand(hand);
    match.setMatchid(id);
    match.setIsAct(0);
    matchMapper.updateUser2Hand(match);
    int user1matchid = matchMapper.selectMatchIdByUser2Name(prin.getName());
    int act = matchMapper.selectIsActByMatchId(user1matchid);
    String judge = "a";

    int user1id = matchMapper.selectUser1IdByMatchId(user1matchid);
    user2id = matchMapper.selectUser2IdByMatchId(user1matchid);

    if (act == 0) {
      String user1hand = matchMapper.selectUser1handByMatchId(user1matchid);
      String user2hand = matchMapper.selectUser2handByMatchId(user1matchid);
      int u1 = 0, u2 = 0;
      if (user1hand.equals("gu")) {
        u1 = 1;
      } else if (user1hand.equals("cho")) {
        u1 = 2;
      } else if (user1hand.equals("pa")) {
        u1 = 3;
      }

      if (user2hand.equals("gu")) {
        u2 = 1;
      } else if (user2hand.equals("cho")) {
        u2 = 2;
      } else if (user2hand.equals("pa")) {
        u2 = 3;
      }

      int u1star = userMapper.selectStar(user1id);
      int u2star = userMapper.selectStar(user2id);

      if (u1 == u2) {// あいこ
        judge = "あいこ";
        switch (u1) {
          case 1:
            int u1Gu = userMapper.selectGu(user1id) - 1;
            int u2Gu = userMapper.selectGu(user2id) - 1;
            userMapper.updateGu(u1Gu, user1id);
            userMapper.updateGu(u2Gu, user2id);
            break;
          case 2:
            int u1Cho = userMapper.selectCho(user1id) - 1;
            int u2Cho = userMapper.selectCho(user2id) - 1;
            userMapper.updateCho(u1Cho, user1id);
            userMapper.updateCho(u2Cho, user2id);
            break;
          case 3:
            int u1Pa = userMapper.selectPa(user1id) - 1;
            int u2Pa = userMapper.selectPa(user2id) - 1;
            userMapper.updatePa(u1Pa, user1id);
            userMapper.updatePa(u2Pa, user2id);
            break;
        }
      } else if ((u2 - u1 == 1) || (u2 - u1 == -2)) {// u1勝利
        judge = "負け";
        switch (u1) {
          case 1: // u1=gu,u2=cho
            int u1Gu = userMapper.selectGu(user1id) - 1;
            int u2Cho = userMapper.selectCho(user2id) - 1;
            userMapper.updateGu(u1Gu, user1id);
            userMapper.updateCho(u2Cho, user2id);
            u1star++;
            u2star--;
            userMapper.updateStar(u1star, user1id);
            userMapper.updateStar(u2star, user2id);
            break;
          case 2: // u1=cho,u2=pa
            int u1Cho = userMapper.selectCho(user1id) - 1;
            int u2Pa = userMapper.selectPa(user2id) - 1;
            userMapper.updateCho(u1Cho, user1id);
            userMapper.updatePa(u2Pa, user2id);
            u1star++;
            u2star--;
            userMapper.updateStar(u1star, user1id);
            userMapper.updateStar(u2star, user2id);
            break;
          case 3: // u1=pa,u2=gu
            int u1Pa = userMapper.selectPa(user1id) - 1;
            int u2Gu = userMapper.selectGu(user2id) - 1;
            userMapper.updatePa(u1Pa, user1id);
            userMapper.updateGu(u2Gu, user2id);
            u1star++;
            u2star--;
            userMapper.updateStar(u1star, user1id);
            userMapper.updateStar(u2star, user2id);
            break;
        }
      } else {// u1敗北
        judge = "勝ち";
        switch (u1) {
          case 1: // u1=Gu,u2=Pa
            int u1Gu = userMapper.selectGu(user1id) - 1;
            int u2Pa = userMapper.selectPa(user2id) - 1;
            userMapper.updateGu(u1Gu, user1id);
            userMapper.updatePa(u2Pa, user2id);
            u1star--;
            u2star++;
            userMapper.updateStar(u1star, user1id);
            userMapper.updateStar(u2star, user2id);
            break;
          case 2: // u1=Cho,u2=Gu
            int u1Cho = userMapper.selectCho(user1id) - 1;
            int u2Gu = userMapper.selectGu(user2id) - 1;
            userMapper.updateCho(u1Cho, user1id);
            userMapper.updateGu(u2Gu, user2id);
            u1star--;
            u2star++;
            userMapper.updateStar(u1star, user1id);
            userMapper.updateStar(u2star, user2id);
            break;
          case 3: // u1=pa,u2=cho
            int u1Pa = userMapper.selectPa(user1id) - 1;
            int u2Cho = userMapper.selectCho(user2id) - 1;
            userMapper.updatePa(u1Pa, user1id);
            userMapper.updateCho(u2Cho, user2id);
            u1star--;
            u2star++;
            userMapper.updateStar(u1star, user1id);
            userMapper.updateStar(u2star, user2id);
            break;
        }
      }
      model.addAttribute("judge", judge);
      // ゲームクリアー、ゲームオーバー処理
      String gameover = "Game Over";
      String gameclear = "Game Clear";

      int star = userMapper.selectStar(user2id);
      gucnt = userMapper.selectGu(user2id);
      chocnt = userMapper.selectCho(user2id);
      pacnt = userMapper.selectPa(user2id);
      int cardsum = gucnt + chocnt + pacnt;
      if (star == 0) {
        model.addAttribute("gamejudge", gameover);
        userMapper.deleteUserById(user2id);
        return "gameover.html";
      } else if (cardsum == 0 && star < 3) {
        model.addAttribute("gamejudge", gameover);
        userMapper.deleteUserById(user2id);
        return "gameover.html";
      } else if (cardsum == 0 && star >= 3) {
        model.addAttribute("gamejudge", gameclear);
        userMapper.deleteUserById(user2id);
        return "gameclear.html";
      }
      // ゲームクリアー、ゲームオーバー処理
    }
    return "wait.html";
  }

  @GetMapping("nextgame")
  public String nextgame(ModelMap model, Principal prin) {

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

    int user1id = userMapper.selectIdByName(prin.getName());
    ArrayList<Integer> match = matchMapper.selectMatchIdByUserid(user1id);

    model.addAttribute("match_wait", match);

    ArrayList<User> userdb = userMapper.selectAllUser();
    ArrayList<Match> matchdb = matchMapper.selectAllMatch();

    model.addAttribute("userdb", userdb);
    model.addAttribute("matchdb", matchdb);

    matchMapper.deletematchById(user1id);

    int userid = userMapper.selectIdByName(prin.getName());

    int gucnt = userMapper.selectGu(userid);
    int chocnt = userMapper.selectCho(userid);
    int pacnt = userMapper.selectPa(userid);
    int starcnt = userMapper.selectStar(userid);

    model.addAttribute("gucnt", gucnt);
    model.addAttribute("chocnt", chocnt);
    model.addAttribute("pacnt", pacnt);
    model.addAttribute("starcnt", starcnt);
    return "main.html";
  }

  @GetMapping("nextgame2")
  public String nextgame2(ModelMap model, Principal prin) {

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
    ArrayList<Integer> match = matchMapper.selectMatchIdByUser2id(user2id);

    model.addAttribute("match_wait", match);

    ArrayList<User> userdb = userMapper.selectAllUser();
    ArrayList<Match> matchdb = matchMapper.selectAllMatch();

    model.addAttribute("userdb", userdb);
    model.addAttribute("matchdb", matchdb);

    int userid = userMapper.selectIdByName(prin.getName());

    int gucnt = userMapper.selectGu(userid);
    int chocnt = userMapper.selectCho(userid);
    int pacnt = userMapper.selectPa(userid);
    int starcnt = userMapper.selectStar(userid);

    model.addAttribute("gucnt", gucnt);
    model.addAttribute("chocnt", chocnt);
    model.addAttribute("pacnt", pacnt);
    model.addAttribute("starcnt", starcnt);
    return "main.html";
  }
}
