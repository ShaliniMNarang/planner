package com.easyupload.photos.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventUsersRepository extends JpaRepository<EventUsers, String>{
	EventUsers findByUserId(String userId);
}
