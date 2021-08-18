package repository;

import dto.Role;
import dto.RoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.cdi.Mapper;

@Mapper
public interface RoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.role
     *
     * @mbg.generated
     */
    long countByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.role
     *
     * @mbg.generated
     */
    int deleteByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.role
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(@Param("emailaddress") String emailaddress, @Param("role") String role);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.role
     *
     * @mbg.generated
     */
    int insert(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.role
     *
     * @mbg.generated
     */
    int insertSelective(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.role
     *
     * @mbg.generated
     */
    List<Role> selectByExampleWithRowbounds(RoleExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.role
     *
     * @mbg.generated
     */
    List<Role> selectByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.role
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.role
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);
}