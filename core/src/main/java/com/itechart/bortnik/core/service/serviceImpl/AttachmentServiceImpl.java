package com.itechart.bortnik.core.service.serviceImpl;

import com.itechart.bortnik.core.database.AttachmentDao;
import com.itechart.bortnik.core.database.daoImpl.AttachmentDaoImpl;
import com.itechart.bortnik.core.domain.Attachment;
import com.itechart.bortnik.core.service.AttachmentService;

import java.util.List;

public class AttachmentServiceImpl implements AttachmentService {

    private AttachmentDao attachmentDao;

    public AttachmentServiceImpl() {
        attachmentDao = new AttachmentDaoImpl();
    }

    @Override
    public Attachment save(Attachment entity) {
        return attachmentDao.insert(entity);
    }

    @Override
    public Attachment update(Attachment entity) {return attachmentDao.update(entity);}

    @Override
    public void remove(int id) {
        attachmentDao.delete(id);
    }

    @Override
    public List<Attachment> readAllAttachmentsByContactId(int id) {
        return attachmentDao.readAllById(id);
    }

}
