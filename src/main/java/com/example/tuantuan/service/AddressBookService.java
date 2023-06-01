package com.example.tuantuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tuantuan.domain.AddressBook;
import com.example.tuantuan.domain.R;

public interface AddressBookService extends IService<AddressBook> {
    //给用户设置默认地址
    R<AddressBook> setDefault(AddressBook addressBook);
}
