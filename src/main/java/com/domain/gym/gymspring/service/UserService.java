package com.domain.gym.gymspring.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.domain.gym.gymspring.domain.User;
import com.domain.gym.gymspring.domain.UserRepository;
import com.domain.gym.gymspring.domain.UserSpecs;
import com.domain.gym.gymspring.domain.UserSpecs.SearchKey;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository repository;

    public String sha256(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(msg.getBytes());

            byte[] bytes = md.digest();
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
        }

        return "";
    }

    @Transactional
    public User insert(User item) {
        Optional<User> opt = repository.findById(item.getId());

        if (opt.isPresent()) {
            return null;
        }

        /*
         * String passwd = sha256(item.getPasswd());
         * item.setPasswd(passwd);
         */

        LocalDateTime localDateTime = LocalDateTime.now().plusHours(9);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        item.setDate(timestamp);

        return repository.save(item);
    }

    @Transactional
    public User update(User item) {
        Optional<User> opt = repository.findById(item.getId());

        if (!opt.isPresent()) {
            return null;
        }

        User old = opt.get();

        String passwd = item.getPasswd();
        if (StringUtils.isEmpty(passwd)) {
            item.setPasswd(old.getPasswd());
        } else {
            // item.setPasswd(sha256(passwd));
            item.setPasswd(passwd);
        }

        item.setDate(old.getDate());

        return repository.save(item);
    }

    @Transactional
    public void delete(User item) {
        repository.delete(item);
    }

    public Optional<User> findById(int id) {
        return repository.findById(id);
    }

    public Optional<User> findByLoginid(String id) {
        return repository.findByLoginid(id);
    }

    public Optional<User> findByGym(int id) {
        return repository.findByGym(id);
    }

    public Page<User> findAll(Map<SearchKey, Object> searchKeys, String order, int page, int size) {
        Sort sort = null;
        boolean desc = false;

        if (StringUtils.isEmpty(order)) {
            order = "id";
        } else {
            if (StringUtils.right(order, 4).equals("Desc")) {
                order = StringUtils.left(order, order.length() - 4);
                desc = true;
            }
        }

        sort = Sort.by(order);

        if (desc) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return searchKeys.isEmpty()
                ? repository.findAll(pageable)
                : repository.findAll(UserSpecs.searchWith(searchKeys), pageable);
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}
