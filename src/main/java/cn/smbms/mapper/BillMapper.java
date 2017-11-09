package cn.smbms.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;

public interface BillMapper {
	
	List<Bill> findBillByPage(Map<String,Object> map);

	Integer countByPage(Map<String,Object> map);

	Integer delectBill(@Param("id")Integer id);

	Integer addBill(Bill bill);

	Integer updateBill(Bill bill);

	Bill findBillByid(@Param("id")Integer id);

	List<Provider> showProviderinfo();
}
