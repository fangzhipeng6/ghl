package org.crazyit.demo.dao;

import java.util.List;

import org.crazyit.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Integer> {//jpa接口，如果业务需要即提供CRUD操作，又需要提供分页以及排序功能使用它
	 /**
	  * 根据名称和密码查询用户数据
     */
	public Product findByName(String name);//spring-data-jpa会根据方法的名字来自动生成sql语句，我们只需要按照方法定义的规则即可
	List<Product> findTop3ByIsDeleteOrderByIdDesc(boolean isDelete);//  查询前3条 isDelete 的数据，并且会根据 id 排序
}
