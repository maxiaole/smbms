package cn.smbms.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import cn.smbms.pojo.Provider;

public interface ProviderMapper {

	List<Provider> findProviderByPage(Map<String,Object> map);

	Integer countByPage(Map<String,Object> map);

	Integer delectProvider(@Param("id")Integer id);

	Integer addProvider(Provider provider);

	Integer updateProvider(Provider provider);

	Provider findProviderByid(@Param("id")Integer id);

}
