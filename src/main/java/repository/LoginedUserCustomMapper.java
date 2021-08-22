package repository;

import org.mybatis.cdi.Mapper;

@Mapper
public interface LoginedUserCustomMapper {

    int deleteOldestUser(String id);
}
