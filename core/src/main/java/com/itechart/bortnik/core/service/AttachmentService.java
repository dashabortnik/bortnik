package com.itechart.bortnik.core.service;

import com.itechart.bortnik.core.domain.Attachment;

import java.util.List;

public interface AttachmentService extends BaseEntityService<Attachment>{

    List<Attachment> readAllAttachmentsByContactId(int id);

}
