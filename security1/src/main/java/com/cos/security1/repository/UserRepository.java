package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// 기본적으로 JpaRepository가 CRUD 메소드를 들고 있음
// @Repository 라는 어노테이션이 없어도 IoC가 됨 -> JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer> {
	// findBy 규칙, Username 문법
	// select * from user where username = ?
	public User findByUsername(String username);

}
