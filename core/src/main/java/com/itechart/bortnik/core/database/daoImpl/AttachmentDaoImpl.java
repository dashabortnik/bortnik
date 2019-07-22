package com.itechart.bortnik.core.database.daoImpl;

import com.itechart.bortnik.core.database.AttachmentDao;
import com.itechart.bortnik.core.database.DatabaseUtil;
import com.itechart.bortnik.core.domain.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttachmentDaoImpl implements AttachmentDao {

    public AttachmentDaoImpl() {
    }

    // create the only instance of class and return it afterwards
    private static class Singleton {
        private static final AttachmentDaoImpl INSTANCE = new AttachmentDaoImpl();
    }

    public static AttachmentDaoImpl getInstance() {
        return Singleton.INSTANCE;
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(AttachmentDaoImpl.class);

    @Override
    public List<Attachment> readAllById(int id) {
        List<Attachment> attachments = new ArrayList<>();
        String sql = "SELECT * FROM attachment WHERE contact_id = ? ";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int attachmentId = resultSet.getInt("attachment_id");
                String name = resultSet.getString("attachment_name");
                String link = resultSet.getString("attachment_link");
                Date uploadDate = resultSet.getDate("upload_date");
                String commentary = resultSet.getString("commentary");
                int contactId = resultSet.getInt("contact_id");
                //create attachment from extracted data
                Attachment attach = new Attachment(attachmentId, name, link, uploadDate, commentary, contactId);
                attachments.add(attach);
            }
            logger.info("Attachments were fetched successfully.");
        } catch (Exception e) {
            logger.error("Error with fetching attachments from database: ", e);
        }
        return attachments;
    }

    @Override
    public Attachment insert(Attachment attachment) {
        String sql = "INSERT INTO attachment (attachment_name, attachment_link, upload_date, commentary, contact_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, attachment.getName());
            ps.setString(2, attachment.getLink());
            ps.setObject(3, attachment.getUploadDate());
            ps.setString(4, attachment.getCommentary());
            ps.setInt(5, attachment.getContactId());
            ps.execute();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                attachment.setId(generatedKeys.getInt(1));
            }
            logger.info("Attachment was saved successfully.");
        } catch (SQLException e) {
            logger.error("Error with inserting attachment into database: ", e);
        }
        return attachment;
    }

    @Override
    public Attachment update(Attachment attachment) {
        String sql = "UPDATE attachment SET attachment_name=?, attachment_link=?, upload_date=? , commentary=? WHERE contact_id = ?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, attachment.getName());
            ps.setString(2, attachment.getLink());
            ps.setObject(3, attachment.getUploadDate());
            ps.setString(4, attachment.getCommentary());
            ps.setInt(5, attachment.getContactId());
            ps.execute();
            logger.info("Attachment was updated successfully.");
        } catch (SQLException e) {
            logger.error("Error with updating attachment: ", e);
        }
        return attachment;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM attachment WHERE attachment_id=?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
            logger.info("Attachment with id {} was deleted successfully.", id);
        } catch (SQLException e) {
            logger.error("Error with deleting attachment: ", e);
        }
    }
}