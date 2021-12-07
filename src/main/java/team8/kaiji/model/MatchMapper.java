package team8.kaiji.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchMapper {
  @Insert("insert into match (user1id,user2id,user1hand,user2hand) values (#{user1id},#{user2id},#{user1hand},NULL);")
  @Options(useGeneratedKeys = true, keyColumn = "matchid", keyProperty = "matchid")
  void insertMatchPlayer1(Match match);

  @Select("select * from match; ")
  ArrayList<Match> selectAllMatch();
}
