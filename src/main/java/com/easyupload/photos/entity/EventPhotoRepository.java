package com.easyupload.photos.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPhotoRepository extends JpaRepository<EventPhoto, Long> {
	List<EventPhoto> findByEventId(Long eventId);
}