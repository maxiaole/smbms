/**
 *  UserController TODO()
 */
package cn.smbms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.RoleService;
import cn.smbms.service.UserService;
import cn.smbms.tools.Page;

/**
 * @author ggy
 * 
 */
@Controller
public class UserController {
	@Autowired
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	private RoleService roleService;

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * login get TODO(get方法提交只是用来跳转login界面的)
	 * 
	 * @return
	 */
	@RequestMapping(value = "login.html", method = RequestMethod.GET)
	public String login() {
		return "../login";
	}

	/**
	 * islogin TODO(post提交用来做用户登录方法实现的)
	 * 
	 * @param model
	 * @param userCode
	 * @param userPassword
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "login.html", method = RequestMethod.POST)
	public String islogin(Model model,
			@RequestParam("userCode") String userCode,
			@RequestParam("userPassword") String userPassword,
			HttpSession session) {
		User user = userService.findByName(userCode);
		if (user != null) {
			if (user.getUserPassword().equals(userPassword)) {
				if (session.getAttribute("USER_CODE") != null) {
					session.removeAttribute("USER_CODE");
				}
				session.setAttribute("USER_CODE", user);
				session.setMaxInactiveInterval(60000);
				return "frame";
			} else {
				model.addAttribute("error", "抱歉密码不正确");
				return "../login";
			}
		} else {
			model.addAttribute("error", "抱歉账户名不正确");
			return "../login";
		}

	}

	/**
	 * userShou TODO(点击用户模块显示首页面信息)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "user.html", method = RequestMethod.GET)
	public String userShou(Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("index", 1);
		map.put("size", 5);
		Page<User> page = userService.findByPage(map);
		model.addAttribute("page", page);
		return "userlist";
	}
	
	/**
	 * userview TODO(通过ID获取用户信息)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "userview.html", method = RequestMethod.GET)
	public String userview(Model model
			,@RequestParam(value = "uid", required = false) String uid) {
		User user = userService.findByid(Integer.parseInt(uid));
		model.addAttribute("user", user);
		return "userview";
	}
	

	/**
	 * userview TODO(跳转到更新页面)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "usermodify.html", method = RequestMethod.GET)
	public String usermodify(Model model
			,@RequestParam(value = "uid", required = false) String uid) {
		User user = userService.findByid(Integer.parseInt(uid));
		model.addAttribute("user", user);
		return "usermodify";
	}
	
	/**
	 * userdel TODO(通过ID删除对象)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "userdel.json",method=RequestMethod.POST)
	@ResponseBody
	public Object userdel(Model model
			,@RequestParam(value = "uid", required = false) String uid) {
		Integer result = userService.delectUser(Integer.parseInt(uid));
		JSONObject json = new JSONObject();
		if(result == 1){
			json.put("delResult", "true");
		}else{
			json.put("delResult", "false");
		}
		return json;
	}
	
	/**
	 * userShow TODO(点击查询用户数据)
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "user.html", method = RequestMethod.POST)
	public String userShow(
			Model model,
			@RequestParam(value = "pageIndex", required = false) String pageIndex,
			@RequestParam(value = "queryname", required = false) String queryname,
			@RequestParam(value = "queryUserRole", required = false) String queryUserRole,
			@RequestParam(value = "method", required = false) String method) {
		List<Role> list = roleService.findByRoleAll();
		model.addAttribute("roleList", list);
		// 这里应该有一个首页面的展示 Page分页对象 显示首页信息
		// 用来实现分页展示
		if ("".equals(pageIndex) || null == pageIndex) {
			pageIndex = "1";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (queryname != null && !"".equals(queryname)) {
			map.put("userName", queryname);
		}
		if (queryUserRole != null && !"".equals(queryUserRole) && !"0".equals(queryUserRole)) {
			map.put("roleId", queryUserRole);
		}
		map.put("index", pageIndex);
		map.put("size", 5);
		Page<User> page = userService.findByPage(map);
		model.addAttribute("queryname", map.get("userName"));
		model.addAttribute("queryUserRole", map.get("roleId"));
		model.addAttribute("page", page);
		return "userlist";
	}

	/**
	 * userAdd TODO(进去用户新增界面)
	 * 
	 * @return
	 */
	@RequestMapping(value = "userAdd.html", method = RequestMethod.GET)
	public String userAdd() {
		// userAdd.html
		return "useradd";
	}

	/**
	 * getUserCode TODO(判断该用户编号是否存在)
	 * 
	 * @param userCode
	 * @return
	 */
	@RequestMapping("getUserCode.json")
	@ResponseBody
	public Object getUserCode(@RequestParam("userCode") String userCode) {
		User user = userService.findByName(userCode);
		JSONObject json = new JSONObject();
		if (user != null) {
			json.put("userCode", "exist");
		} else {
			json.put("userCode", "success");
		}
		return json;
	}
	
	@RequestMapping(value = "userUpdate.html", method = RequestMethod.POST)
	public String userUpdate(Model model, User user, HttpSession session) {
		if (session.getAttribute("USER_CODE") != null) {
			User u = (User) session.getAttribute("USER_CODE");
			user.setModifyBy(u.getId());
		} else {
			user.setModifyBy(1);
		}
		user.setModifyDate(new Date());
		Integer id = userService.updateUser(user);
		if (id > 0) {
			return "redirect:user.html";
		} else {
			model.addAttribute("message", "抱歉因为网络原因更新失败！");
			return "usermodify";
		}
	}
	
/**
 *添加
 * @param model
 * @param user
 * @param session
 * @return
 */
	@RequestMapping(value = "userAdd.html", method = RequestMethod.POST)
	public String userAdd(Model model, User user, HttpSession session) {
		if (session.getAttribute("USER_CODE") != null) {
			User u = (User) session.getAttribute("USER_CODE");
			user.setCreatedBy(u.getId());
		} else {
			user.setCreatedBy(1);
		}
		user.setCreationDate(new Date());
		Integer id = userService.saveUser(user);
		if (id > 0) {
			return "redirect:user.html";
		} else {
			model.addAttribute("message", "抱歉因为网络原因添加失败！");
			return "useradd";
		}
	}

	/**
	 * roleList TODO(跳转添加页面的时候实现角色的展示)
	 * 
	 * @return
	 */
	@RequestMapping("role.json")
	@ResponseBody
	public Object roleList() {
		List<Role> list = roleService.findByRoleAll();
		return JSON.toJSONString(list);
	}
	/**
	 * 跳转到修改密码页面
	 * @return
	 */
	@RequestMapping(value="updatepwd.html",method=RequestMethod.GET)
	public String updatePwd(){	
		return "pwdmodify";
	}
	/**
	 * userupd TODO(更改密码)
	 * @param model
	 * @param session
	 * @param oldpassword
	 * @return
	 */
	@RequestMapping(value = "updatepwd.html", method = RequestMethod.POST)
	public String userupd(Model model, HttpSession session
			,@RequestParam(value="rnewpassword", required =false)String rnewpassword) {
		User user = new User();
		if (session.getAttribute("USER_CODE") != null) {
			User u = (User) session.getAttribute("USER_CODE");
			user.setId(u.getId());
		}
		user.setUserPassword(rnewpassword);
		Integer id = userService.updatePwd(user);
		if (id > 0) {
			if (session.getAttribute("USER_CODE") != null) {
				session.removeAttribute("USER_CODE");
			}
			return "../login";
		} else {
			model.addAttribute("message", "抱歉因为网络原因更改密码失败！");
			return "pwdmodify";
		}
	}
	/**
	 * userpwdupd TODO(密码失去焦点验证)
	 * @return
	 */
	@RequestMapping(value = "userpwdupd.json", method = RequestMethod.POST)
	@ResponseBody
	public Object userpwdupd(Model model, HttpSession session,
			@RequestParam(value="oldpassword", required =false)String oldpassword){
		JSONObject json = new JSONObject();
		if(session.getAttribute("USER_CODE") !=null){
			if(oldpassword != null || !"".equals(oldpassword)){
				User u = (User)session.getAttribute("USER_CODE");
				if(u.getUserPassword().equals(oldpassword)){
					json.put("result", "true");
				}else{
					json.put("result", "false");
				}
			}else{
				json.put("result", "error");
			}
		}else{
			json.put("result", "sessionerror");
		}
		return json;
	}
}
