/**
 *  UserMapper TODO()
 */
package cn.smbms.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.User;

/**
 * @author ggy
 *
 */
public interface UserMapper {
	/**
	 * findByName TODO(登录、查询提示、详情)
	 * @param name
	 * @return
	 */
	User findByName(@Param("name")String name);
	/**
	 * findByQuery TODO(动态查询+分页)
	 * @param map
	 * @return
	 */
	List<User> findByQuery(Map<String, Object> map);
	/**
	 * countByQuery TODO(总条数)
	 * @param map
	 * @return
	 */
	Integer countByQuery(Map<String, Object> map);
	/**
	 * saveUser  TODO(用户新增)
	 * @param user
	 * @return
	 */
	Integer saveUser(User user);
	/**
	 * updateUser TODO(用户修改)
	 * @param user
	 * @return
	 */
	Integer updateUser(User user);
	/**
	 * deleteUser TODO(删除用户)
	 * @param id
	 * @return
	 */
	Integer deleteUser(@Param("id")Integer id);
	
	/**
	 * findByid TODO(通过ID获取用户信息)
	 * @param id
	 * @return
	 */
	User findByid(@Param("id")Integer id);
	
	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	Integer updatePwd(User user);
	
}
