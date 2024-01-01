package com.domain.gym.gymspring.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository
        extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User>, CrudRepository<User, String> {
    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(int id);

    Optional<User> findByLoginid(String loginid);

    Optional<User> findByGym(int gym);
}
