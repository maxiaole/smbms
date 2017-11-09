package cn.smbms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.service.ProviderService;
import cn.smbms.tools.Page;

@Controller
public class ProviderController {
	@Autowired
	private ProviderService providerService;

	public void setProviderService(ProviderService providerService) {
		this.providerService = providerService;
	}
	/**
	 * 点击实现页面展示
	 * @param model
	 * @return
	 */
	@RequestMapping(value="provider.html",method=RequestMethod.GET)
	public String ProviderShow(Model model){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("index", 1);
		map.put("size", 5);
		Page<Provider> page=providerService.findProviderByPage(map);
		model.addAttribute("page",page);
		return "providerlist";
	}
	/**
	 * 查询全部信息 和模糊查询  
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "provider.html", method = RequestMethod.POST)
	public String ProviderShowYm(Model model,
			@RequestParam(value="pageIndex",required=false)String pageIndex,
			@RequestParam(value="queryProCode",required=false)String queryProCode,
			@RequestParam(value="queryProName",required=false)String queryProName,
			@RequestParam(value = "method", required = false) String method){
		if ("".equals(pageIndex) || null == pageIndex) {
			pageIndex = "1";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (queryProCode != null && !"".equals(queryProCode)) {
			map.put("proCode", queryProCode);
		}
		if (queryProName != null && !"".equals(queryProName)) {
			map.put("proName", queryProName);
		}
		map.put("index", pageIndex);
		map.put("size", 5);
		Page<Provider> page = providerService.findProviderByPage(map);
		model.addAttribute("queryProCode", map.get("proCode"));
		model.addAttribute("queryProName", map.get("proName"));
		model.addAttribute("page", page);
		return "providerlist";
	}
	/**
	 * 跳转添加页面
	 * @return
	 */
	@RequestMapping(value="provideradd.html",method=RequestMethod.GET)
	public String  providerAdd(){
		return "provideradd";		
	}
	/**
	 * 添加用户
	 * @param model
	 * @param provider
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "provideradd.html", method = RequestMethod.POST)
	public String providerAdd(Model model, Provider provider, HttpSession session) {
	if (session.getAttribute("PRO_CODE") != null) {
			Provider p = (Provider) session.getAttribute("PRO_CODE");
			provider.setCreatedBy(p.getId());
		} else {
			provider.setCreatedBy(1);
		}
		System.out.println("uhkjsdfjlasdjflkjasdf");
		System.out.println(provider.getProName());
		provider.setCreationDate(new Date());
		Integer id = providerService.addProvider(provider);
		System.out.println(id+"--------------");
		if (id > 0) {
			return "redirect:provider.html";
		} else {
			model.addAttribute("message", "抱歉因为网络原因添加失败！");
			return "provideradd";
		}
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "providerdel.json",method=RequestMethod.GET)
	@ResponseBody
	public Object billdel(Model model
			,@RequestParam(value = "proid", required = false) String proid) {
		JSONObject json = new JSONObject();
		Integer result = providerService.delectProvider(Integer.parseInt(proid));
		System.out.println(proid);
		System.out.println(result);
		if(result == 1){
			json.put("delResult", "true");
		}else{
			json.put("delResult", "false");
		}
		return json;
	}
	/**
	 * 查看信息
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="providerview.html",method=RequestMethod.GET)
public String providerview(Model model
		,@RequestParam(value = "proid", required = false) String id){
		Provider provider=providerService.findProviderByid(Integer.parseInt(id));
		model.addAttribute("provider",provider);
		return "providerview";
}
	/**
	 * 修改
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="providermodify.html",method=RequestMethod.GET)
	public String providermodify(Model model,
			@RequestParam(value="proid",required=false) String id){
		Provider provider=providerService.findProviderByid(Integer.parseInt(id));
		model.addAttribute("provider",provider);
		return "providermodify";
		
	}
	/**
	 * 修改
	 * @param model
	 * @param provider
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "providerupdate.html", method = RequestMethod.POST)
	public String providerUpdate(Model model, Provider provider, HttpSession session) {
	if (session.getAttribute("PRO_CODE") != null) {
			Provider p = (Provider) session.getAttribute("PRO_CODE");
			provider.setCreatedBy(p.getId());
		} else {
			provider.setCreatedBy(1);
		}
		System.out.println("uhkjsdfjlasdjflkjasdf");
		System.out.println(provider.getProName());
		provider.setCreationDate(new Date());
		Integer id = providerService.updateProvider(provider);
		System.out.println(id+"--------------");
		if (id > 0) {
			return "redirect:provider.html";
		} else {
			model.addAttribute("message", "抱歉因为网络原因添加失败！");
			return "providermodify";
		}
	}

}
