package com.java456.dao;


import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.java456.entity.Client;


public interface ClientDao extends JpaRepository<Client,Integer>,JpaSpecificationExecutor<Client>  {
	
	@Query(value="select * from t_client where id = ?1",nativeQuery = true)
	public Client findId(Integer id);
	
	//bianhao LIKE concat('%',?,'%')
	//需要配置nativeQuery true 代表支持sql
	@Query(value="select * from t_client t where concat(t.bianhao,t.name) like concat('%',?,'%')",nativeQuery = true)
	List<Client> fuzzyFind(String value);
}
