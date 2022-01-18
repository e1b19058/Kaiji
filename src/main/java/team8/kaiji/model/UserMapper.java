package team8.kaiji.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface UserMapper {

  @Select("select * from users;")
  ArrayList<User> selectAllUser();

  @Insert("insert into users (name,gu,cho,pa,star) values (#{name},2,2,2,3);")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUser(User user);

  @Select("select name from users where id = #{id};")
  String selectNameById(int id);

  @Select("select name from users")
  ArrayList<String> selectAllUserName();

  @Select("select id from users where name=#{name};")
  int selectIdByName(String name);

  @Select("select * from users")
  ArrayList<User> selectAllUsers();

  @Select("select gu from users where id=#{userid}")
  int selectGu(int userid);

  @Select("select cho from users where id=#{userid}")
  int selectCho(int userid);

  @Select("select pa from users where id=#{userid}")
  int selectPa(int userid);

  @Update("Update Users set gu=#{Gu} where id=#{userid}")
  void updateGu(int Gu, int userid);

  @Update("Update Users set cho=#{Cho} where id=#{userid}")
  void updateCho(int Cho, int userid);

  @Update("Update Users set pa=#{Pa} where id=#{userid}")
  void updatePa(int Pa, int userid);

  @Select("Select star from Users where id=#{userid}")
  int selectStar(int userid);

  @Update("Update Users set star=#{star} where id=#{userid}")
  void updateStar(int star, int userid);

  @Delete("Delete from users where id = #{id}")
  boolean deleteUserById(int id);

}
