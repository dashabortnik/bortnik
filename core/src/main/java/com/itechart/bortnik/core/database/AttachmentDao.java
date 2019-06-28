package com.itechart.bortnik.core.database;

import com.itechart.bortnik.core.domain.Attachment;
import java.util.List;

public interface AttachmentDao extends BaseEntityDao<Attachment> {

    //all attachments of one contact id
    List<Attachment> readAllById(int id);
}
