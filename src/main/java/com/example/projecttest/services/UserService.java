package com.example.projecttest.services;

import com.example.projecttest.models.Users;
import com.example.projecttest.repositories.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private IUsersRepository usersRepository;

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public boolean save(Users obj) {
        boolean flag = false;
        Users tmp = usersRepository.save(obj);
        if (!tmp.equals(null)) {
            flag = true;
        }
        return flag;
    }

    public  Boolean existByUsername(String username){return  usersRepository.existsByUsername(username);}
}
