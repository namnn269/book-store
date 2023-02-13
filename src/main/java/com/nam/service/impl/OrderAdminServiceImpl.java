package com.nam.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nam.dto.AdminOrderDto;
import com.nam.entity.Book;
import com.nam.entity.Order;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.mapper.IOrderMapper;
import com.nam.repository.IBookRepository;
import com.nam.repository.IOrderRepository;
import com.nam.service.IOrderAdminService;

@Service
@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
public class OrderAdminServiceImpl implements IOrderAdminService {

	@Autowired
	private IOrderRepository orderRepo;
	@Autowired
	private IBookRepository bookRepo;
	@Autowired
	private IOrderMapper orderMapper;
	@Autowired
	private Environment env;
	
	/* Lấy ra danh sách order DTO đã tạo */
	@Override
	public List<AdminOrderDto> getListCreatedOrder(int pageNo, int pageSize, String sortByDate) {
		List<AdminOrderDto> createdOrdersDtos= getListOrderByStatus(pageNo,pageSize,sortByDate, 1, 0);
		return createdOrdersDtos;
	}

	/* Lấy ra danh sách order DTO đã được duyệt */
	@Override
	public List<AdminOrderDto> getListConfirmedOrders(int pageNo, int pageSize, String sortByDate) {
		List<AdminOrderDto> confirmedOrdersDtos= getListOrderByStatus(pageNo,pageSize,sortByDate, 2, 0);
		return confirmedOrdersDtos;
	}
	
	/* Lấy ra danh sách order DTO đã bị hủy */
	@Override
	public List<AdminOrderDto> getListCanceledOrders(int pageNo, int pageSize, String sortByDate, int cancelBy) {
		List<AdminOrderDto> confirmedOrdersDtos= getListOrderByStatus(pageNo, pageSize, sortByDate, 3, cancelBy);
		return confirmedOrdersDtos;
	}
	
	/* Lấy ra danh sách order DTO theo trạng thái */
	private List<AdminOrderDto> getListOrderByStatus(int pageNo, int pageSize, String sortByDate, int status, int cancelBy) {
		List<Order> orders = getPageble(pageNo,pageSize,sortByDate,status,cancelBy).getContent();
		List<AdminOrderDto> ordersDtos = orders.stream()
				.map(order->orderMapper.mapFromOrderToAdminOrderDto(order))
				.collect(Collectors.toList());
		return ordersDtos;
	}
	
	@Override
	public Page<Order> getPageble(int pageNo, int pageSize, String sortByDate, int status, int cancelBy) {
		Sort sort;

		if (sortByDate.equals("1")) {
			sort = Sort.by(Direction.DESC, "orderDate");
		} else if (sortByDate.equals("2")) {
			sort = Sort.by(Direction.ASC, "orderDate");
		} else {
			sort = Sort.by("id");
		}
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Order> orders;
		if (cancelBy == 0) {
			orders = orderRepo.findByStatus(status, pageable);
		} else {
			orders = orderRepo.findByStatusAndCancelBy(status, cancelBy, pageable);
		}
		return orders;
	}

	/* Nhận vào order id để thực hiện xác nhận order status order 1->2 */
	@Override
	public Message confirmOrder(Long orderId) {
		Optional<Order> opOrder = orderRepo.findById(orderId);
		if (opOrder.isEmpty())
			throw new ObjectNotFoundException(env.getProperty("message.not.find.order"));
		
		Order confirmOrder = opOrder.get();
		confirmOrder.getOrderDetails().stream().forEach(od -> {
			if (od.getBook().getAmountInStock() - od.getAmount() < 0)
				throw new ObjectNotFoundException(env.getProperty("message.buy.over.quantity"));
		});
		
		confirmOrder.getOrderDetails().stream().forEach(od -> {
			Book book = od.getBook();
			book.setAmountInStock(book.getAmountInStock() - od.getAmount());
			bookRepo.save(book);
		});
		
		confirmOrder.setStatus(2);
		orderRepo.save(confirmOrder);
		return new Message(env.getProperty("message.confirm.order.success"));
	}
	
	/* Nhận vào order id để thực hiện hủy order status order 1->3 */
	@Override
	public Message cancelOrder(Long orderId) {
		Optional<Order> opOrder = orderRepo.findById(orderId);
		if (opOrder.isPresent()) {
			Order cancelOrder = opOrder.get();
			cancelOrder.setStatus(3);
			cancelOrder.setCancelBy(1);
			orderRepo.save(cancelOrder);
			return new Message(env.getProperty("message.cancel.order.success"));
		} else {
			throw new ObjectNotFoundException(env.getProperty("message.not.find.order"));
		}
	}

	@Override
	public Order findOrderById(Long orderId) {
		return orderRepo.findById(orderId).orElse(null);
	}
}
















