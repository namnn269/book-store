package com.nam.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nam.dto.AdminBookDto;
import com.nam.dto.DisplayBookDto;
import com.nam.entity.Book;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.mapper.IBookMapper;
import com.nam.repository.IBookRepository;
import com.nam.repository.IOrderDetailRepository;
import com.nam.service.IBookService;

@Service
public class BookServiceImpl implements IBookService {
	@Autowired
	private IBookRepository bookRepo;
	
	@Autowired
	private IBookMapper bookMapper;
	
	@Autowired
	private IOrderDetailRepository orderDetailRepo;
	
	@Value("${upload.img.path}")
	private String uploadImgPath;
	
	@Value("${get.img.path}")
	private String getImgPath;
	
	@Value("${get.default.img.path}")
	private String getDefaultImgPath;
	
	private final int TOP_BEST_SELLER_AMOUNT = 5;

	@Override
	public Optional<Book> findById(Long id) {
		return bookRepo.findById(id);
	}
 
	@Override
	public List<Book> findAll() {
		return bookRepo.findAll();
	} 

	@Override
	public Message save(AdminBookDto bookDto, MultipartFile multiFile) throws IOException {
		Book book = bookMapper.fromAdminBookDtoToBook(bookDto);
		if (book.getId() == null) {
			if (multiFile.isEmpty()) 
				book.setImgLink(getDefaultImgPath);
			else	
				bookRepo.save(book);
		}

		if (multiFile.isEmpty()) {
			bookRepo.save(book);
			return new Message("Thành công! ID: " + book.getId());
		}
		try {
			
			String oldImgFolderPath = uploadImgPath + book.getId();
			File dir = new File(oldImgFolderPath);
			if (dir.exists())
				for (File file : dir.listFiles()) {
					if (!file.isDirectory())
						file.delete();
				}

			InputStream inputStream = multiFile.getInputStream();
			Path uploadPath = Paths.get(uploadImgPath + book.getId());

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			String fileName = multiFile.getOriginalFilename();
			book.setImgLink(getImgPath + book.getId() + "/" + fileName);
			bookRepo.save(book);

			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Không thể lưu ảnh!");
		}

		return new Message("Thành công! ID: " + book.getId());
	}

	@Override
	public List<DisplayBookDto> findAll(int pageNo, int pageSize, String searchKey, long categoryId, String sortByPrice
		) {
		Page<Book> page = getPageBook(pageNo, pageSize, searchKey, categoryId, sortByPrice);
		if (!page.hasContent())
			return Collections.emptyList();

		return page.getContent().stream().map(book -> {
			return bookMapper.fromBookToDisplayBookDto(book);
		}).collect(Collectors.toList());
	}

	@Override
	public Page<Book> getPageBook(int pageNo, int pageSize, String searchKey, long categoryId, String sortByPrice) {
		Direction priceDirection;
		if (sortByPrice.equalsIgnoreCase("price-asc"))
			priceDirection = Direction.ASC; 
		else
			priceDirection = Direction.DESC;

		sortByPrice="price";
		Sort sort = Sort.by(priceDirection, sortByPrice); 
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Book> page;
		if (categoryId != 0)
			page = bookRepo.findAllBySearchAndFilterAndSort(searchKey, categoryId, pageable);
		else
			page = bookRepo.findAllBySearchAndSort(searchKey, pageable);
		return page;
	}

	@Override
	public Message delete(Long id) {
		Optional<Book> opBook = bookRepo.findById(id);
		if (!opBook.isPresent())
			throw new ObjectNotFoundException("Không tìm thấy sách");

		Book delBook = opBook.get();

		if (!delBook.getImgLink().equals(getDefaultImgPath)) {
			String folderDeleleLink = uploadImgPath + delBook.getId();
			File folderDelete = new File(folderDeleleLink);
			try {
				FileUtils.deleteDirectory(folderDelete);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		delBook.removeCategory();
		delBook.removeAllAuthors();
		delBook.getOrderDetails().stream().forEach(x -> {
			x.getOrder().removeOrderDetail(x);
			orderDetailRepo.delete(x);
		});
		delBook.removeAllOrderDetail();
		bookRepo.delete(delBook);
		return new Message("Xóa sách thành công! ID: " + id);

	}

	@Override
	public AdminBookDto getUpdateBook(Long id) {
		Optional<Book> opBook = bookRepo.findById(id);
		if(!opBook.isPresent())throw new ObjectNotFoundException("Không tìm thấy sách!");
		AdminBookDto bookDto= new AdminBookDto();
		bookDto=bookMapper.fromBookToAdminDtoBook(opBook.get());
		return bookDto;
	}

	@Override
	public DisplayBookDto findBookDetailById(Long id) {
		Optional<Book> opBook= bookRepo.findById(id);
		if(!opBook.isPresent()) throw new ObjectNotFoundException("Không tìm thấy sách");
		DisplayBookDto bookDto=bookMapper.fromBookToDisplayBookDto(opBook.get());
		return bookDto;
	}

	@Override
	public List<DisplayBookDto> findBestSellerBooks() {
		Pageable pageable=PageRequest.of(0, TOP_BEST_SELLER_AMOUNT);
		Page<Book> bookPage=orderDetailRepo.findTopBook(pageable);
		
		if(bookPage.isEmpty())
			return Collections.emptyList();
		
		return bookPage.getContent().stream()
				.map(bookMapper::fromBookToDisplayBookDto)
				.collect(Collectors.toList());
	}
}












