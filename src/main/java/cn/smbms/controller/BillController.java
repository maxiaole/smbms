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

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.service.BillService;
import cn.smbms.tools.Page;
/**
 * 订单管理系统
 * @author Administrator
 *
 */
@Controller
public class BillController {
	@Autowired
	private BillService billService;

	public void setBillService(BillService billService) {
		this.billService = billService;
	}
	/**
	 * 点击实现页面展示
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bill.html", method = RequestMethod.GET)
	public String BillShou(Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("index", 1);
		map.put("size", 5);
		Page<Bill> page = billService.findBillByPage(map);
		model.addAttribute("page", page);
		return "billlist";
	}

	/**
	 * 查询全部信息和模糊查询
	 * @param model
	 * @param pageIndex
	 * @param queryname
	 * @param queryUserRole
	 * @param method
	 * @return
	 */
	@RequestMapping(value = "bill.html", method = RequestMethod.POST)
	public String BillShow(
			Model model,
			@RequestParam(value = "pageIndex", required = false) String pageIndex,
			@RequestParam(value = "queryProductName", required = false) String queryProductName,
			@RequestParam(value = "queryProviderId", required = false) String queryProviderId,
			@RequestParam(value = "queryIsPayment", required = false) String ispayment,
			@RequestParam(value = "method", required = false) String method) {
		List<Provider> list=billService.showProviderinfo();
		model.addAttribute("providerList",list);
		// 这里应该有一个首页面的展示 Page分页对象 显示首页信息
		// 用来实现分页展示
		if ("".equals(pageIndex) || null == pageIndex) {
			pageIndex = "1";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (queryProductName != null && !"".equals(queryProductName)) {
			map.put("productName", queryProductName);
		}
		if (queryProviderId != null && !"".equals(queryProviderId) && !"0".equals(queryProviderId)) {
			map.put("providerId", queryProviderId);
		}
		if (ispayment != null && !"".equals(ispayment) && !"0".equals(ispayment)) {
			map.put("ispayment", ispayment);
		}
		map.put("index", pageIndex);
		map.put("size", 5);
		Page<Bill> page = billService.findBillByPage(map);
		model.addAttribute("queryProductName", map.get("productName"));
		model.addAttribute("queryProviderId", map.get("providerId"));	
		model.addAttribute("queryIsPayment", map.get("ispayment"));	
		model.addAttribute("page", page);
		return "billlist";
	}
	/**
	 * userAdd TODO(进去用户新增界面)
	 * 
	 * @return
	 */
	@RequestMapping(value = "billAdd.html", method = RequestMethod.GET)
	public String providerAdd() {
		return "billadd";
	}
	
	/**
	 * 添加 
	 * @param model
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "billAdd.html", method = RequestMethod.POST)
	public String billAdd(Model model, Bill bill, HttpSession session) {
		if (session.getAttribute("BILL_CODE") != null) {
			Bill u = (Bill) session.getAttribute("BILL_CODE");
			bill.setCreatedBy(u.getId());
		} else {
			bill.setCreatedBy(1);
		}
		bill.setCreationDate(new Date());
		Integer id = billService.addBill(bill);
		if (id > 0) {
			return "redirect:bill.html";
		} else {
			model.addAttribute("message", "抱歉因为网络原因添加失败！");
			return "billadd";
		}
	}
	/**
	 * Provider TODO(跳转添加页面的时候实现角色的展示)
	 * 
	 * @return
	 */
	@RequestMapping("billder.json")
	@ResponseBody
	public Object billList() {
		List<Provider> list = billService.showProviderinfo();
		return JSON.toJSONString(list);
	}
	
	/**
	 * billid TODO(通过ID获取用户信息)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "billview.html", method = RequestMethod.GET)
	public String billview(Model model
			,@RequestParam(value = "billid", required = false) String id) {
		Bill bill = billService.findBillByid(Integer.parseInt(id));
		model.addAttribute("bill", bill);
		return "billview";
		
		
	}

	
	/**
	 * 删除
	 */
	@RequestMapping(value = "billdel.json",method=RequestMethod.GET)
	@ResponseBody
	public Object billdel(Model model
			,@RequestParam(value = "billid", required = false) String billid) {
		JSONObject json = new JSONObject();
		Integer result = billService.delectBill(Integer.parseInt(billid));
		System.out.println(billid);
		System.out.println(result);
		if(result == 1){
			json.put("delResult", "true");
		}else{
			json.put("delResult", "false");
		}
		return json;
	}
	
	
	
	
	
	/**
	 * 跳转修改页面
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "billmodify.html", method = RequestMethod.GET)
	public String billmodify(Model model
			,@RequestParam(value = "billid", required = false) String id) {
		Bill bill = billService.findBillByid(Integer.parseInt(id));
		model.addAttribute("bill", bill);
		return "billmodify";
	}
	/**
	 * 修改
	 * @param model
	 * @param bill
	 * @param session
	 * @return
	 */
	
	@RequestMapping(value = "billUpdate.html", method = RequestMethod.POST)
	public String billUpdate(Model model, Bill bill, HttpSession session) {
		if (session.getAttribute("BILL_CODE") != null) {
			Bill u = (Bill) session.getAttribute("BILL_CODE");
			bill.setCreatedBy(u.getId());
		} else {
			bill.setCreatedBy(1);
		}
		bill.setCreationDate(new Date());
		Integer id = billService.updateBill(bill);
		if (id > 0) {
			return "redirect:bill.html";
		} else {
			model.addAttribute("message", "抱歉因为网络原因添加失败！");
			return "billmodify";
		}
	}
}
