package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserService {

    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public List<User> findAll(Sort sort) {
        return userRepo.findAll(sort);
    }

    public List<User> findAllById(Iterable<Long> longs) {
        return userRepo.findAllById(longs);
    }

    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return userRepo.saveAll(entities);
    }

    public void flush() {
        userRepo.flush();
    }

    public <S extends User> S saveAndFlush(S entity) {
        return userRepo.saveAndFlush(entity);
    }

    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return userRepo.saveAllAndFlush(entities);
    }

    @Deprecated
    public void deleteInBatch(Iterable<User> entities) {
        userRepo.deleteInBatch(entities);
    }

    public void deleteAllInBatch(Iterable<User> entities) {
        userRepo.deleteAllInBatch(entities);
    }

    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        userRepo.deleteAllByIdInBatch(longs);
    }

    public void deleteAllInBatch() {
        userRepo.deleteAllInBatch();
    }

    @Deprecated
    public User getOne(Long aLong) {
        return userRepo.getOne(aLong);
    }

    @Deprecated
    public User getById(Long aLong) {
        return userRepo.getById(aLong);
    }

    public User getReferenceById(Long aLong) {
        return userRepo.getReferenceById(aLong);
    }

    public <S extends User> List<S> findAll(Example<S> example) {
        return userRepo.findAll(example);
    }

    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return userRepo.findAll(example, sort);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    public <S extends User> S save(S entity) {
        return userRepo.save(entity);
    }

    public Optional<User> findById(Long aLong) {
        return userRepo.findById(aLong);
    }

    public boolean existsById(Long aLong) {
        return userRepo.existsById(aLong);
    }

    public long count() {
        return userRepo.count();
    }

    public void deleteById(Long aLong) {
        userRepo.deleteById(aLong);
    }

    public void delete(User entity) {
        userRepo.delete(entity);
    }

    public void deleteAllById(Iterable<? extends Long> longs) {
        userRepo.deleteAllById(longs);
    }

    public void deleteAll(Iterable<? extends User> entities) {
        userRepo.deleteAll(entities);
    }

    public void deleteAll() {
        userRepo.deleteAll();
    }

    public <S extends User> Optional<S> findOne(Example<S> example) {
        return userRepo.findOne(example);
    }

    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return userRepo.findAll(example, pageable);
    }

    public <S extends User> long count(Example<S> example) {
        return userRepo.count(example);
    }

    public <S extends User> boolean exists(Example<S> example) {
        return userRepo.exists(example);
    }

    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return userRepo.findBy(example, queryFunction);
    }

    @Query(value = "SELECT * FROM user WHERE username=:username", nativeQuery = true)
    public User getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }
}
