package team8.kaiji.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

  @Select("select * from users;")
  ArrayList<User> selectAllUser();

  @Insert("insert into users (name,gu,cho,pa,star) values (#{name},2,2,2,3);")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUser(User user);

  @Select("select name from users where id = #{id};")
  String selectNameById(int id);
}
