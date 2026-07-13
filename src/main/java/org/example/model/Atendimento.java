package org.example.model;

public class Atendimento {
    private Integer id;
    private Integer medico_id;
    private Integer paciente_id;
    private String data;

    public Atendimento(Integer medico_id, Integer paciente_id, String data) {
        this.id = 0;
        this.medico_id = medico_id;
        this.paciente_id = paciente_id;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMedico_id() {
        return medico_id;
    }

    public void setMedico_id(Integer medico_id) {
        this.medico_id = medico_id;
    }

    public Integer getPaciente_id() {
        return paciente_id;
    }

    public void setPaciente_id(Integer paciente_id) {
        this.paciente_id = paciente_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Atendimento{" +
                "id=" + id +
                ", medico_id=" + medico_id +
                ", paciente_id=" + paciente_id +
                ", data='" + data + '\'' +
                '}';
    }
}
