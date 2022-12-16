package com.nam.mapper;

import com.nam.dto.AdminBookDto;
import com.nam.dto.DisplayBookDto;
import com.nam.entity.Book;

public interface IBookMapper {

	DisplayBookDto fromBookToDisplayBookDto(Book book);

	Book fromAdminBookDtoToBook(AdminBookDto bookDto);

	AdminBookDto fromBookToAdminDtoBook(Book book);

}
