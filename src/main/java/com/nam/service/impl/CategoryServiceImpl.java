package com.nam.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nam.entity.Category;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectAlreadyExistedException;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.repository.ICategoryRepository;
import com.nam.service.ICategoryService;
import java.util.Collections;

@Service
@PropertySource(value = "messages.properties", encoding = "utf-8")
public class CategoryServiceImpl implements ICategoryService {
	@Autowired
	private ICategoryRepository categoryRepo;
	
	@Autowired
	private Environment env;

	/* Lấy thể loại từ ID */
	@Override
	public Optional<Category> findById(Long id) {
		return categoryRepo.findById(id);
	}

	/* Lấy danh sách tất cả thể loại */
	@Override
	public List<Category> findAll() {
		return categoryRepo.findAll();
	}

	/* Nhận vào category để thực hiện lưu */
	@Override
	public Message save(Category category) {
		Category checkCategory = categoryRepo.findByCategoryTitle(category.getCategoryTitle());

		if (checkCategory != null)
			throw new ObjectAlreadyExistedException(env.getProperty("message.existed.category"));
		categoryRepo.save(category);
		return new Message(env.getProperty("message.save.success"));
	}

	/* Lấy ra list thể loại dựa vào tham số truyền vào */
	@Override
	public List<Category> findAll(int pageNo, int pageSize, String sortBy, String seachFor) {
		Page<Category> page = getPageCategory(pageNo, pageSize, sortBy, seachFor);
		if (!page.hasContent())
			return Collections.emptyList();
		return page.getContent();
	} 

	/* Lấy ra page thể loại dựa vào tham số truyền vào */
	@Override
	public Page<Category> getPageCategory(int pageNo, int pageSize, String sortBy, String searchFor) {
		
		int partitionIndex = sortBy.indexOf("-");
		String sortByWord = sortBy.substring(0, partitionIndex);
		String ascOrDesc = sortBy.substring(partitionIndex + 1);
		Direction direction = Direction.ASC;
		if (ascOrDesc.equalsIgnoreCase("DESC"))
			direction = Direction.DESC;
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction ,sortByWord));
		Page<Category> pageCategory = categoryRepo.findAllByPageableAndSearch(searchFor, pageable);
		return pageCategory;
	}

	/* Nhận vào id thể loại để xóa */
	@Override
	public Message delete(Long id) {
		Optional<Category> delCategory=categoryRepo.findById(id);
		if(delCategory.isPresent()) {
			categoryRepo.delete(delCategory.get());
			return new Message(env.getProperty("message.delete.category.success"));
		} else {
			throw new ObjectNotFoundException(env.getProperty("message.existed.category"));
		}
	}

}













