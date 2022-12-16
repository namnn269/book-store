package com.nam.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "book_title", length = 250)
	private String bookTitle;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "img_link", length = 500)
	private String imgLink;

	@Column(name = "price")
	private double price;
	
	@Column(name = "entry_price")
	private double entryPrice;
	
	@Column(name = "publishing_year")
	private Integer publishingYear;
	
	@Column(name = "amount_in_stock")
	private Long amountInStock;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "author_book",
				joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
	private Collection<Author> authors ;
	
	@ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;
	
	@OneToMany(mappedBy = "book",cascade =CascadeType.ALL ,  fetch = FetchType.LAZY)
	private Collection<OrderDetail> orderDetails=new ArrayList<>();
	
	public void removeAuthor(Author author) {
		this.authors.remove(author);
		}
	
	public void removeAuthorList(List<Author> authors) {
		authors.stream().forEach(x->{this.authors.remove(x);});
	}
	
	public void removeAllAuthors() {
		this.authors=null;
	}
	
	public void removeOrderDetail(OrderDetail orderDetail) {
		this.orderDetails.remove(orderDetail);
		orderDetail.setBook(null);
	}
	
	public void removeAllOrderDetail() {
		this.orderDetails=null;
	}
	
	public void removeCategory() {
		this.category.getBooks().remove(this);
		this.category = null;
	}
	
}

















