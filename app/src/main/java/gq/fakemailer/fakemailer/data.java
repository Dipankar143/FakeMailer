package gq.fakemailer.fakemailer;

/**
 * Created by dipanker on 23/06/17.
 */

public class data  {
    public data(String url,String email,String subject,String pos) {
        this.setUrl(url);
        this.setEmail(email);
        this.setSubject(subject);
        this.setPos(pos);

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public  String getEmail(){
        return  email;
    }

    private String url;
    private  String email;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String subject;
    private  String pos;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
