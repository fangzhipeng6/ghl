package org.crazyit.demo.service;

import org.crazyit.demo.dao.ProductDao;
import org.crazyit.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	@Autowired
	private ProductDao productDao;

	//查询待专家信息
	public Page<Product> findAll1(Pageable pageable) {
		Example<Product> example = getMedical();
		return productDao.findAll(example,pageable);
	}

	private Example<Product> getMedical() {
		Product product = new Product();
		product.setType("medical");
		return Example.of(product);
	}
	
	public Product findById(Integer productId) {
        return productDao.findById(productId).get();
    }
    
	public void save(Product product) {
        productDao.save(product);
    }
    
    public Page<Product> findAll(Pageable pageable) {
        Example<Product> example = getNotDeleteExample();
        return productDao.findAll(example, pageable);
    }
    
    /**
     * 返回一个设置了 isDelete 为 false 的Example
     * @return
     */
    private Example<Product> getNotDeleteExample() {
        // 只查询没有删除的图书
        Product product = new Product();
        product.setIsDelete(false);
        return Example.of(product);
    }
    
    public void delete(Integer[] ids) {
        for(Integer id : ids) {
            Product product = findById(id);
            product.setIsDelete(true);
            save(product);
        }
    }
    
    public Page<Product> testFindAll(Pageable pageable) {
        return productDao.findAll(pageable);
    }
	
	
}
