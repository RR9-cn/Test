package com.java456.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity     //创建实体类
@Table(name="t_client")   //创建表和实体的映射关系
public class Client {
	
	@Id //声明主键配置
	@GeneratedValue(strategy = GenerationType.IDENTITY) //主键生成策略 
	private Integer id;
	
	@Column(length=20)
	private String bianhao;
	
	@Column(length=20)
	private String name;
	
	@Column(length=20)
	private String phone;
	
	@Column(length=200)
	private String remark;
	
	@Temporal(TemporalType.TIMESTAMP) 
	private Date  createDateTime;
	
	
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBianhao() {
		return bianhao;
	}
	
	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	

	@JsonSerialize(using=CustomDateTimeSerializer.class)
	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}
	
	
	
	
	
	
	
}
