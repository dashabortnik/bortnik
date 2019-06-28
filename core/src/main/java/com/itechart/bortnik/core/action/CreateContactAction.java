package com.itechart.bortnik.core.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.Address;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.Gender;
import com.itechart.bortnik.core.domain.Marital;
import com.itechart.bortnik.core.service.AttachmentService;
import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.PhoneService;
import com.itechart.bortnik.core.service.serviceImpl.AttachmentServiceImpl;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;
import com.itechart.bortnik.core.service.serviceImpl.PhoneServiceImpl;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CreateContactAction implements BaseAction {

    private ContactService contactService;
    private PhoneService phoneService;
    private AttachmentService attachmentService;
    //private HttpServletResponse reader;

    public CreateContactAction() {
        contactService = new ContactServiceImpl();
        phoneService = new PhoneServiceImpl();
        attachmentService = new AttachmentServiceImpl();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("CREATE CONTACT ACTION");
        Contact receivedContact = null;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        List<FileItem> items = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            items = upload.parseRequest(request);
            String surname = null;
            String name = null;
            String patronymic = null;
            Date birthday = null;
            Gender gender = null;
            String nationality = null;
            Marital maritalStatus = null;
            String website = null;
            String email = null;
            String workplace = null;
            String country = null;
            String city = null;
            String street = null;
            String postcode = null;
            String photoLink = null;
            cycle:
            for (FileItem item : items) {
                //System.out.println(item.getFieldName() + " --- " + item.getString());

                if (item.isFormField()) {
                    switch (item.getFieldName()) {
                        case "surname": {
                            surname = item.getString();
                            break;
                        }
                        case "name": {
                            name = item.getString();
                            break;
                        }
                        case "patronymic": {
                            patronymic = item.getString();
                            break;
                        }
                        case "birthday": {
                            SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
                            birthday = dfShort.parse(item.getString());
                            java.sql.Date sqlDate = new java.sql.Date(birthday.getTime());
                            birthday = sqlDate;
                            break;
                        }
                        case "gender": {
                            gender = Gender.valueOf(item.getString().trim());
                            System.out.println("HHHHHHHHHH" + gender + "HHHHHHHHH");
                            break;
                        }
                        case "nationality": {
                            nationality = item.getString();
                            break;
                        }
                        case "marital": {
                            maritalStatus = Marital.valueOf(item.getString().trim());
                            System.out.println("HHHHHHHHHH" + maritalStatus + "HHHHHHHHH");
                            break;
                        }
                        case "website": {
                            website = item.getString();
                            break;
                        }
                        case "email": {
                            email = item.getString();
                            break;
                        }
                        case "workplace": {
                            workplace = item.getString();
                            break;
                        }
                        case "country": {
                            country = item.getString();
                            break;
                        }
                        case "city": {
                            city = item.getString();
                            break;
                        }
                        case "street": {
                            street = item.getString();
                            break;
                        }
                        case "postcode": {
                            postcode = item.getString();
                            break;
                        }
                        default: {
                            continue cycle;
                        }
                    }
                } else {
                    String path = FileSystems.getDefault().getPath("").toAbsolutePath() + File.separator + "img"
                            + File.separator + ThreadLocalRandom.current().nextInt(1, 2147483646 + 1) + ".jpg";;
                    File uploadedFile = new File(path);
                    item.write(uploadedFile);
                    System.out.println("Image path: " + uploadedFile.getAbsolutePath());
                    photoLink = uploadedFile.getAbsolutePath();
                        //1 //System.out.println("Working Directory = " + System.getProperty("user.dir"));
                        //2 //System.out.println("Current relative path is: " + Paths.get("").toAbsolutePath().toString());
                        //3 //System.out.println("LOCATION 2: " + FileSystems.getDefault().getPath("").toAbsolutePath());
                }
            }
            receivedContact = new Contact(0, surname, name, patronymic, birthday, gender, nationality, maritalStatus, website,
                    email, workplace, photoLink, new Address(0, country, city, street, postcode));
            System.out.println(receivedContact.toString());
        } catch (FileUploadException error) {
            System.out.println("Error With File Parsing - " + error.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (receivedContact!=null) {
            Contact fullContact = contactService.save(receivedContact);
            System.out.println("HEYYYYYY" + fullContact.toString());
            response.setHeader("Content-Type", "application/json; charset=UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(df);
            try (PrintWriter out = response.getWriter()){
            mapper.writeValue(out, fullContact);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        } else {
            //display OOPS message
        }
    }
}
