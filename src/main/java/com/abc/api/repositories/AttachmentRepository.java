package com.abc.api.repositories;

import com.abc.api.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository <Attachment, Long> {
}
