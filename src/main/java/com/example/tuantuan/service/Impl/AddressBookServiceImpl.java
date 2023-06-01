package com.example.tuantuan.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tuantuan.Mapper.AddressBookMapper;
import com.example.tuantuan.domain.AddressBook;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.service.AddressBookService;
import com.example.tuantuan.utils.BaseContext;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Override
    public R<AddressBook> setDefault(AddressBook addressBook) {
        //获取当前用户的所有地址信息，将默认状态设置为0
        LambdaUpdateWrapper<AddressBook> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.set(AddressBook::getIsDefault,0);
        this.update(queryWrapper);
        //将当前获取到的地址信息中的默认状态设置为1
        addressBook.setIsDefault(1);
        //更新数据表中的信息
        this.updateById(addressBook);
        return R.success(addressBook);
    }
}
