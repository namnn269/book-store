package com.nam.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
@PropertySource(value = "messages.properties", encoding = "utf-8")
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
	@Autowired
	private Environment env;
	
	/* Lấy ID của sách và số lượng để thêm vào giỏ hàng hiện tại */
	@Override
	public void addToCart(Long id, Long quantity) {
		User user = userService.getCurrentLoggedInUser();
		List<Order> orders = orderRepo.findByUserAndStatus(user, 0, 0);
		System.out.println(orders.isEmpty());
		Book book = bookRepo.findById(id).get();
		
		// Nếu số lượng vượt quá trong kho thì quăng ra lỗi
		if(book.getAmountInStock()<quantity) 
			throw new OverQuantityException(env.getProperty("message.buy.over.quantity"));
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setAmount(quantity);
		orderDetail.setBook(book);
		orderDetail.setPrice(book.getPrice());
		
		// Trường hợp khách hàng đang ko có order/cart nào -> tạo mới order và orderDetail
		if (orders.isEmpty()) {
			Order newOrder = new Order();
			Long now = Calendar.getInstance().getTimeInMillis();
			newOrder.setUser(user);
			newOrder.setStatus(0);
			newOrder.setOrderDate(new Timestamp(now));
			orderRepo.save(newOrder);
			orderDetail.setOrder(newOrder);
			orderDetailService.save(orderDetail);
			return;
		} 
		
		// Trường hợp đã có giỏ hàng hiện tại
		Order currentOrder = orders.get(0);
		
		// Lấy danh sách OrderDetail mà có chứa bookID giống với bookId truyền vào
		Optional<OrderDetail> opOrderDetails = currentOrder
								.getOrderDetails()
								.stream()
								.filter(x -> Objects.equals(x.getBook().getId(), id))
								.findFirst();
		
		// Nếu danh sách OrderDetail rỗng thì tạo mới 1 OrderDetail và add book mới thêm và quantity vào
		if (opOrderDetails.isEmpty()) {
			orderDetail.setOrder(currentOrder);
//			orderRepo.save(currentOrder);
			orderDetailService.save(orderDetail);
			return;
		}
		
		// Nếu đã có OrderDetail chứa book thì tăng quantity lên 1 lượng truyền vào
		orderDetail = opOrderDetails.get();
		orderDetail.setAmount(orderDetail.getAmount() + quantity);
		orderDetailService.save(orderDetail);
	}

	/* Lấy ra order hiện tại của user đang đăng nhập */
	@Override
	public Order findCurrentOrder(User user) {
		List<Order> orders = orderRepo.findByUserAndStatus(user, 0, 0);
		if (orders.isEmpty())
			return null;
		return orders.get(0);
	}

	/* Thực hiện tăng hoặc giảm số lượng book có trong order detail hiện tại */
	@Override
	public void changeAmountInOrderDetail(boolean increase, Long id) {
		try {
			User user = userService.getCurrentLoggedInUser();
			if (user == null)
				return;
			Order order = findCurrentOrder(user);
			Collection<OrderDetail> orderDetails = order.getOrderDetails();
			Optional<OrderDetail> opDetail = orderDetails
											.stream()
											.filter(x -> Objects.equals(x.getBook().getId(), id))
											.findFirst();
			if (opDetail.isEmpty())
				return;
			OrderDetail orderDetail = opDetail.get();
			// khi tăng 1 đơn vị
			if (increase) 
				orderDetail.setAmount(orderDetail.getAmount() + 1);
			// khi giảm 1 đơn vị
			else {
				// Nếu chỉ còn 1 đơn vị thì không thực hiện
				if (Objects.equals(orderDetail.getAmount(), 1))
					return;
				orderDetail.setAmount(orderDetail.getAmount() - 1);
			}
			orderDetailService.save(orderDetail);
		} catch (Exception e) {
		}
		
	}

	/* Nhận vào id book để thực hiện xóa orderdetail chứa book id tương ứng trong giỏ hàng hiện tại */
	@Override
	public void deleteProductInCart(Long id) {
		try {
			User user = userService.getCurrentLoggedInUser();
			if (user == null)
				return;
			Order order = findCurrentOrder(user);
			Collection<OrderDetail> orderDetails = order.getOrderDetails();
			Optional<OrderDetail> opDetail = orderDetails
										.stream()
										.filter(x->Objects.equals(x.getBook().getId(), id))
										.findFirst();
			if (opDetail.isEmpty())
				return;
			OrderDetail orderDetail = opDetail.get();
			Book book = orderDetail.getBook();
			book.removeOrderDetail(orderDetail);
			order.removeOrderDetail(orderDetail);
			orderrDetailRepo.delete(orderDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/* Thực hiện submit order hiện tại, đổi status order 0->1 */
	@Override
	public Message completeCurrentOrder() {
		User user = userService.getCurrentLoggedInUser();
		Order order = findCurrentOrder(user);
		if (order == null)
			throw new OrderFailureException(env.getProperty("message.order.error"));
		order.setStatus(1);
		orderRepo.save(order);
		return new Message(env.getProperty("message.order.success"));
	}

	/* Lấy ra danh sách order DTO đã tạo */
	@Override
	public List<AdminOrderDto> getListCreatedOrder() {
		List<AdminOrderDto> createdOrdersDtos= getListOrderByStatus(1);
		return createdOrdersDtos;
	}

	/* Lấy ra danh sách order DTO đã được duyệt */
	@Override
	public List<AdminOrderDto> getListConfirmedOrders() {
		List<AdminOrderDto> confirmedOrdersDtos= getListOrderByStatus(2);
		return confirmedOrdersDtos;
	}

	/* Nhận vào order id để thực hiện xác nhận order status order 1->2 */
	@Override
	public Message confirmOrder(Long orderId) {
		Optional<Order> opOrder= orderRepo.findById(orderId);
		if (opOrder.isPresent()) {
			Order confirmOrder = opOrder.get();
			confirmOrder.getOrderDetails().stream().forEach(od->{
				Book book= od.getBook();
				book.setAmountInStock(book.getAmountInStock() - od.getAmount());
				bookRepo.save(book);
			});
			confirmOrder.setStatus(2);
			orderRepo.save(confirmOrder);
			return new Message(env.getProperty("message.confirm.order.success"));
		} else {
			throw new ObjectNotFoundException(env.getProperty("message.not.find.order"));
		}
	}
	
	/* Lấy ra danh sách order DTO theo trạng thái */
	private List<AdminOrderDto> getListOrderByStatus(int status){
		List<Order> orders = orderRepo.findByStatus(status);
		@SuppressWarnings("null")
		List<AdminOrderDto> ordersDtos = orders.stream().map(order -> {
			AdminOrderDto adminOrder = new AdminOrderDto();
			adminOrder.setId(order.getId());
			adminOrder.setBuyer(userMapper.fromUserToUserDto(order.getUser()));
			adminOrder.setBookDtos(order.getOrderDetails().stream()
					.map(orderDetailMapper::fromOrderDetailToPurchasedBookDto)
					.collect(Collectors.toList()));
			adminOrder.setTotalMoney(order.getOrderDetails()
								.stream()
								.reduce(0d, (d, od) -> d + od.getPrice() * od.getAmount(), Double::sum));
			return adminOrder;
		}).collect(Collectors.toList());
		return ordersDtos;
	}
}
















