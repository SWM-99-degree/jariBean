package com.example.jariBean.repository;

import com.example.jariBean.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}