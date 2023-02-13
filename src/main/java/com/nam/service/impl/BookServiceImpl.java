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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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

@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
@Service
public class BookServiceImpl implements IBookService {
	@Autowired
	private IBookRepository bookRepo;
	
	@Autowired
	private IBookMapper bookMapper;
	
	@Autowired
	private IOrderDetailRepository orderDetailRepo;
	
	@Autowired
	private Environment env;
	
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

	/* Thực hiện lưu sách mới hoặc update sách đã tồn tại trong database */
	@Override
	public Message save(AdminBookDto bookDto, MultipartFile multiFile) throws IOException {
		Book book = bookMapper.fromAdminBookDtoToBook(bookDto);
		// TH thêm mới sách, ID == null
		if (book.getId() == null) {
			if (multiFile.isEmpty())
				// Nếu không có link online hoặc ảnh upload từ máy tính thì sẽ dùng hình ảnh mặc định
				if (bookDto.getImgLink().equals(""))
					book.setImgLink(getDefaultImgPath);
				else
					book.setImgLink(bookDto.getImgLink());
			// Thực hiện lưu sách mới để lấy ID
			else
				bookRepo.save(book);
		}
		
		// Nếu không upload ảnh từ máy thì thực hiện lưu book
		if (multiFile.isEmpty()) {
			bookRepo.save(book);
			return new Message(env.getProperty("message.save.success"));
		}
		
		// Nếu upload ảnh từ máy thì thực hiện lưu ảnh vào folder, xóa ảnh cũ và update link trong database
		try {
			String oldImgFolderPath = uploadImgPath + book.getId();
			File dir = new File(oldImgFolderPath);
			
			// Thực hiện xóa các file ảnh cũ có trong đường dẫn của ảnh sách update
			if (dir.exists())
				for (File file : dir.listFiles()) {
					if (!file.isDirectory())
						file.delete();
				}

			
			InputStream inputStream = multiFile.getInputStream();
			Path uploadPath = Paths.get(uploadImgPath + book.getId());

			// Nếu chưa có đường dẫn đến folder chứa ảnh thì tạo mới
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			String fileName = multiFile.getOriginalFilename();
			book.setImgLink(getImgPath + book.getId() + "/" + fileName);
			bookRepo.save(book);

			// Thực hiện lưu ảnh vào folder
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException(env.getProperty("message.save.img.error"));
		}

		return new Message(env.getProperty("message.save.success"));
	}

	/* Lấy danh sách Book DTO dựa vào tham số truyền vào */
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

	/* Lấy page Book DTO dựa vào tham số truyền vào */
	@Override
	public Page<Book> getPageBook(int pageNo, int pageSize, String searchKey, long categoryId, String sortByPrice) {
		Direction priceDirection;
		if (sortByPrice.equalsIgnoreCase(env.getProperty("value.price.asc"))) {
			sortByPrice = "price";
			priceDirection = Direction.ASC;
		} else if (sortByPrice.equalsIgnoreCase(env.getProperty("value.price.desc"))) {
			priceDirection = Direction.DESC;
			sortByPrice = "price";
		} else {
			priceDirection = Direction.ASC;
			sortByPrice = "id";
		}

		Sort sort = Sort.by(priceDirection, sortByPrice); 
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Book> page;
		if (categoryId != 0)
			page = bookRepo.findAllBySearchAndFilterAndSort(searchKey, categoryId, pageable);
		else
			page = bookRepo.findAllBySearchAndSort(searchKey, pageable);
		return page;
	}

	/* Lấy ID book để xóa book, và xóa các order detail liên quan */
	@Override
	public Message delete(Long id) {
		Optional<Book> opBook = bookRepo.findById(id);
		if (!opBook.isPresent())
			throw new ObjectNotFoundException(env.getProperty("message.not.find.book"));

		Book delBook = opBook.get();

		// Xóa folder chứa ảnh book cần xóa
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
		return new Message(env.getProperty("message.delete.book.success"));

	}

	/* Từ ID book lấy ra book DTO admin */
	@Override
	public AdminBookDto getUpdateBook(Long id) {
		Optional<Book> opBook = bookRepo.findById(id);
		if(!opBook.isPresent())throw new ObjectNotFoundException(env.getProperty("message.not.find.book"));
		AdminBookDto bookDto= new AdminBookDto();
		bookDto=bookMapper.fromBookToAdminDtoBook(opBook.get());
		return bookDto;
	}

	/* Lấy ra BookDto từ ID home */
	@Override
	public DisplayBookDto findBookDetailById(Long id) {
		Optional<Book> opBook= bookRepo.findById(id);
		if(!opBook.isPresent()) throw new ObjectNotFoundException(env.getProperty("message.not.find.book"));
		DisplayBookDto bookDto=bookMapper.fromBookToDisplayBookDto(opBook.get());
		return bookDto;
	}

	/* Lấy danh sách book bán chạy nhất */
	@Override
	public List<DisplayBookDto> findBestSellerBooks() {
		Pageable pageable = PageRequest.of(0, TOP_BEST_SELLER_AMOUNT);
		Page<Book> bookPage = orderDetailRepo.findTopBook(pageable);

		if(bookPage.isEmpty())
			bookPage= bookRepo.findAll(pageable);
		
		return bookPage.getContent().stream()
				.map(bookMapper::fromBookToDisplayBookDto)
				.collect(Collectors.toList());
	}
}












