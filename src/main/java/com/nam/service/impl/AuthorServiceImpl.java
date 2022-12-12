package com.nam.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nam.entity.Author;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.repository.IAuthorRepository;
import com.nam.service.IAuthorService;

@Service
public class AuthorServiceImpl implements IAuthorService {

	@Autowired
	private IAuthorRepository authorRepo;

	@Override
	public Optional<Author> findById(Long id) {
		Optional<Author> author=authorRepo.findById(id);
		if(author.isPresent()) {
			return author;
		} else {
			throw new ObjectNotFoundException("Không tìm thấy tác giả");
		}
	}

	@Override
	public List<Author> findAll() {
		return authorRepo.findAll();
	}

	@Override
	public Message save(Author author) {
		authorRepo.save(author);
		return new Message("Thành công! ID: "+author.getId());
	}

	@Override
	public List<Author> findAll(int pageNo, int pageSize, String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Author> page = authorRepo.findAll(pageable);
		if(!page.hasContent()) return Collections.emptyList();
		return page.getContent();
	}

	@Override
	public Page<Author> getPageAuthor(int pageNo, int pageSize, String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return authorRepo.findAll(pageable);
	}

	@Override
	public Message delete(Long id) {
		Optional<Author> opAuthor=authorRepo.findById(id);
		if(opAuthor.isPresent()) {
			Author delAuthor=opAuthor.get();
			delAuthor.getBooks().stream().forEach(x->x.removeAuthor(delAuthor));
			authorRepo.delete(delAuthor);
			return new Message("Xóa thành công! ID: "+id);
		} else {
			throw new ObjectNotFoundException("Không tìm thấy tác giả");
		}
		
	}

}