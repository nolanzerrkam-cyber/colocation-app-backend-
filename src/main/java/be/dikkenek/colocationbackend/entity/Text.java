package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.TextDAO;
import jakarta.persistence.*;

@Entity
@Table(name = "Text")
public class Text
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "message", length = 50, nullable = false)
    private String msg;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Text(){}

    public Text(String msg)
    {
        setMsg(msg);
    }

    public boolean Create(TextDAO dao)
    {
        return dao.Create(this);
    }

    public static Text findById(TextDAO dao, Long id)
    {
        return dao.findById(id);
    }
}
