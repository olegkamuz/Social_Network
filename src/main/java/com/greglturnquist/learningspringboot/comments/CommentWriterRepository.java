package com.greglturnquist.learningspringboot.comments;

import reactor.core.publisher.Mono;

import org.springframework.data.repository.Repository;

public interface CommentWriterRepository extends Repository<Comment, String> {

	Mono<Comment> save(Comment newComment);

	// Needed to support save()
	Mono<Comment> findById(String id);

	Mono<Void> deleteAll();
}
