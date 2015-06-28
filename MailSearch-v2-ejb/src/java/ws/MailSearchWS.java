/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import entities.Campaign;
import entities.Email;
import entities.User;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author paul
 */
@WebService(serviceName = "MailSearchWS")
@XmlSeeAlso(User.class)
@Stateless()
public class MailSearchWS {
    public EntityManager em = Persistence.createEntityManagerFactory("MailSearch-v2-ejbPU").createEntityManager();
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getUsers")
    public ArrayList getUsers() {
        ArrayList<User> result = new ArrayList<>(em.createNamedQuery("User.findAll").getResultList());
        
        return result;
    }
    
    @WebMethod(operationName = "getCampaigns")
    public ArrayList getCampaigns() {
        ArrayList<User> result = new ArrayList<>(em.createNamedQuery("Campaign.findAll").getResultList());
        
        return result;
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "getCampaignById")
    public Campaign getCampaignById(@WebParam(name = "campaignId") int campaignId){
        Query query = em.createQuery("SELECT c FROM Campaign c WHERE c.id="+campaignId);
        Campaign campaign = (Campaign) query.getSingleResult();
        return campaign;
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "getUserByMail")
    public User getUserByMail(@WebParam(name = "email") String email){
        Query query = em.createQuery("SELECT u FROM User u WHERE u.mail='"+email+"'");
        User user = (User) query.getSingleResult();
        return user;
    }
    
     /**
     * Web service operation
     */
    @WebMethod(operationName = "getUserById")
    public User getUserById(@WebParam(name = "Id") int id){
        Query query = em.createQuery("SELECT u FROM User u WHERE u.id="+id);
        User user = (User) query.getSingleResult();
        return user;
    }
    
       /**
     * Web service operation
     */
    @WebMethod(operationName = "comparePassword")
    public Boolean comparePassword(@WebParam(name = "email") String email, @WebParam(name = "password") String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        boolean comparison = false;
        User us = this.getUserByMail(email);
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        String hashedPassword = new String(hash);
        
        if(hashedPassword.equals(us.getPassword()))
            comparison = true;
        
        return comparison;
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "addUser") //Fonctionne pas
    public void addUser(@WebParam(name = "email") String email, @WebParam(name = "password") String password){
       /* User user = null;
        user = getUserByMail(email);*/
        
       // if(user == null){
           /* MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            String hashedPassword = new String(hash);*/
            User us = new User();
            us.setMail(email);
            us.setPassword(password);
            em.persist(us);
            //us.setId(5);
            
            /*Query query = em.("INSERT INTO user (mail,password) VALUES (:email,:hashedPassword)");
            query.setParameter("email", email);
            query.setParameter("hashedPassword", hashedPassword);
            query.executeUpdate();*/
      //  }
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllCampaignsByUserId")//Fonctionne pas, type de userId ne correspond pas
    public List<Campaign> getAllCampaignsByUserId(@WebParam(name = "userId") int userId){
        
        Query query = em.createQuery("SELECT c FROM Campaign c WHERE c.userId="+userId);
        List<Campaign> listCampaigns = query.getResultList();
        
        return listCampaigns;
    }
    
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllCampaignsUserByStatus")//Fonctionne pas, type de userId ne correspond pas
    public List<Campaign> getAllCampaignsUserByStatus(@WebParam(name = "userId") int userId, @WebParam(name = "status") String status){

        
        Query query = em.createQuery("SELECT * FROM campaign WHERE userId="+userId+" AND status='"+status+"'");
        List<Campaign> listCampaigns = query.getResultList();

        return listCampaigns;
    }
    
     /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllEmailByCampaignId")
    public List<Email> getAllEmailByCampaignId(@WebParam(name = "campaignId") int campaignId, @WebParam(name = "userId") int userId){

        Query query = em.createQuery("SELECT e.id, e.email, e.selected, e.campaign_id FROM email e, campaign c, user u WHERE e.campaign_id = c.id AND c.userId = u.id AND c.id = "+campaignId+" AND u.id = "+userId+"");
        List<Email> listEmails = null;
        return listEmails;
    }

    /**
     * Web service operation
     */
//    @WebMethod(operationName = "addCampaign")
//    public void addCampaign(@WebParam(name = "keyword") String keyword, @WebParam(name = "mailObject") String mailObject, @WebParam(name = "mailContent") String mailContent, @WebParam(name = "mailFileName") String mailFileName, @WebParam(name = "mailFileContent") String mailFileContent, @WebParam(name = "userId") int userId) throws SQLException {
//        
//        Campaign campaign = new Campaign();
//        campaign.setKeyword(keyword);
//        campaign.setStatus(keyword);
//        Connection connection = this.getConnection();
//        Query query = em.createQuery("INSERT INTO campaign VALUES (keyword,status,mailObject,mailContent,mailFileName,mailFileContent,userId) VALUES (?,?,?,?,?,?,?)");
//        query.setString(1,keyword);
//        query.setString(2,"SCRAPPING_PENDING");
//        query.setString(3,mailObject);
//        query.setString(4,mailContent);
//        query.setString(5,mailFileName);
//        query.setString(6,mailFileContent);
//        query.setInt(7,userId);
//        
//        query.executeUpdate();
//    }
}
