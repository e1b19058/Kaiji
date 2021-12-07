package team8.kaiji.model;

public class Match {
  int matchid;
  int user1id;
  int user2id;
  String user1hand;
  String user2hand;

  public int getMatchid() {
    return matchid;
  }

  public void setMatchid(int matchid) {
    this.matchid = matchid;
  }

  public int getUser1id() {
    return user1id;
  }

  public void setUser1id(int user1id) {
    this.user1id = user1id;
  }

  public int getUser2id() {
    return user2id;
  }

  public void setUser2id(int user2id) {
    this.user2id = user2id;
  }

  public String getUser1hand() {
    return user1hand;
  }

  public void setUser1hand(String user1hand) {
    this.user1hand = user1hand;
  }

  public String getUser2hand() {
    return user2hand;
  }

  public void setUser2hand(String user2hand) {
    this.user2hand = user2hand;
  }
}
