package com.nam.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nam.dto.AdminOrderDto;
import com.nam.entity.Book;
import com.nam.entity.Order;
import com.nam.entity.OrderDetail;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.exception_mesage.OrderFailureException;
import com.nam.exception_mesage.OverQuantityException;
import com.nam.mapper.IOrderDetailMapper;
import com.nam.mapper.IUserMapper;
import com.nam.repository.IBookRepository;
import com.nam.repository.IOrderDetailRepository;
import com.nam.repository.IOrderRepository;
import com.nam.service.IOrderDetailService;
import com.nam.service.IOrderService;
import com.nam.service.IUserService;

@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private IOrderRepository orderRepo;
	@Autowired
	private IBookRepository bookRepo;
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderDetailService orderDetailService;
	@Autowired
	private IOrderDetailRepository orderrDetailRepo;
	@Autowired
	private IOrderDetailMapper orderDetailMapper;
	@Autowired
	private IUserMapper userMapper;
	
	@Override
	public List<Order> findAllByUser(User user) {
//		List<Order> orders=orderRepo.findByUser(user);
		List<Order> orders2= new ArrayList<>();
		
		return orders2;
	}
	
	@Override
	public void addToCart(Long id, Long quantity) {
		User user = userService.getCurrentLoggedInUser();
		List<Order> orders = orderRepo.findByUserAndStatus(user, 0, 0);
		Book book = bookRepo.findById(id).get();
		
		if(book.getAmountInStock()<quantity) 
			throw new OverQuantityException("Số lượng mua vượt quá số lượng hiện tại!");
		book.setAmountInStock(book.getAmountInStock()-quantity);
		
		// Trường hợp khách hàng đang ko có order/cart nào -> tạo mới order và orderDetail
		if (orders.isEmpty()) {
			Order newOrder = new Order();
			Long now = Calendar.getInstance().getTimeInMillis();
			newOrder.setUser(user);
			newOrder.setStatus(0);
			newOrder.setAddress(user.getProfileUser().getAddress());
			newOrder.setOrderDate(new Timestamp(now));
			orderRepo.save(newOrder);
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setAmount(quantity);
			orderDetail.setBook(book);
			orderDetail.setPrice(book.getPrice());
			orderDetail.setOrder(newOrder);
			orderDetailService.save(orderDetail);
			return;
		} 
		
		// Trường hợp đã có giỏ hàng hiện tại
		Order currentOrder = orders.get(0);
		
		// Lấy danh sách OrderDetail mà có chứa bookID giống với bookId truyền vào
		Optional<OrderDetail> opOrderDetails = orders.get(0)
							.getOrderDetails().stream()
							.filter(x -> Objects.equals(x.getBook().getId(), id))
							.findFirst();
		
		// Nếu danh sách OrderDetail rỗng thì tạo mới 1 OrderDetail và add book mới thêm và quantity vào
		if (opOrderDetails.isEmpty()) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setAmount(quantity);
			orderDetail.setBook(book);
			orderDetail.setPrice(book.getPrice());
			orderDetail.setOrder(currentOrder);
			currentOrder.getOrderDetails().add(orderDetail);
			
			orderRepo.save(currentOrder);
			return;
		}
		
		// Nếu đã có OrderDetail chứa book thì tăng quantity lên 1 lượng truyền vào
		OrderDetail orderDetail=opOrderDetails.get();
		orderDetail.setAmount(orderDetail.getAmount()+quantity);
		orderDetailService.save(orderDetail);
	}

	@Override
	public Order findCurrentOrder(User user) {
		List<Order> orders= orderRepo.findByUserAndStatus(user, 0, 0);
		if(orders.isEmpty())return null;
		return orders.get(0);
	}

	@Override
	public void changeAmountInOrderDetail(boolean increase, Long id) {
		try {
			User user = userService.getCurrentLoggedInUser();
			if(user==null)return;
			Order order= findCurrentOrder(user);
			Collection<OrderDetail> orderDetails=	order.getOrderDetails();
			Optional<OrderDetail> opDetail = orderDetails
										.stream()
										.filter(x->Objects.equals(x.getBook().getId(), id))
										.findFirst();
			if (opDetail.isEmpty())
				return;
			OrderDetail orderDetail = opDetail.get();
			if (increase) {
				orderDetail.setAmount(orderDetail.getAmount() + 1);
			} else {
				if (Objects.equals(orderDetail.getAmount(), 1))
					return;
				orderDetail.setAmount(orderDetail.getAmount() - 1);
			}
			orderDetailService.save(orderDetail);
		} catch (Exception e) {
		}
		
	}

	@Override
	public void deleteProductInCart(Long id) {
		try {
			User user = userService.getCurrentLoggedInUser();
			if(user==null)return;
			Order order= findCurrentOrder(user);
			Collection<OrderDetail> orderDetails = order.getOrderDetails();
			Optional<OrderDetail> opDetail = orderDetails
										.stream()
										.filter(x->Objects.equals(x.getBook().getId(), id))
										.findFirst();
			if (opDetail.isEmpty())
				return;
			OrderDetail orderDetail = opDetail.get();
			Book book=orderDetail.getBook();
			book.removeOrderDetail(orderDetail);
			order.removeOrderDetail(orderDetail);
			orderrDetailRepo.delete(orderDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Message completeCurrentOrder() {
		User user = userService.getCurrentLoggedInUser();
		Order order = findCurrentOrder(user);
		if (order == null)
			throw new OrderFailureException("Đặt hàng thất bại!");
		order.setStatus(1);
		orderRepo.save(order);
		return new Message("Đặt hàng thành công!");
	}

	@Override
	public List<AdminOrderDto> getListCreatedOrder() {
		List<AdminOrderDto> createdOrdersDtos= getListOrderByStatus(1);
		return createdOrdersDtos;
	}

	@Override
	public List<AdminOrderDto> getListConfirmedOrders() {
		List<AdminOrderDto> confirmedOrdersDtos= getListOrderByStatus(2);
		return confirmedOrdersDtos;
	}

	
	@Override
	public Message confirmOrder(Long orderId) {
		Optional<Order> opOrder= orderRepo.findById(orderId);
		if (opOrder.isPresent()) {
			Order confirmOrder = opOrder.get();
			confirmOrder.setStatus(2);
			orderRepo.save(confirmOrder);
			return new Message("Xác nhận thành công! ID: "+orderId);
		} else {
			throw new ObjectNotFoundException("Không tìm thấy order!");
		}
	}
	
	private List<AdminOrderDto> getListOrderByStatus(int status){
		List<Order> orders = orderRepo.findByStatus(status);
		List<AdminOrderDto> ordersDtos = orders.stream().map(order -> {
			AdminOrderDto adminOrder = new AdminOrderDto();
			adminOrder.setId(order.getId());
			adminOrder.setBuyer(userMapper.fromUserToUserDto(order.getUser()));
			adminOrder.setBookDtos(order.getOrderDetails().stream()
					.map(orderDetailMapper::fromOrderDetailToPurchasedBookDto)
					.collect(Collectors.toList()));
			return adminOrder;
		}).collect(Collectors.toList());
		return ordersDtos;
	}
}
















