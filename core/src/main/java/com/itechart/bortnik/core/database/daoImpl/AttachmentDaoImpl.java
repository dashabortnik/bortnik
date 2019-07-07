package com.itechart.bortnik.core.database.daoImpl;

import com.itechart.bortnik.core.database.AttachmentDao;
import com.itechart.bortnik.core.database.DatabaseUtil;
import com.itechart.bortnik.core.domain.Attachment;

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
                int contactId = resultSet.getInt("contact_id");

                Attachment attach = new Attachment(attachmentId, name, link, uploadDate, contactId);
                attachments.add(attach);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ATTACHMENTS:" + attachments.toString());
        return attachments;
    }

    @Override
    public Attachment insert(Attachment attachment) {
        String sql = "INSERT INTO attachment (attachment_name, attachment_link, upload_date, contact_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, attachment.getName());
            ps.setString(2, attachment.getLink());
            ps.setObject(3, attachment.getUploadDate());
            ps.setInt(4, attachment.getContactId());
            ps.execute();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                attachment.setId(generatedKeys.getInt(1));
            } // чтобы получить id добавленного элемента
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return attachment;
    }

    @Override
    public Attachment update(Attachment attachment) {
        String sql = "UPDATE attachment SET attachment_name=?, attachment_link=?, upload_date=? WHERE contact_id = ?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, attachment.getName());
            ps.setString(2, attachment.getLink());
            ps.setObject(3, attachment.getUploadDate());
            ps.setInt(4, attachment.getContactId());
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
