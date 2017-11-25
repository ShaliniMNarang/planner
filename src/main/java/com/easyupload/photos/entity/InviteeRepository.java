package com.easyupload.photos.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteeRepository extends JpaRepository<Invitee, Long> {
	List<Invitee> findByEventId(Long eventId);
}