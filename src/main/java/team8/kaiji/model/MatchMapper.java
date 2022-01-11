package team8.kaiji.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchMapper {
  @Insert("insert into match (user1id,user2id,user1hand,user2hand, IsAct) values (#{user1id},#{user2id},#{user1hand},NULL,#{isAct});")
  @Options(useGeneratedKeys = true, keyColumn = "matchid", keyProperty = "matchid")
  void insertMatchPlayer1(Match match);

  @Select("select * from match; ")
  ArrayList<Match> selectAllMatch();

  @Select("select matchid from match where user2id = #{user2id};")
  ArrayList<Integer> selectMatchIdByUserid(int user2id);

  @Update("Update Match set user2hand=#{user2hand}, isAct=#{isAct} where matchid = #{matchid} ")
  void updateUser2Hand(Match match);

  @Select("select matchid from match join users on (match.user1id = users.id) where users.name = #{User1Name} ")
  int selectMatchIdByUser1Name(String User1Name);

  @Select("select matchid from match join users on (match.user2id = users.id) where users.name = #{User2Name} and isAct = 0")
  int selectMatchIdByUser2Name(String User2Name);

  @Select("select IsAct from match where matchid=#{MatchId}")

  int selectIsActByMatchId(int MatchId);

  @Select("select user1hand from match where matchid = #{MatchId}")
  String selectUser1handByMatchId(int MatchId);

  @Select("select user2hand from match where matchid = #{MatchId}")
  String selectUser2handByMatchId(int MatchId);

  @Select("select matchid from match join users on (match.user1id = users.id) where match.user2id=#{id} and users.name=#{name}")
  String selectIdByUser2idAndUser1name(int id, String name);

}
