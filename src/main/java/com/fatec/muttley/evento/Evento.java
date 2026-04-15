package com.fatec.muttley.evento;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Evento {
    @Id
    private long id;
    private String tema;
    private String local;
    private Date data;
    private String horario;

    public Evento() {
        super();
    }
    public Evento(String tema, String local, Date data, String horario) {
        this.tema = tema;
        this.local = local;
        this.data = data;
        this.horario = horario;
    }

    public long getId() {
        return id;
    }
    public String getTema() {
        return tema;
    }
    public String getLocal() {
        return local;
    }
    public Date getData() {
        return data;
    }
    public String getHorario() {
        return horario;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setTema(String tema) {
        this.tema = tema;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return tema + "\n" + local + "\n" + horario;
    }
}
