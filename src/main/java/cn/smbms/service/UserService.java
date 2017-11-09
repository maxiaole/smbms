/**
 *  UserService TODO()
 */
package cn.smbms.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.User;
import cn.smbms.tools.Page;

/**
 * @author ggy
 *
 */
public interface UserService {
	
	User findByName(String name);
	
	Integer saveUser(User user);
	
	Page<User> findByPage(Map<String, Object> map);
	
	Integer delectUser(@Param("id")Integer id);
	
	/**
	 * updateUser TODO(用户修改)
	 * @param user
	 * @return
	 */
	Integer updateUser(User user);
	
	User findByid(@Param("id")Integer id);
	
	Integer updatePwd(User user);
}	
