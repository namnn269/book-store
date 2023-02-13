package com.nam.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

import com.nam.dto.OrderDto;
import com.nam.dto.ProfileUserDto;
import com.nam.entity.Book;
import com.nam.entity.Order;
import com.nam.entity.OrderDetail;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.exception_mesage.OrderFailureException;
import com.nam.exception_mesage.OverQuantityException;
import com.nam.mapper.IOrderMapper;
import com.nam.repository.IBookRepository;
import com.nam.repository.IOrderDetailRepository;
import com.nam.repository.IOrderRepository;
import com.nam.service.IOrderDetailService;
import com.nam.service.IOrderUserService;
import com.nam.service.IUserService;

@Service
@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
public class OrderUserServiceImpl implements IOrderUserService {

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
	private IOrderMapper orderMapper;
	@Autowired
	private Environment env;
	
	/* Lấy ID của sách và số lượng để thêm vào giỏ hàng hiện tại */
	@Override
	public void addToCart(Long id, Long quantity) {
		User user = userService.getCurrentLoggedInUser();
		List<Order> orders = orderRepo.findByUserAndStatus(user, 0, 0);
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
			Optional<OrderDetail> opDetail = orderDetails.stream()
					.filter(x -> x.getBook().getId() == id)
					.findFirst();
			
			if(opDetail.isEmpty())
				throw new ObjectNotFoundException(env.getProperty("message.not.find.order"));
			OrderDetail orderDetail = opDetail.get();
			
			Optional<Book> opBook= bookRepo.findById(orderDetail.getBook().getId());
			if ( opBook.isEmpty())
				throw new ObjectNotFoundException(env.getProperty("message.not.find.book"));
			// khi tăng 1 đơn vị
			if (increase)
				orderDetail.setAmount(orderDetail.getAmount() + 1);
			// khi giảm 1 đơn vị
			else {
				// Nếu chỉ còn 1 đơn vị thì không thực hiện
				if (orderDetail.getAmount() == 1)
					return;
				orderDetail.setAmount(orderDetail.getAmount() - 1);
			}
			orderDetailService.save(orderDetail);
		} catch (Exception e) {
			throw new ObjectNotFoundException(env.getProperty("message.not.find.book"));
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
	public Message submitCurrentOrder(ProfileUserDto profileUserDto) {
		User user = userService.getCurrentLoggedInUser();
		Order order = findCurrentOrder(user);
		if (order == null)
			throw new OrderFailureException(env.getProperty("message.order.error"));
		order.setReceiver(profileUserDto.getFullName());
		order.setPhoneNumbber(profileUserDto.getPhoneNumber());
		order.setAddress(profileUserDto.getAddress());
		order.setOrderDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		order.setStatus(1);
		orderRepo.save(order);
		return new Message(env.getProperty("message.order.success"));
	}

	@Override
	public List<OrderDto> findCurrentOrdersUser(int pageNo, int pageSize, String sortByDate, int status) {
		return getPageble(pageNo, pageSize, sortByDate, status)
				.map(order->orderMapper.fromOrderToOrderDto(order))
				.toList();
	}

	@Override
	public Page<Order> getPageble(int pageNo, int pageSize, String sortByDate, int status) {
		User user = userService.getCurrentLoggedInUser();
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
		if (status == 1) {
			orders = orderRepo.findByStatusAndUser(1, user, pageable);
		} else if (status == 2) {
			orders = orderRepo.findByStatusAndUser(2, user, pageable);
		} else if (status == 3) {
			orders = orderRepo.findByStatusAndUser(3, user, pageable);
		} else {
			orders = orderRepo.findAllByUser(user, pageable);
		}
		return orders;
	}

	/* Nhận vào order id để thực hiện hủy order status order 1->3 */
	@Override
	public Message cancelOrder(Long orderId) {
		Optional<Order> opOrder = orderRepo.findById(orderId);
		if (opOrder.isPresent()) {
			Order cancelOrder = opOrder.get();
			cancelOrder.setStatus(3);
			cancelOrder.setCancelBy(2);
			orderRepo.save(cancelOrder);
			return new Message(env.getProperty("message.cancel.order.success"));
		} else {
			throw new ObjectNotFoundException(env.getProperty("message.not.find.order"));
		}
	}

}
















